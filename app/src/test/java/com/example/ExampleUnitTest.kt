package com.example

import com.example.data.model.DinnerEvent
import com.example.data.model.EventStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testCoverGuaranteeThreshold() {
        val pendingDinner = DinnerEvent(
            id = 1,
            title = "Test Tasting",
            description = "Test Description",
            hostId = 1,
            hostName = "Host",
            hostHandle = "@host",
            hostNiche = "Wine",
            hostFollowers = "10k",
            venueId = 1,
            venueName = "Venue",
            venueAddress = "Address",
            venueNeighborhood = "Soho",
            dateString = "Tomorrow",
            timeString = "7 PM",
            pricePerSeat = 100.0,
            totalSeats = 12,
            bookedSeats = 6,
            minCoversRequired = 8,
            status = EventStatus.PENDING_COVERS
        )

        assertFalse("Should not be confirmed when bookedSeats < minCovers", pendingDinner.isConfirmed)
        assertEquals(2, pendingDinner.seatsToConfirm)
        assertEquals(6, pendingDinner.remainingSeats)

        val confirmedDinner = pendingDinner.copy(bookedSeats = 8)
        assertTrue("Should be confirmed when bookedSeats >= minCovers", confirmedDinner.isConfirmed)
        assertEquals(0, confirmedDinner.seatsToConfirm)
        assertEquals(4, confirmedDinner.remainingSeats)
    }
}
