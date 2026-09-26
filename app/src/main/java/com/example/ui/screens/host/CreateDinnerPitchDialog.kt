package com.example.ui.screens.host

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.Restaurant
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.OliveSage
import com.example.ui.theme.SuccessGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateDinnerPitchDialog(
    restaurants: List<Restaurant>,
    onDismiss: () -> Unit,
    onSubmitPitch: (
        restaurantId: Long,
        title: String,
        date: String,
        time: String,
        targetCovers: Int,
        minGuaranteeCovers: Int,
        pricePerSeat: Double,
        pitchNote: String,
        menuVision: String
    ) -> Unit
) {
    var selectedRestaurantId by remember { mutableLongStateOf(restaurants.firstOrNull()?.id ?: 1L) }
    var eventTitle by remember { mutableStateOf("Tuscan Harvest & Orange Wine Salon") }
    var proposedDate by remember { mutableStateOf("Tuesday, Nov 26") }
    var proposedTime by remember { mutableStateOf("7:30 PM - 10:30 PM") }
    var targetCovers by remember { mutableIntStateOf(12) }
    var minGuaranteeCovers by remember { mutableIntStateOf(8) }
    var pricePerSeat by remember { mutableDoubleStateOf(120.0) }
    var menuVision by remember { mutableStateOf("4-course wood-fired seafood paired with biodynamic skin-contact wines.") }
    var pitchNote by remember { mutableStateOf("I will bring 12 dedicated wine lovers on a slow Tuesday night, guaranteeing 8 covers minimum for your private dining room.") }

    val selectedRestaurant = restaurants.find { it.id == selectedRestaurantId }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .testTag("create_pitch_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pitch Dinner to Restaurant",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Offer guaranteed covers on off-peak nights",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Select Partner Venue
                Text(
                    text = "Select Partner Restaurant",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    restaurants.forEach { r ->
                        val isSelected = r.id == selectedRestaurantId
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedRestaurantId = r.id },
                            label = { Text(r.name, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberGoldPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Event Title
                OutlinedTextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Curated Event Title") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pitch_title_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Date & Time
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = proposedDate,
                        onValueChange = { proposedDate = it },
                        label = { Text("Date (e.g. Tuesday)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = proposedTime,
                        onValueChange = { proposedTime = it },
                        label = { Text("Time") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Cover Guarantee Configuration
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Cover Guarantee Model",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SuccessGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = minGuaranteeCovers.toString(),
                                onValueChange = { minGuaranteeCovers = it.toIntOrNull() ?: minGuaranteeCovers },
                                label = { Text("Min Guarantee") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = targetCovers.toString(),
                                onValueChange = { targetCovers = it.toIntOrNull() ?: targetCovers },
                                label = { Text("Max Seats") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = pricePerSeat.toInt().toString(),
                                onValueChange = { pricePerSeat = it.toDoubleOrNull() ?: pricePerSeat },
                                label = { Text("Seat $") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val estVenueRevenue = targetCovers * pricePerSeat * 0.8
                        val estHostCut = targetCovers * pricePerSeat * 0.2
                        Text(
                            text = "Est. Venue Revenue: $${estVenueRevenue.toInt()} (80%) • Host Cut: $${estHostCut.toInt()} (20%)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Menu Vision & Pitch
                OutlinedTextField(
                    value = menuVision,
                    onValueChange = { menuVision = it },
                    label = { Text("Menu & Beverage Vision") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = pitchNote,
                    onValueChange = { pitchNote = it },
                    label = { Text("Pitch Note to Venue Manager") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSubmitPitch(
                            selectedRestaurantId,
                            eventTitle,
                            proposedDate,
                            proposedTime,
                            targetCovers,
                            minGuaranteeCovers,
                            pricePerSeat,
                            pitchNote,
                            menuVision
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_pitch_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Text("Send Pitch to Restaurant", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
