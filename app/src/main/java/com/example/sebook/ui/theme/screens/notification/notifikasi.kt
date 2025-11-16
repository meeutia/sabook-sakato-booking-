package com.example.sebook.ui.theme.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.ui.theme.components.CustomButton

data class Notification(
    val id: Int,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val roomName: String? = null
)

enum class NotificationType {
    APPROVED,
    REJECTED,
    REVIEW_REMINDER,
    PENDING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    val notifications = remember {
        mutableStateListOf(
            Notification(
                id = 1,
                title = "Pengajuan Disetujui",
                message = "Pengajuan ruangan Aula Utama untuk tanggal 25 Desember 2024 telah disetujui. Silakan datang sesuai jadwal yang telah ditentukan.",
                time = "2 jam yang lalu",
                type = NotificationType.APPROVED,
                isRead = false,
                roomName = "Aula Utama"
            ),
            Notification(
                id = 2,
                title = "Berikan Review Anda",
                message = "Terima kasih telah menggunakan Ruang Meeting A. Bagikan pengalaman Anda dengan memberikan review.",
                time = "5 jam yang lalu",
                type = NotificationType.REVIEW_REMINDER,
                isRead = false,
                roomName = "Ruang Meeting A"
            ),
            Notification(
                id = 3,
                title = "Pengajuan Disetujui ✓",
                message = "Pengajuan ruangan Lab Komputer untuk tanggal 20 Desember 2024 telah disetujui.",
                time = "1 hari yang lalu",
                type = NotificationType.APPROVED,
                isRead = true,
                roomName = "Lab Komputer"
            ),
            Notification(
                id = 4,
                title = "Pengajuan Ditolak",
                message = "Maaf, pengajuan ruangan Ruang Seminar untuk tanggal 18 Desember 2024 ditolak. Ruangan sudah dibooking oleh pengguna lain.",
                time = "2 hari yang lalu",
                type = NotificationType.REJECTED,
                isRead = true,
                roomName = "Ruang Seminar"
            ),
            Notification(
                id = 5,
                title = "Berikan Review Anda",
                message = "Bagaimana pengalaman Anda menggunakan Ruang Workshop? Kami tunggu review dari Anda.",
                time = "3 hari yang lalu",
                type = NotificationType.REVIEW_REMINDER,
                isRead = true,
                roomName = "Ruang Workshop"
            ),
            Notification(
                id = 6,
                title = "Pengajuan Sedang Diproses",
                message = "Pengajuan ruangan Auditorium untuk tanggal 30 Desember 2024 sedang dalam proses review admin.",
                time = "4 hari yang lalu",
                type = NotificationType.PENDING,
                isRead = true,
                roomName = "Auditorium"
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                windowInsets = WindowInsets(top = 24.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F8F8))
                    .padding(paddingValues)
            ) {
                if (notifications.isEmpty()) {
                    // Empty State
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "No notifications",
                                modifier = Modifier.size(80.dp),
                                tint = Color.LightGray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Belum ada notifikasi",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Notifikasi pengajuan ruangan akan muncul di sini",
                                fontSize = 14.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(notifications) { notification ->
                            NotificationItem(
                                notification = notification,
                                onClick = {
                                    // Mark as read when clicked
                                    val index = notifications.indexOf(notification)
                                    notifications[index] = notification.copy(isRead = true)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val backgroundColor = if (notification.isRead) Color.White else Color(0xFFFFF8F0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Icon based on type
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(getNotificationColor(notification.type).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getNotificationIcon(notification.type),
                contentDescription = null,
                tint = getNotificationColor(notification.type),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                // Unread indicator
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF96300))
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = notification.message,
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notification.time,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )

                // Action button for review reminder
                if (notification.type == NotificationType.REVIEW_REMINDER && !notification.isRead) {
                    // Tombol Ajukan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CustomButton(
                            text = "Tulis Review",
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .fillMaxWidth(0.4f),
                            fontSize = 10.sp,
                            onClick = { /* Navigate to review screen */ }

                        )
                    }
                }
            }
        }
    }

    Divider(
        color = Color(0xFFE0E0E0),
        thickness = 1.dp
    )
}

fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.APPROVED -> Icons.Filled.CheckCircle
        NotificationType.REJECTED -> Icons.Filled.Cancel
        NotificationType.REVIEW_REMINDER -> Icons.Filled.Star
        NotificationType.PENDING -> Icons.Filled.Schedule
    }
}

fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.APPROVED -> Color(0xFF4CAF50)
        NotificationType.REJECTED -> Color(0xFFF44336)
        NotificationType.REVIEW_REMINDER -> Color(0xFFF96300)
        NotificationType.PENDING -> Color(0xFFFF9800)
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(navController = rememberNavController())
}