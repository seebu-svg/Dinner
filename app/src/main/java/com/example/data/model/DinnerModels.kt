package com.example.data.model

enum class MarketplaceRole {
    DINER,
    HOST,
    RESTAURANT
}

enum class EventStatus {
    PENDING_COVERS,
    CONFIRMED,
    COMPLETED
}

enum class ProposalStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}

data class MenuCourse(
    val courseNumber: Int,
    val title: String,
    val description: String,
    val winePairing: String = ""
)

data class DinnerEvent(
    val id: Long = 0,
    val title: String,
    val description: String,
    val hostId: Long,
    val hostName: String,
    val hostHandle: String,
    val hostNiche: String,
    val hostFollowers: String,
    val hostRating: Float = 4.9f,
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
    val status: EventStatus = EventStatus.PENDING_COVERS,
    val menuCourses: List<MenuCourse> = emptyList(),
    val vibeTags: List<String> = emptyList(),
    val dressCode: String = "Smart Casual",
    val icebreakerPrompts: List<String> = emptyList(),
    val hostCutPercent: Int = 20,
    val specialNotes: String = ""
) {
    val isConfirmed: Boolean
        get() = bookedSeats >= minCoversRequired || status == EventStatus.CONFIRMED

    val coversProgress: Float
        get() = if (minCoversRequired > 0) (bookedSeats.toFloat() / minCoversRequired.toFloat()).coerceIn(0f, 1f) else 1f

    val remainingSeats: Int
        get() = (totalSeats - bookedSeats).coerceAtLeast(0)

    val seatsToConfirm: Int
        get() = (minCoversRequired - bookedSeats).coerceAtLeast(0)
}

data class Restaurant(
    val id: Long = 0,
    val name: String,
    val cuisine: String,
    val address: String,
    val neighborhood: String,
    val rating: Float = 4.8f,
    val reviewCount: Int = 120,
    val coversGuaranteedTotal: Int = 0,
    val totalRevenueGenerated: Double = 0.0,
    val description: String = "",
    val minCoversForPrivateRoom: Int = 8,
    val maxRoomCapacity: Int = 18,
    val contactName: String = "",
    val isPartnerVerified: Boolean = true,
    val offPeakSlots: List<String> = listOf("Tuesday Dinner", "Thursday Supper", "Sunday Twilight")
)

data class Host(
    val id: Long = 0,
    val name: String,
    val handle: String,
    val niche: String,
    val followerCount: String,
    val bio: String,
    val rating: Float = 4.9f,
    val dinnersHostedCount: Int = 0,
    val totalEarnings: Double = 0.0,
    val signatureDishStyle: String = "Tasting Menus & Natural Wine",
    val commissionPercent: Int = 20
)

data class Booking(
    val id: Long = 0,
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
    val dietaryRestrictions: String = "None",
    val seatingVibe: String = "Conversationalist",
    val qrTicketCode: String = "TAVOLA-VIP-7492",
    val bookingTimestamp: Long = System.currentTimeMillis(),
    val isCheckedIn: Boolean = false
)

data class CollaborationProposal(
    val id: Long = 0,
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
    val status: ProposalStatus = ProposalStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)
