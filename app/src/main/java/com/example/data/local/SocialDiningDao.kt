package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialDiningDao {

    // --- Dinner Events ---
    @Query("SELECT * FROM dinner_events ORDER BY id DESC")
    fun getAllDinnerEvents(): Flow<List<DinnerEventEntity>>

    @Query("SELECT * FROM dinner_events WHERE id = :id LIMIT 1")
    fun getDinnerEventById(id: Long): Flow<DinnerEventEntity?>

    @Query("SELECT * FROM dinner_events WHERE hostId = :hostId ORDER BY id DESC")
    fun getDinnerEventsByHost(hostId: Long): Flow<List<DinnerEventEntity>>

    @Query("SELECT * FROM dinner_events WHERE venueId = :venueId ORDER BY id DESC")
    fun getDinnerEventsByVenue(venueId: Long): Flow<List<DinnerEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDinnerEvent(event: DinnerEventEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDinnerEvents(events: List<DinnerEventEntity>)

    @Update
    suspend fun updateDinnerEvent(event: DinnerEventEntity)

    @Query("UPDATE dinner_events SET bookedSeats = bookedSeats + :seats WHERE id = :eventId")
    suspend fun incrementBookedSeats(eventId: Long, seats: Int)

    @Query("UPDATE dinner_events SET status = :status WHERE id = :eventId")
    suspend fun updateDinnerStatus(eventId: Long, status: String)

    // --- Restaurants ---
    @Query("SELECT * FROM restaurants ORDER BY rating DESC")
    fun getAllRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :id LIMIT 1")
    fun getRestaurantById(id: Long): Flow<RestaurantEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Query("UPDATE restaurants SET coversGuaranteedTotal = coversGuaranteedTotal + :covers, totalRevenueGenerated = totalRevenueGenerated + :revenue WHERE id = :id")
    suspend fun addGuaranteedCoversAndRevenue(id: Long, covers: Int, revenue: Double)

    // --- Hosts ---
    @Query("SELECT * FROM hosts ORDER BY dinnersHostedCount DESC")
    fun getAllHosts(): Flow<List<HostEntity>>

    @Query("SELECT * FROM hosts WHERE id = :id LIMIT 1")
    fun getHostById(id: Long): Flow<HostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHost(host: HostEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHosts(hosts: List<HostEntity>)

    @Update
    suspend fun updateHost(host: HostEntity)

    @Query("UPDATE hosts SET totalEarnings = totalEarnings + :amount, dinnersHostedCount = dinnersHostedCount + :dinnersCount WHERE id = :id")
    suspend fun addHostEarnings(id: Long, amount: Double, dinnersCount: Int = 0)

    // --- Bookings ---
    @Query("SELECT * FROM bookings ORDER BY bookingTimestamp DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE dinnerEventId = :eventId ORDER BY id ASC")
    fun getBookingsForEvent(eventId: Long): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET isCheckedIn = :checkedIn WHERE id = :id")
    suspend fun updateCheckInStatus(id: Long, checkedIn: Boolean)

    // --- Collaboration Proposals ---
    @Query("SELECT * FROM proposals ORDER BY timestamp DESC")
    fun getAllProposals(): Flow<List<CollaborationProposalEntity>>

    @Query("SELECT * FROM proposals WHERE restaurantId = :venueId ORDER BY timestamp DESC")
    fun getProposalsForRestaurant(venueId: Long): Flow<List<CollaborationProposalEntity>>

    @Query("SELECT * FROM proposals WHERE hostId = :hostId ORDER BY timestamp DESC")
    fun getProposalsForHost(hostId: Long): Flow<List<CollaborationProposalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProposal(proposal: CollaborationProposalEntity): Long

    @Query("UPDATE proposals SET status = :status WHERE id = :id")
    suspend fun updateProposalStatus(id: Long, status: String)
}
