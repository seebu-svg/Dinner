package com.example.ui.screens.diner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Booking
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.DarkEspressoBg
import com.example.ui.theme.OliveSage
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TerracottaAccent

@Composable
fun BookingPassDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onOpenIcebreakers: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .testTag("booking_pass_dialog"),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TerracottaAccent.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "OFFICIAL DINING PASS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // The Ticket Pass Container
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = booking.dinnerTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = AmberGoldPrimary,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = booking.venueName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "${booking.dateString} • ${booking.timeString}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // QR Code Representation
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SimulatedQrCanvas(modifier = Modifier.fillMaxSize())
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = booking.qrTicketCode,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Guest and Booking Details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "GUEST", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = booking.guestName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "SEATS", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = "${booking.seatsCount} Reserved",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGoldPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "DIETARY", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = booking.dietaryRestrictions,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "SEATING VIBE", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    text = booking.seatingVibe,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OliveSage
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Check-in status badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (booking.isCheckedIn) SuccessGreen.copy(alpha = 0.15f) else AmberGoldPrimary.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (booking.isCheckedIn) SuccessGreen else AmberGoldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (booking.isCheckedIn) "CHECKED IN AT DOOR" else "PRESENT AT RESTAURANT ENTRANCE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (booking.isCheckedIn) SuccessGreen else AmberGoldPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Icebreaker conversation button
                Button(
                    onClick = onOpenIcebreakers,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_table_icebreakers_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberGoldPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open Table Icebreakers", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Present this digital pass upon arrival at the restaurant. Host & venue will welcome you directly to your table.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun SimulatedQrCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val step = w / 9f

        // Draw corner position detection markers
        // Top-left
        drawRect(Color.Black, Offset(0f, 0f), Size(step * 3, step * 3))
        drawRect(Color.White, Offset(step * 0.5f, step * 0.5f), Size(step * 2, step * 2))
        drawRect(Color.Black, Offset(step, step), Size(step, step))

        // Top-right
        drawRect(Color.Black, Offset(w - step * 3, 0f), Size(step * 3, step * 3))
        drawRect(Color.White, Offset(w - step * 2.5f, step * 0.5f), Size(step * 2, step * 2))
        drawRect(Color.Black, Offset(w - step * 2, step), Size(step, step))

        // Bottom-left
        drawRect(Color.Black, Offset(0f, h - step * 3), Size(step * 3, step * 3))
        drawRect(Color.White, Offset(step * 0.5f, h - step * 2.5f), Size(step * 2, step * 2))
        drawRect(Color.Black, Offset(step, h - step * 2), Size(step, step))

        // Random matrix dots
        val pattern = listOf(
            Offset(step * 4, step * 1),
            Offset(step * 5, step * 2),
            Offset(step * 4, step * 3),
            Offset(step * 1, step * 4),
            Offset(step * 3, step * 4),
            Offset(step * 5, step * 4),
            Offset(step * 7, step * 4),
            Offset(step * 2, step * 5),
            Offset(step * 4, step * 5),
            Offset(step * 6, step * 5),
            Offset(step * 4, step * 7),
            Offset(step * 6, step * 7),
            Offset(step * 5, step * 8),
            Offset(step * 7, step * 8)
        )
        pattern.forEach { pt ->
            drawRect(Color.Black, pt, Size(step, step))
        }
    }
}
