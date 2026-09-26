package com.example.data.firestore

import android.content.Context
import android.util.Log
import com.example.data.model.DinnerEvent
import com.example.data.model.Restaurant
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreManager(private val context: Context) {

    private val tag = "FirestoreManager"
    private var firestore: FirebaseFirestore? = null

    init {
        try {
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                firestore = FirebaseFirestore.getInstance()
                Log.d(tag, "Firestore initialized successfully.")
            } else {
                Log.d(tag, "FirebaseApp not initialized; Firestore operations deferred.")
            }
        } catch (e: Exception) {
            Log.w(tag, "Firestore init deferred: ${e.message}")
        }
    }

    suspend fun saveRestaurant(restaurant: Restaurant): Boolean {
        return try {
            val db = firestore ?: return false
            val data = hashMapOf(
                "id" to restaurant.id,
                "name" to restaurant.name,
                "cuisine" to restaurant.cuisine,
                "address" to restaurant.address,
                "neighborhood" to restaurant.neighborhood,
                "rating" to restaurant.rating,
                "reviewCount" to restaurant.reviewCount,
                "coversGuaranteedTotal" to restaurant.coversGuaranteedTotal,
                "totalRevenueGenerated" to restaurant.totalRevenueGenerated,
                "description" to restaurant.description,
                "minCoversForPrivateRoom" to restaurant.minCoversForPrivateRoom,
                "maxRoomCapacity" to restaurant.maxRoomCapacity,
                "contactName" to restaurant.contactName,
                "isPartnerVerified" to restaurant.isPartnerVerified
            )
            db.collection("restaurants")
                .document(restaurant.id.toString())
                .set(data)
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error saving restaurant to Firestore: ${e.message}")
            false
        }
    }

    suspend fun saveDinnerEvent(event: DinnerEvent): Boolean {
        return try {
            val db = firestore ?: return false
            val data = hashMapOf(
                "id" to event.id,
                "title" to event.title,
                "description" to event.description,
                "hostId" to event.hostId,
                "hostName" to event.hostName,
                "hostHandle" to event.hostHandle,
                "hostNiche" to event.hostNiche,
                "hostFollowers" to event.hostFollowers,
                "venueId" to event.venueId,
                "venueName" to event.venueName,
                "venueAddress" to event.venueAddress,
                "venueNeighborhood" to event.venueNeighborhood,
                "dateString" to event.dateString,
                "timeString" to event.timeString,
                "pricePerSeat" to event.pricePerSeat,
                "totalSeats" to event.totalSeats,
                "bookedSeats" to event.bookedSeats,
                "minCoversRequired" to event.minCoversRequired,
                "status" to event.status.name,
                "dressCode" to event.dressCode,
                "hostCutPercent" to event.hostCutPercent
            )
            db.collection("dinner_events")
                .document(event.id.toString())
                .set(data)
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error saving dining event to Firestore: ${e.message}")
            false
        }
    }

    suspend fun updateBookedSeats(eventId: Long, newBooked: Int, newStatus: String): Boolean {
        return try {
            val db = firestore ?: return false
            db.collection("dinner_events")
                .document(eventId.toString())
                .update(
                    mapOf(
                        "bookedSeats" to newBooked,
                        "status" to newStatus
                    )
                )
                .await()
            true
        } catch (e: Exception) {
            Log.e(tag, "Error updating booked seats in Firestore: ${e.message}")
            false
        }
    }
}
