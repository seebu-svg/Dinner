package com.example.data.repository

import com.example.data.firestore.FirestoreManager
import com.example.data.local.ModelMappers.toDomain
import com.example.data.local.ModelMappers.toEntity
import com.example.data.local.SocialDiningDao
import com.example.data.model.Booking
import com.example.data.model.CollaborationProposal
import com.example.data.model.DinnerEvent
import com.example.data.model.EventStatus
import com.example.data.model.Host
import com.example.data.model.MenuCourse
import com.example.data.model.ProposalStatus
import com.example.data.model.Restaurant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID

class SocialDiningRepository(
    private val dao: SocialDiningDao,
    private val firestoreManager: FirestoreManager? = null
) {

    val dinnerEvents: Flow<List<DinnerEvent>> = dao.getAllDinnerEvents().map { list ->
        list.map { it.toDomain() }
    }

    val restaurants: Flow<List<Restaurant>> = dao.getAllRestaurants().map { list ->
        list.map { it.toDomain() }
    }

    val hosts: Flow<List<Host>> = dao.getAllHosts().map { list ->
        list.map { it.toDomain() }
    }

    val bookings: Flow<List<Booking>> = dao.getAllBookings().map { list ->
        list.map { it.toDomain() }
    }

    val proposals: Flow<List<CollaborationProposal>> = dao.getAllProposals().map { list ->
        list.map { it.toDomain() }
    }

    fun getDinnerEvent(id: Long): Flow<DinnerEvent?> {
        return dao.getDinnerEventById(id).map { it?.toDomain() }
    }

    fun getRestaurant(id: Long): Flow<Restaurant?> {
        return dao.getRestaurantById(id).map { it?.toDomain() }
    }

    fun getBookingsForEvent(eventId: Long): Flow<List<Booking>> {
        return dao.getBookingsForEvent(eventId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun bookSeats(
        eventId: Long,
        seatsCount: Int,
        guestName: String,
        guestEmail: String,
        dietaryRestrictions: String,
        seatingVibe: String
    ): Result<Booking> {
        val eventEntity = dao.getDinnerEventById(eventId).firstOrNull()
            ?: return Result.failure(Exception("Event not found"))

        val event = eventEntity.toDomain()
        if (event.remainingSeats < seatsCount) {
            return Result.failure(Exception("Only ${event.remainingSeats} seats remaining!"))
        }

        val totalPrice = event.pricePerSeat * seatsCount
        val randomSuffix = UUID.randomUUID().toString().take(6).uppercase()
        val qrCode = "TAVOLA-${event.id}-$randomSuffix"

        val booking = Booking(
            dinnerEventId = event.id,
            dinnerTitle = event.title,
            venueName = event.venueName,
            dateString = event.dateString,
            timeString = event.timeString,
            guestName = guestName,
            guestEmail = guestEmail,
            seatsCount = seatsCount,
            pricePerSeat = event.pricePerSeat,
            totalPrice = totalPrice,
            dietaryRestrictions = dietaryRestrictions.ifBlank { "None" },
            seatingVibe = seatingVibe,
            qrTicketCode = qrCode,
            bookingTimestamp = System.currentTimeMillis(),
            isCheckedIn = false
        )

        val bookingId = dao.insertBooking(booking.toEntity())
        val savedBooking = booking.copy(id = bookingId)

        // Increment seats and check cover guarantee
        val newBooked = event.bookedSeats + seatsCount
        val newStatus = if (newBooked >= event.minCoversRequired) {
            EventStatus.CONFIRMED.name
        } else {
            eventEntity.status
        }

        dao.incrementBookedSeats(eventId, seatsCount)
        dao.updateDinnerStatus(eventId, newStatus)

        // Sync with Firestore
        firestoreManager?.updateBookedSeats(eventId, newBooked, newStatus)

        // Venue gets revenue & cover counts
        dao.addGuaranteedCoversAndRevenue(event.venueId, seatsCount, totalPrice * 0.8)

        // Host gets commission
        val hostEarnings = totalPrice * (event.hostCutPercent / 100.0)
        dao.addHostEarnings(event.hostId, hostEarnings, 0)

        return Result.success(savedBooking)
    }

    suspend fun createHostPitch(proposal: CollaborationProposal): Long {
        return dao.insertProposal(proposal.toEntity())
    }

    suspend fun acceptProposal(proposalId: Long): Result<DinnerEvent> {
        val allProposals = dao.getAllProposals().firstOrNull() ?: emptyList()
        val propEntity = allProposals.find { it.id == proposalId }
            ?: return Result.failure(Exception("Proposal not found"))

        dao.updateProposalStatus(proposalId, ProposalStatus.ACCEPTED.name)

        // Also create a confirmed / pending dinner event for this partnership
        val venueEntity = dao.getRestaurantById(propEntity.restaurantId).firstOrNull()
        val newDinner = DinnerEvent(
            title = propEntity.eventTitle,
            description = "${propEntity.pitchNote} | Vision: ${propEntity.menuVision}",
            hostId = propEntity.hostId,
            hostName = propEntity.hostName,
            hostHandle = propEntity.hostHandle,
            hostNiche = propEntity.hostNiche,
            hostFollowers = propEntity.hostFollowers,
            venueId = propEntity.restaurantId,
            venueName = propEntity.restaurantName,
            venueAddress = venueEntity?.address ?: "Partner Venue Address",
            venueNeighborhood = venueEntity?.neighborhood ?: "Downtown",
            dateString = propEntity.proposedDate,
            timeString = propEntity.proposedTime,
            pricePerSeat = propEntity.pricePerSeat,
            totalSeats = propEntity.targetCovers,
            bookedSeats = 0,
            minCoversRequired = propEntity.minGuaranteeCovers,
            status = EventStatus.PENDING_COVERS,
            menuCourses = listOf(
                MenuCourse(1, "Chef's Welcome Amuse", "Seasonal botanical bite & sparkling pairing", "Signature House Welcome"),
                MenuCourse(2, "Collaborative Tasting Course", propEntity.menuVision, "Sommelier Curated Pairing"),
                MenuCourse(3, "Artisanal Dessert & Digestif", "Curated dessert with digestif", "Amaro / Digestif")
            ),
            vibeTags = listOf("New Collaboration", "Guaranteed Covers", "Exclusive Menu"),
            dressCode = "Smart Casual",
            icebreakerPrompts = listOf(
                "What inspired you to join tonight's table?",
                "Favorite dining destination in the world?"
            )
        )

        val newId = dao.insertDinnerEvent(newDinner.toEntity())
        val savedEvent = newDinner.copy(id = newId)
        firestoreManager?.saveDinnerEvent(savedEvent)
        return Result.success(savedEvent)
    }

    suspend fun declineProposal(proposalId: Long) {
        dao.updateProposalStatus(proposalId, ProposalStatus.DECLINED.name)
    }

    suspend fun createDinnerDirectly(dinner: DinnerEvent): Long {
        return dao.insertDinnerEvent(dinner.toEntity())
    }

    suspend fun setCheckIn(bookingId: Long, checkedIn: Boolean) {
        dao.updateCheckInStatus(bookingId, checkedIn)
    }
}
