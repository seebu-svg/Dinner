package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MarketplaceRole
import com.example.ui.components.AuthAccountDialog
import com.example.ui.components.MarketplaceRoleBar
import com.example.ui.screens.diner.BookingPassDialog
import com.example.ui.screens.diner.BookingSheetDialog
import com.example.ui.screens.diner.DinerDiscoverScreen
import com.example.ui.screens.diner.DinnerDetailDialog
import com.example.ui.screens.diner.TableIcebreakersDialog
import com.example.ui.screens.host.CreateDinnerPitchDialog
import com.example.ui.screens.host.HostDashboardScreen
import com.example.ui.screens.restaurant.ProposalReviewDialog
import com.example.ui.screens.restaurant.RestaurantDashboardScreen
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.TerracottaAccent
import com.example.ui.viewmodel.SocialDiningViewModel

@Composable
fun MainAppScreen(
    viewModel: SocialDiningViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dinners by viewModel.filteredDinners.collectAsStateWithLifecycle()
    val allDinners by viewModel.dinnerEvents.collectAsStateWithLifecycle()
    val hosts by viewModel.hosts.collectAsStateWithLifecycle()
    val restaurants by viewModel.restaurants.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val proposals by viewModel.proposals.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button when dialogs are open or to return to Diner mode
    BackHandler(enabled = uiState.selectedDinner != null ||
            uiState.bookingTargetDinner != null ||
            uiState.activePassBooking != null ||
            uiState.isPitchDialogOpen ||
            uiState.reviewingProposal != null ||
            uiState.activeIcebreakerDinner != null ||
            uiState.isAuthDialogOpen ||
            uiState.currentRole != MarketplaceRole.DINER) {
        when {
            uiState.isAuthDialogOpen -> viewModel.closeAuthDialog()
            uiState.activeIcebreakerDinner != null -> viewModel.closeIcebreakers()
            uiState.reviewingProposal != null -> viewModel.closeProposalReview()
            uiState.isPitchDialogOpen -> viewModel.closePitchDialog()
            uiState.activePassBooking != null -> viewModel.closePass()
            uiState.bookingTargetDinner != null -> viewModel.closeBookingDialog()
            uiState.selectedDinner != null -> viewModel.closeDinnerDetail()
            uiState.currentRole != MarketplaceRole.DINER -> viewModel.switchRole(MarketplaceRole.DINER)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = AmberGoldPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(data.visuals.message, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Main Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(AmberGoldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DinnerDining,
                                contentDescription = "SocialTable Logo",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SocialTable",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Social Dining Marketplace",
                                fontSize = 11.sp,
                                color = AmberGoldPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Active Mode Pill & Auth Button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = when (uiState.currentRole) {
                                    MarketplaceRole.DINER -> "Diner View"
                                    MarketplaceRole.HOST -> "Creator Portal"
                                    MarketplaceRole.RESTAURANT -> "Venue Portal"
                                },
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGoldPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = { viewModel.openAuthDialog() },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("account_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User Account",
                                tint = if (authState.isAuthenticated) AmberGoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Three-Sided Role Selector
                MarketplaceRoleBar(
                    selectedRole = uiState.currentRole,
                    onRoleSelected = { viewModel.switchRole(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentRole) {
                MarketplaceRole.DINER -> {
                    DinerDiscoverScreen(
                        dinners = dinners,
                        hosts = hosts,
                        restaurants = restaurants,
                        userBookings = bookings,
                        searchQuery = uiState.searchQuery,
                        selectedFilter = uiState.selectedFilter,
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        onFilterSelect = { viewModel.selectFilter(it) },
                        onDinnerClick = { viewModel.openDinnerDetail(it) },
                        onBookingPassClick = { viewModel.openPass(it) }
                    )
                }

                MarketplaceRole.HOST -> {
                    HostDashboardScreen(
                        hosts = hosts,
                        activeHostId = uiState.activeHostId,
                        dinners = allDinners,
                        proposals = proposals,
                        bookings = bookings,
                        onSelectHost = { viewModel.setActiveHost(it) },
                        onOpenPitchDialog = { viewModel.openPitchDialog() },
                        onToggleCheckIn = { viewModel.toggleCheckIn(it) }
                    )
                }

                MarketplaceRole.RESTAURANT -> {
                    RestaurantDashboardScreen(
                        restaurants = restaurants,
                        activeRestaurantId = uiState.activeRestaurantId,
                        dinners = allDinners,
                        proposals = proposals,
                        onSelectRestaurant = { viewModel.setActiveRestaurant(it) },
                        onReviewProposal = { viewModel.openProposalReview(it) }
                    )
                }
            }
        }

        // --- Dialogs & Sheets ---

        // 1. Dinner Detail Sheet
        uiState.selectedDinner?.let { dinner ->
            DinnerDetailDialog(
                dinner = dinner,
                onDismiss = { viewModel.closeDinnerDetail() },
                onBookClick = { viewModel.openBookingDialog(it) },
                onViewIcebreakers = { viewModel.openIcebreakers(it) }
            )
        }

        // 2. Booking Flow Dialog
        uiState.bookingTargetDinner?.let { targetDinner ->
            BookingSheetDialog(
                dinner = targetDinner,
                onDismiss = { viewModel.closeBookingDialog() },
                onConfirmBooking = { eventId, seats, name, email, dietary, vibe ->
                    viewModel.reserveSeats(eventId, seats, name, email, dietary, vibe)
                }
            )
        }

        // 3. Digital Pass with QR
        uiState.activePassBooking?.let { pass ->
            BookingPassDialog(
                booking = pass,
                onDismiss = { viewModel.closePass() },
                onOpenIcebreakers = {
                    val matchingDinner = allDinners.find { it.id == pass.dinnerEventId }
                        ?: allDinners.firstOrNull()
                    if (matchingDinner != null) {
                        viewModel.openIcebreakers(matchingDinner)
                    }
                }
            )
        }

        // 4. Host Pitch Creation
        if (uiState.isPitchDialogOpen) {
            CreateDinnerPitchDialog(
                restaurants = restaurants,
                onDismiss = { viewModel.closePitchDialog() },
                onSubmitPitch = { restId, title, date, time, target, minGuar, price, note, vision ->
                    viewModel.submitHostPitch(restId, title, date, time, target, minGuar, price, note, vision)
                }
            )
        }

        // 5. Restaurant Proposal Review
        uiState.reviewingProposal?.let { proposal ->
            ProposalReviewDialog(
                proposal = proposal,
                onDismiss = { viewModel.closeProposalReview() },
                onAccept = { viewModel.acceptProposal(it) },
                onDecline = { viewModel.declineProposal(it) }
            )
        }

        // 6. Active Table Conversation Icebreakers
        uiState.activeIcebreakerDinner?.let { icebreakerDinner ->
            TableIcebreakersDialog(
                dinner = icebreakerDinner,
                onDismiss = { viewModel.closeIcebreakers() }
            )
        }

        // 7. Authentication & Account Dialog
        if (uiState.isAuthDialogOpen) {
            AuthAccountDialog(
                userState = authState,
                currentRole = uiState.currentRole,
                onDismiss = { viewModel.closeAuthDialog() },
                onSignIn = { email, name, role ->
                    viewModel.signInUser(email, name, role)
                },
                onSignOut = { viewModel.signOutUser() }
            )
        }
    }
}
