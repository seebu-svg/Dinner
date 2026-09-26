package com.example.data.local

import com.example.data.model.Booking
import com.example.data.model.CollaborationProposal
import com.example.data.model.DinnerEvent
import com.example.data.model.EventStatus
import com.example.data.model.Host
import com.example.data.model.MenuCourse
import com.example.data.model.ProposalStatus
import com.example.data.model.Restaurant
import org.json.JSONArray
import org.json.JSONObject

object ModelMappers {

    fun menuCoursesToJson(courses: List<MenuCourse>): String {
        val array = JSONArray()
        courses.forEach { course ->
            val obj = JSONObject()
            obj.put("courseNumber", course.courseNumber)
            obj.put("title", course.title)
            obj.put("description", course.description)
            obj.put("winePairing", course.winePairing)
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToMenuCourses(json: String): List<MenuCourse> {
        if (json.isBlank()) return emptyList()
        val list = mutableListOf<MenuCourse>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    MenuCourse(
                        courseNumber = obj.optInt("courseNumber", i + 1),
                        title = obj.optString("title", ""),
                        description = obj.optString("description", ""),
                        winePairing = obj.optString("winePairing", "")
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }

    fun stringListToJson(list: List<String>): String {
        val array = JSONArray()
        list.forEach { array.put(it) }
        return array.toString()
    }

    fun jsonToStringList(json: String): List<String> {
        if (json.isBlank()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (_: Exception) {}
        return list
    }

    fun DinnerEventEntity.toDomain(): DinnerEvent {
        return DinnerEvent(
            id = id,
            title = title,
            description = description,
            hostId = hostId,
            hostName = hostName,
            hostHandle = hostHandle,
            hostNiche = hostNiche,
            hostFollowers = hostFollowers,
            hostRating = hostRating,
            venueId = venueId,
            venueName = venueName,
            venueAddress = venueAddress,
            venueNeighborhood = venueNeighborhood,
            dateString = dateString,
            timeString = timeString,
            pricePerSeat = pricePerSeat,
            totalSeats = totalSeats,
            bookedSeats = bookedSeats,
            minCoversRequired = minCoversRequired,
            status = try { EventStatus.valueOf(status) } catch (_: Exception) { EventStatus.PENDING_COVERS },
            menuCourses = jsonToMenuCourses(menuCoursesJson),
            vibeTags = jsonToStringList(vibeTagsJson),
            dressCode = dressCode,
            icebreakerPrompts = jsonToStringList(icebreakerPromptsJson),
            hostCutPercent = hostCutPercent,
            specialNotes = specialNotes
        )
    }

    fun DinnerEvent.toEntity(): DinnerEventEntity {
        return DinnerEventEntity(
            id = id,
            title = title,
            description = description,
            hostId = hostId,
            hostName = hostName,
            hostHandle = hostHandle,
            hostNiche = hostNiche,
            hostFollowers = hostFollowers,
            hostRating = hostRating,
            venueId = venueId,
            venueName = venueName,
            venueAddress = venueAddress,
            venueNeighborhood = venueNeighborhood,
            dateString = dateString,
            timeString = timeString,
            pricePerSeat = pricePerSeat,
            totalSeats = totalSeats,
            bookedSeats = bookedSeats,
            minCoversRequired = minCoversRequired,
            status = status.name,
            menuCoursesJson = menuCoursesToJson(menuCourses),
            vibeTagsJson = stringListToJson(vibeTags),
            dressCode = dressCode,
            icebreakerPromptsJson = stringListToJson(icebreakerPrompts),
            hostCutPercent = hostCutPercent,
            specialNotes = specialNotes
        )
    }

    fun RestaurantEntity.toDomain(): Restaurant {
        return Restaurant(
            id = id,
            name = name,
            cuisine = cuisine,
            address = address,
            neighborhood = neighborhood,
            rating = rating,
            reviewCount = reviewCount,
            coversGuaranteedTotal = coversGuaranteedTotal,
            totalRevenueGenerated = totalRevenueGenerated,
            description = description,
            minCoversForPrivateRoom = minCoversForPrivateRoom,
            maxRoomCapacity = maxRoomCapacity,
            contactName = contactName,
            isPartnerVerified = isPartnerVerified,
            offPeakSlots = jsonToStringList(offPeakSlotsJson)
        )
    }

    fun Restaurant.toEntity(): RestaurantEntity {
        return RestaurantEntity(
            id = id,
            name = name,
            cuisine = cuisine,
            address = address,
            neighborhood = neighborhood,
            rating = rating,
            reviewCount = reviewCount,
            coversGuaranteedTotal = coversGuaranteedTotal,
            totalRevenueGenerated = totalRevenueGenerated,
            description = description,
            minCoversForPrivateRoom = minCoversForPrivateRoom,
            maxRoomCapacity = maxRoomCapacity,
            contactName = contactName,
            isPartnerVerified = isPartnerVerified,
            offPeakSlotsJson = stringListToJson(offPeakSlots)
        )
    }

    fun HostEntity.toDomain(): Host {
        return Host(
            id = id,
            name = name,
            handle = handle,
            niche = niche,
            followerCount = followerCount,
            bio = bio,
            rating = rating,
            dinnersHostedCount = dinnersHostedCount,
            totalEarnings = totalEarnings,
            signatureDishStyle = signatureDishStyle,
            commissionPercent = commissionPercent
        )
    }

    fun Host.toEntity(): HostEntity {
        return HostEntity(
            id = id,
            name = name,
            handle = handle,
            niche = niche,
            followerCount = followerCount,
            bio = bio,
            rating = rating,
            dinnersHostedCount = dinnersHostedCount,
            totalEarnings = totalEarnings,
            signatureDishStyle = signatureDishStyle,
            commissionPercent = commissionPercent
        )
    }

    fun BookingEntity.toDomain(): Booking {
        return Booking(
            id = id,
            dinnerEventId = dinnerEventId,
            dinnerTitle = dinnerTitle,
            venueName = venueName,
            dateString = dateString,
            timeString = timeString,
            guestName = guestName,
            guestEmail = guestEmail,
            seatsCount = seatsCount,
            pricePerSeat = pricePerSeat,
            totalPrice = totalPrice,
            dietaryRestrictions = dietaryRestrictions,
            seatingVibe = seatingVibe,
            qrTicketCode = qrTicketCode,
            bookingTimestamp = bookingTimestamp,
            isCheckedIn = isCheckedIn
        )
    }

    fun Booking.toEntity(): BookingEntity {
        return BookingEntity(
            id = id,
            dinnerEventId = dinnerEventId,
            dinnerTitle = dinnerTitle,
            venueName = venueName,
            dateString = dateString,
            timeString = timeString,
            guestName = guestName,
            guestEmail = guestEmail,
            seatsCount = seatsCount,
            pricePerSeat = pricePerSeat,
            totalPrice = totalPrice,
            dietaryRestrictions = dietaryRestrictions,
            seatingVibe = seatingVibe,
            qrTicketCode = qrTicketCode,
            bookingTimestamp = bookingTimestamp,
            isCheckedIn = isCheckedIn
        )
    }

    fun CollaborationProposalEntity.toDomain(): CollaborationProposal {
        return CollaborationProposal(
            id = id,
            hostId = hostId,
            hostName = hostName,
            hostHandle = hostHandle,
            hostFollowers = hostFollowers,
            hostNiche = hostNiche,
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            eventTitle = eventTitle,
            proposedDate = proposedDate,
            proposedTime = proposedTime,
            targetCovers = targetCovers,
            minGuaranteeCovers = minGuaranteeCovers,
            pricePerSeat = pricePerSeat,
            pitchNote = pitchNote,
            menuVision = menuVision,
            status = try { ProposalStatus.valueOf(status) } catch (_: Exception) { ProposalStatus.PENDING },
            timestamp = timestamp
        )
    }

    fun CollaborationProposal.toEntity(): CollaborationProposalEntity {
        return CollaborationProposalEntity(
            id = id,
            hostId = hostId,
            hostName = hostName,
            hostHandle = hostHandle,
            hostFollowers = hostFollowers,
            hostNiche = hostNiche,
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            eventTitle = eventTitle,
            proposedDate = proposedDate,
            proposedTime = proposedTime,
            targetCovers = targetCovers,
            minGuaranteeCovers = minGuaranteeCovers,
            pricePerSeat = pricePerSeat,
            pitchNote = pitchNote,
            menuVision = menuVision,
            status = status.name,
            timestamp = timestamp
        )
    }
}
