package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.EventStatus
import com.example.data.model.ProposalStatus

@Entity(tableName = "dinner_events")
data class DinnerEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val hostId: Long,
    val hostName: String,
    val hostHandle: String,
    val hostNiche: String,
    val hostFollowers: String,
    val hostRating: Float,
    val venueId: Long,
    val venueName: String,
    val venueAddress: String,
    val venueNeighborhood: String,
    val dateString: String,
    val timeString: String,
    val pricePerSeat: Double,
    val totalSeats: Int,
    val bookedSeats: Int,
    val minCoversRequired: Int,
    val status: String = EventStatus.PENDING_COVERS.name,
    val menuCoursesJson: String,
    val vibeTagsJson: String,
    val dressCode: String,
    val icebreakerPromptsJson: String,
    val hostCutPercent: Int = 20,
    val specialNotes: String = ""
)

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val cuisine: String,
    val address: String,
    val neighborhood: String,
    val rating: Float,
    val reviewCount: Int,
    val coversGuaranteedTotal: Int,
    val totalRevenueGenerated: Double,
    val description: String,
    val minCoversForPrivateRoom: Int,
    val maxRoomCapacity: Int,
    val contactName: String,
    val isPartnerVerified: Boolean,
    val offPeakSlotsJson: String
)

@Entity(tableName = "hosts")
data class HostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val handle: String,
    val niche: String,
    val followerCount: String,
    val bio: String,
    val rating: Float,
    val dinnersHostedCount: Int,
    val totalEarnings: Double,
    val signatureDishStyle: String,
    val commissionPercent: Int
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dinnerEventId: Long,
    val dinnerTitle: String,
    val venueName: String,
    val dateString: String,
    val timeString: String,
    val guestName: String,
    val guestEmail: String,
    val seatsCount: Int,
    val pricePerSeat: Double,
    val totalPrice: Double,
    val dietaryRestrictions: String,
    val seatingVibe: String,
    val qrTicketCode: String,
    val bookingTimestamp: Long,
    val isCheckedIn: Boolean
)

@Entity(tableName = "proposals")
data class CollaborationProposalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hostId: Long,
    val hostName: String,
    val hostHandle: String,
    val hostFollowers: String,
    val hostNiche: String,
    val restaurantId: Long,
    val restaurantName: String,
    val eventTitle: String,
    val proposedDate: String,
    val proposedTime: String,
    val targetCovers: Int,
    val minGuaranteeCovers: Int,
    val pricePerSeat: Double,
    val pitchNote: String,
    val menuVision: String,
    val status: String = ProposalStatus.PENDING.name,
    val timestamp: Long = System.currentTimeMillis()
)
