package com.example.ui.screens.host

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Campaign
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.data.model.CollaborationProposal
import com.example.data.model.DinnerEvent
import com.example.data.model.Host
import com.example.data.model.ProposalStatus
import com.example.ui.components.CoverGuaranteeIndicator
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.OliveSage
import com.example.ui.theme.PendingAmber
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaAccent

@Composable
fun HostDashboardScreen(
    hosts: List<Host>,
    activeHostId: Long,
    dinners: List<DinnerEvent>,
    proposals: List<CollaborationProposal>,
    bookings: List<Booking>,
    onSelectHost: (Long) -> Unit,
    onOpenPitchDialog: () -> Unit,
    onToggleCheckIn: (Booking) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentHost = hosts.find { it.id == activeHostId } ?: hosts.firstOrNull()
    val hostDinners = dinners.filter { it.hostId == (currentHost?.id ?: 1L) }
    val hostProposals = proposals.filter { it.hostId == (currentHost?.id ?: 1L) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("host_dashboard_screen"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 96.dp)
    ) {
        // Creator Profile Switcher Bar
        item {
            Text(
                text = "Viewing as Creator / Host:",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(hosts) { h ->
                    val isSelected = h.id == activeHostId
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) AmberGoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { onSelectHost(h.id) }
                            .testTag("select_host_${h.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) Color.White else AmberGoldPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = h.name.take(1),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AmberGoldPrimary else Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = h.name,
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

        // Host Summary Card
        item {
            if (currentHost != null) {
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
                                    text = currentHost.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${currentHost.handle} • ${currentHost.followerCount} Reach",
                                    fontSize = 12.sp,
                                    color = AmberGoldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = currentHost.niche,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = onOpenPitchDialog,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary),
                                modifier = Modifier.testTag("pitch_restaurant_fab")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Pitch Dinner", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Key Metrics Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MetricItem(
                                title = "Host Earnings",
                                value = "$${currentHost.totalEarnings.toInt()}",
                                icon = Icons.Default.MonetizationOn,
                                tint = SuccessGreen
                            )
                            MetricItem(
                                title = "Dinners Hosted",
                                value = "${currentHost.dinnersHostedCount}",
                                icon = Icons.Default.People,
                                tint = AmberGoldPrimary
                            )
                            MetricItem(
                                title = "Host Rating",
                                value = "★ ${currentHost.rating}",
                                icon = Icons.Default.Star,
                                tint = TerracottaAccent
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section: Curated Dinners Managed by Host
        item {
            Text(
                text = "🍽️ Your Curated Dinners (${hostDinners.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Track cover guarantee progress, table reservations & door check-ins",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        if (hostDinners.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No active dinners yet. Pitch a dinner to a restaurant partner above!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(hostDinners) { dinner ->
                val dinnerBookings = bookings.filter { it.dinnerEventId == dinner.id }

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
                                    text = "Venue: ${dinner.venueName} • ${dinner.dateString}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = AmberGoldPrimary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "$${(dinner.bookedSeats * dinner.pricePerSeat * 0.2).toInt()} est. earnings",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGoldPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        CoverGuaranteeIndicator(
                            booked = dinner.bookedSeats,
                            minRequired = dinner.minCoversRequired,
                            totalCapacity = dinner.totalSeats,
                            isConfirmed = dinner.isConfirmed
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Guest check-in list preview
                        if (dinnerBookings.isNotEmpty()) {
                            Text(
                                text = "Guest List & Door Check-In (${dinnerBookings.size} RSVPs):",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            dinnerBookings.forEach { b ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = b.guestName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "${b.seatsCount} seat(s) • ${b.dietaryRestrictions}",
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = { onToggleCheckIn(b) },
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.testTag("check_in_guest_${b.id}"),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = if (b.isCheckedIn) SuccessGreen else MaterialTheme.colorScheme.outline
                                        )
                                    ) {
                                        Icon(
                                            imageVector = if (b.isCheckedIn) Icons.Default.CheckCircle else Icons.Default.HowToReg,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (b.isCheckedIn) "Checked In" else "Check In",
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Collaboration Pitches Sent to Venues
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "📩 Partnership Pitches Sent (${hostProposals.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Track venue approvals for your curated dining themes and guaranteed covers",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        if (hostProposals.isEmpty()) {
            item {
                Text(
                    text = "No proposals currently pending.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(hostProposals) { prop ->
                val statusColor = when (prop.status) {
                    ProposalStatus.ACCEPTED -> SuccessGreen
                    ProposalStatus.PENDING -> PendingAmber
                    ProposalStatus.DECLINED -> TerracottaAccent
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = prop.eventTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = statusColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = prop.status.name,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Restaurant: ${prop.restaurantName} • ${prop.proposedDate}",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Guarantee: ${prop.minGuaranteeCovers} min covers / ${prop.targetCovers} target @ $${prop.pricePerSeat.toInt()}/seat",
                            fontSize = 11.sp,
                            color = AmberGoldPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"${prop.pitchNote}\"",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
