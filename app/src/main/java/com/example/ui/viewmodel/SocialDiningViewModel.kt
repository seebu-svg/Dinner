package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthManager
import com.example.data.auth.AuthUserState
import com.example.data.model.Booking
import com.example.data.model.CollaborationProposal
import com.example.data.model.DinnerEvent
import com.example.data.model.EventStatus
import com.example.data.model.Host
import com.example.data.model.MarketplaceRole
import com.example.data.model.MenuCourse
import com.example.data.model.ProposalStatus
import com.example.data.model.Restaurant
import com.example.data.repository.SocialDiningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MarketplaceUiState(
    val currentRole: MarketplaceRole = MarketplaceRole.DINER,
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val selectedDinner: DinnerEvent? = null,
    val bookingTargetDinner: DinnerEvent? = null,
    val activePassBooking: Booking? = null,
    val isPitchDialogOpen: Boolean = false,
    val reviewingProposal: CollaborationProposal? = null,
    val activeIcebreakerDinner: DinnerEvent? = null,
    val isAuthDialogOpen: Boolean = false,
    val activeHostId: Long = 1L, // Elena Rostova
    val activeRestaurantId: Long = 1L, // Osteria Del Fico
    val snackbarMessage: String? = null
)

class SocialDiningViewModel(
    private val repository: SocialDiningRepository,
    private val authManager: AuthManager? = null
) : ViewModel() {

    val authState: StateFlow<AuthUserState> = authManager?.userState
        ?: MutableStateFlow(AuthUserState()).asStateFlow()

    private val _uiState = MutableStateFlow(MarketplaceUiState())
    val uiState: StateFlow<MarketplaceUiState> = _uiState.asStateFlow()

    val dinnerEvents: StateFlow<List<DinnerEvent>> = repository.dinnerEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val restaurants: StateFlow<List<Restaurant>> = repository.restaurants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hosts: StateFlow<List<Host>> = repository.hosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookings: StateFlow<List<Booking>> = repository.bookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val proposals: StateFlow<List<CollaborationProposal>> = repository.proposals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered dinners for Diners
    val filteredDinners: StateFlow<List<DinnerEvent>> = combine(
        dinnerEvents,
        _uiState
    ) { events, state ->
        events.filter { event ->
            val matchesQuery = state.searchQuery.isBlank() ||
                event.title.contains(state.searchQuery, ignoreCase = true) ||
                event.hostName.contains(state.searchQuery, ignoreCase = true) ||
                event.venueName.contains(state.searchQuery, ignoreCase = true) ||
                event.venueNeighborhood.contains(state.searchQuery, ignoreCase = true) ||
                event.vibeTags.any { it.contains(state.searchQuery, ignoreCase = true) }

            val matchesFilter = when (state.selectedFilter) {
                "All" -> true
                "Confirmed Tables" -> event.isConfirmed
                "Wine & Tasting" -> event.vibeTags.any { it.contains("Wine", true) } || event.description.contains("wine", true)
                "Intimate (<12)" -> event.totalSeats <= 12
                "Pending Covers" -> event.status == EventStatus.PENDING_COVERS
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun switchRole(role: MarketplaceRole) {
        _uiState.value = _uiState.value.copy(currentRole = role)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun selectFilter(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun openDinnerDetail(dinner: DinnerEvent) {
        _uiState.value = _uiState.value.copy(selectedDinner = dinner)
    }

    fun closeDinnerDetail() {
        _uiState.value = _uiState.value.copy(selectedDinner = null)
    }

    fun openBookingDialog(dinner: DinnerEvent) {
        _uiState.value = _uiState.value.copy(
            selectedDinner = null,
            bookingTargetDinner = dinner
        )
    }

    fun closeBookingDialog() {
        _uiState.value = _uiState.value.copy(bookingTargetDinner = null)
    }

    fun openPass(booking: Booking) {
        _uiState.value = _uiState.value.copy(activePassBooking = booking)
    }

    fun closePass() {
        _uiState.value = _uiState.value.copy(activePassBooking = null)
    }

    fun openPitchDialog() {
        _uiState.value = _uiState.value.copy(isPitchDialogOpen = true)
    }

    fun closePitchDialog() {
        _uiState.value = _uiState.value.copy(isPitchDialogOpen = false)
    }

    fun openProposalReview(proposal: CollaborationProposal) {
        _uiState.value = _uiState.value.copy(reviewingProposal = proposal)
    }

    fun closeProposalReview() {
        _uiState.value = _uiState.value.copy(reviewingProposal = null)
    }

    fun openIcebreakers(dinner: DinnerEvent) {
        _uiState.value = _uiState.value.copy(activeIcebreakerDinner = dinner)
    }

    fun closeIcebreakers() {
        _uiState.value = _uiState.value.copy(activeIcebreakerDinner = null)
    }

    fun setActiveHost(hostId: Long) {
        _uiState.value = _uiState.value.copy(activeHostId = hostId)
    }

    fun setActiveRestaurant(restaurantId: Long) {
        _uiState.value = _uiState.value.copy(activeRestaurantId = restaurantId)
    }

    fun openAuthDialog() {
        _uiState.value = _uiState.value.copy(isAuthDialogOpen = true)
    }

    fun closeAuthDialog() {
        _uiState.value = _uiState.value.copy(isAuthDialogOpen = false)
    }

    fun signInUser(email: String, name: String, role: String) {
        authManager?.signInWithCustomSession(email, name, role)
        _uiState.value = _uiState.value.copy(
            isAuthDialogOpen = false,
            snackbarMessage = "👋 Welcome $name! Signed in as $role."
        )
    }

    fun signOutUser() {
        authManager?.signOut()
        _uiState.value = _uiState.value.copy(
            isAuthDialogOpen = false,
            snackbarMessage = "Signed out."
        )
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    fun reserveSeats(
        eventId: Long,
        seatsCount: Int,
        guestName: String,
        guestEmail: String,
        dietary: String,
        vibe: String
    ) {
        viewModelScope.launch {
            val result = repository.bookSeats(
                eventId = eventId,
                seatsCount = seatsCount,
                guestName = guestName,
                guestEmail = guestEmail,
                dietaryRestrictions = dietary,
                seatingVibe = vibe
            )
            result.onSuccess { booking ->
                _uiState.value = _uiState.value.copy(
                    bookingTargetDinner = null,
                    activePassBooking = booking,
                    snackbarMessage = "🎉 Reservation confirmed! $seatsCount seat(s) reserved at ${booking.venueName}."
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    snackbarMessage = "Booking failed: ${err.message}"
                )
            }
        }
    }

    fun submitHostPitch(
        restaurantId: Long,
        eventTitle: String,
        proposedDate: String,
        proposedTime: String,
        targetCovers: Int,
        minGuaranteeCovers: Int,
        pricePerSeat: Double,
        pitchNote: String,
        menuVision: String
    ) {
        viewModelScope.launch {
            val currentHost = hosts.value.find { it.id == _uiState.value.activeHostId }
                ?: hosts.value.firstOrNull()
            val restaurant = restaurants.value.find { it.id == restaurantId }

            if (currentHost != null && restaurant != null) {
                val proposal = CollaborationProposal(
                    hostId = currentHost.id,
                    hostName = currentHost.name,
                    hostHandle = currentHost.handle,
                    hostFollowers = currentHost.followerCount,
                    hostNiche = currentHost.niche,
                    restaurantId = restaurant.id,
                    restaurantName = restaurant.name,
                    eventTitle = eventTitle,
                    proposedDate = proposedDate,
                    proposedTime = proposedTime,
                    targetCovers = targetCovers,
                    minGuaranteeCovers = minGuaranteeCovers,
                    pricePerSeat = pricePerSeat,
                    pitchNote = pitchNote,
                    menuVision = menuVision,
                    status = ProposalStatus.PENDING
                )
                repository.createHostPitch(proposal)
                _uiState.value = _uiState.value.copy(
                    isPitchDialogOpen = false,
                    snackbarMessage = "✉️ Pitch sent to ${restaurant.name}! They will review the cover guarantee."
                )
            }
        }
    }

    fun acceptProposal(proposalId: Long) {
        viewModelScope.launch {
            val result = repository.acceptProposal(proposalId)
            result.onSuccess { createdEvent ->
                _uiState.value = _uiState.value.copy(
                    reviewingProposal = null,
                    snackbarMessage = "✅ Proposal accepted! '${createdEvent.title}' is now live on the marketplace."
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    snackbarMessage = "Error: ${err.message}"
                )
            }
        }
    }

    fun declineProposal(proposalId: Long) {
        viewModelScope.launch {
            repository.declineProposal(proposalId)
            _uiState.value = _uiState.value.copy(
                reviewingProposal = null,
                snackbarMessage = "Proposal declined."
            )
        }
    }

    fun toggleCheckIn(booking: Booking) {
        viewModelScope.launch {
            val newStatus = !booking.isCheckedIn
            repository.setCheckIn(booking.id, newStatus)
            _uiState.value = _uiState.value.copy(
                snackbarMessage = if (newStatus) "✓ ${booking.guestName} checked in!" else "Check-in undone."
            )
        }
    }
}
