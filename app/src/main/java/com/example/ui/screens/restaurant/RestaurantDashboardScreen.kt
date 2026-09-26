package com.example.ui.screens.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CollaborationProposal
import com.example.data.model.DinnerEvent
import com.example.data.model.ProposalStatus
import com.example.data.model.Restaurant
import com.example.ui.components.CoverGuaranteeIndicator
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.OliveSage
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaAccent

@Composable
fun RestaurantDashboardScreen(
    restaurants: List<Restaurant>,
    activeRestaurantId: Long,
    dinners: List<DinnerEvent>,
    proposals: List<CollaborationProposal>,
    onSelectRestaurant: (Long) -> Unit,
    onReviewProposal: (CollaborationProposal) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentVenue = restaurants.find { it.id == activeRestaurantId } ?: restaurants.firstOrNull()
    val venueDinners = dinners.filter { it.venueId == (currentVenue?.id ?: 1L) }
    val incomingProposals = proposals.filter { it.restaurantId == (currentVenue?.id ?: 1L) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("restaurant_dashboard_screen"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 96.dp)
    ) {
        // Venue Selector Bar
        item {
            Text(
                text = "Viewing as Partner Restaurant / Venue:",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(restaurants) { r ->
                    val isSelected = r.id == activeRestaurantId
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { onSelectRestaurant(r.id) }
                            .testTag("select_venue_${r.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else AmberGoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = r.name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Venue Overview Card & Cover Guarantee Impact
        item {
            if (currentVenue != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = currentVenue.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${currentVenue.cuisine} • ${currentVenue.neighborhood}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SuccessGreen.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Verified Partner", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Key Hospitality Metrics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            VenueMetric(
                                label = "Guaranteed Covers",
                                value = "${currentVenue.coversGuaranteedTotal}",
                                icon = Icons.Default.Shield,
                                tint = SuccessGreen
                            )
                            VenueMetric(
                                label = "Off-Peak Revenue",
                                value = "$${currentVenue.totalRevenueGenerated.toInt()}",
                                icon = Icons.Default.MonetizationOn,
                                tint = AmberGoldPrimary
                            )
                            VenueMetric(
                                label = "Private Capacity",
                                value = "${currentVenue.maxRoomCapacity} seats",
                                icon = Icons.Default.People,
                                tint = TerracottaAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Off-peak Slow Day Utilization
                        Text(
                            text = "📅 Protected Off-Peak Slots: ${currentVenue.offPeakSlots.joinToString(" • ")}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Incoming Host Pitch Collaboration Requests
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "📩 Incoming Host Pitches (${incomingProposals.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Influencers proposing events with guaranteed cover commitments",
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (incomingProposals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No pending pitches for ${currentVenue?.name ?: "this venue"}.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(incomingProposals) { prop ->
                val isPending = prop.status == ProposalStatus.PENDING

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = prop.eventTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                                Text(
                                    text = "Pitched by ${prop.hostName} (${prop.hostHandle}) • ${prop.hostFollowers} reach",
                                    fontSize = 12.sp,
                                    color = AmberGoldPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Date: ${prop.proposedDate} • ${prop.proposedTime}",
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (prop.status == ProposalStatus.ACCEPTED) SuccessGreen.copy(alpha = 0.15f) else PendingAmber.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = prop.status.name,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (prop.status == ProposalStatus.ACCEPTED) SuccessGreen else PendingAmber,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Guarantee Highlights
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Minimum Covers Guaranteed:",
                                        fontSize = 11.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${prop.minGuaranteeCovers} covers ($${(prop.minGuaranteeCovers * prop.pricePerSeat * 0.8).toInt()} min venue rev)",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessGreen
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Target Total Seats:",
                                        fontSize = 11.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${prop.targetCovers} seats @ $${prop.pricePerSeat.toInt()}/seat",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "\"${prop.pitchNote}\"",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isPending) {
                            Button(
                                onClick = { onReviewProposal(prop) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("review_proposal_${prop.id}"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                            ) {
                                Text("Review & Approve Pitch", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Section: Live & Upcoming Dinners at this Venue
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "🏛️ Live Dinners Hosted at Venue (${venueDinners.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Real-time covers tracking toward your kitchen prep threshold",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        if (venueDinners.isEmpty()) {
            item {
                Text(
                    text = "No live dinners currently running at ${currentVenue?.name ?: "this venue"}.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(venueDinners) { dinner ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = dinner.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                                Text(
                                    text = "Host: ${dinner.hostName} • Date: ${dinner.dateString}",
                                    fontSize = 12.sp,
                                    color = AmberGoldPrimary
                                )
                            }
                            Text(
                                text = "$${(dinner.bookedSeats * dinner.pricePerSeat * 0.8).toInt()} venue net",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        CoverGuaranteeIndicator(
                            booked = dinner.bookedSeats,
                            minRequired = dinner.minCoversRequired,
                            totalCapacity = dinner.totalSeats,
                            isConfirmed = dinner.isConfirmed
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VenueMetric(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
