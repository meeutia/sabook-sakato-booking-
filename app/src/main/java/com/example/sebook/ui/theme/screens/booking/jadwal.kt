package com.example.sebook.ui.theme.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.data.model.DetailBookingItem
import com.example.sebook.data.model.JadwalResponse
import com.example.sebook.data.model.JadwalResponseItem
import com.example.sebook.data.model.SlotTerisiItem
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.screens.informasi.RuanganViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.sebook.ui.theme.components.CustomButton

data class BookingInfo(val day: Int, val bookedBy: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    navController: NavController, 
    idRuangan: String,
    idPengajuan: String? // Add idPengajuan parameter
) {
    val context = LocalContext.current
    val viewModel: RuanganViewModel = viewModel(factory = ViewModelFactory(context))

    var currentCalendar by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf<Int?>(null) }
    var showTimeDialog by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf<String?>(null) }
    var endTime by remember { mutableStateOf<String?>(null) }

    val jadwalState by viewModel.jadwalState.collectAsState()
    val slotState by viewModel.slotState.collectAsState()

    LaunchedEffect(idRuangan, idPengajuan, currentCalendar) {
        viewModel.getJadwalRuangan(idRuangan, idPengajuan)
    }

    if (showTimeDialog && selectedDate != null) {
        val cal = currentCalendar.clone() as Calendar
        cal.set(Calendar.DAY_OF_MONTH, selectedDate!!)
        val tanggalSewa = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
        LaunchedEffect(idRuangan, tanggalSewa, idPengajuan) {
            viewModel.getSlotTerisi(idRuangan, tanggalSewa, idPengajuan)
        }

        when (val state = slotState) {
            is UiState.Loading -> { /* Loading indicator can be shown here */ }
            is UiState.Success -> {
                TimeRangeDialog(
                    timeOptions = generateTimeOptions(),
                    bookedSlots = state.data,
                    initialStart = startTime,
                    initialEnd = endTime,
                    onDismiss = { showTimeDialog = false },
                    onConfirm = { newStart, newEnd ->
                        startTime = newStart
                        endTime = newEnd
                        showTimeDialog = false
                    }
                )
            }
            is UiState.Error -> { showTimeDialog = false }
            is UiState.Idle -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pilih Jadwal", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton({ navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                windowInsets = WindowInsets(top = 24.dp)
            )
        },
        content = {
            paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton({ currentCalendar = (currentCalendar.clone() as Calendar).apply { add(Calendar.MONTH, -1) } }) { Text("<") }
                    Text(
                        text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentCalendar.time),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                    IconButton({ currentCalendar = (currentCalendar.clone() as Calendar).apply { add(Calendar.MONTH, 1) } }) { Text(">") }
                }

                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab").forEach { day ->
                        Text(day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, color = Color.Gray)
                    }
                }

                when (val state = jadwalState) {
                    is UiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is UiState.Success -> {
                        val bookedDatesMap = state.data.tanggal_terbooking.associateBy { it.tanggal }
                        CalendarGrid(
                            currentCalendar = currentCalendar,
                            bookedDates = bookedDatesMap,
                            selectedDate = selectedDate,
                            onDateClick = { dayInfo -> selectedDate = dayInfo.day }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LegendItem(Color(0xFF6B8E7F), "Full")
                            Spacer(Modifier.width(12.dp))
                            LegendItem(Color(0xFFFFD700), "Partial")
                            Spacer(Modifier.width(12.dp))
                            LegendItem(Color(0xFFF0F5F0), "Tersedia")
                            Spacer(Modifier.width(12.dp))
                            LegendItem(Color(0xFFFF8C00), "Dipilih")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (selectedDate != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF8)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Jam Penggunaan Ruangan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("Dipilih: ${(startTime ?: "--:--")} s.d. ${(endTime ?: "--:--")}", fontSize = 12.sp, color = Color.Gray)
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                        OutlinedButton({ showTimeDialog = true }, shape = RoundedCornerShape(10.dp)) { Text("Pilih Jam") }
                                    }
                                }
                            }
                        }

                        if (startTime != null && endTime != null) {
                            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                                CustomButton(
                                    text = "Lanjut",
                                    onClick = {
                                        val cal = currentCalendar.clone() as Calendar
                                        cal.set(Calendar.DAY_OF_MONTH, selectedDate!!)
                                        val tanggalSewa = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

                                        var route = "form?id_ruangan=$idRuangan&tanggal=$tanggalSewa&mulai=$startTime&selesai=$endTime"
                                        if (idPengajuan != null) {
                                            route += "&id_pengajuan=$idPengajuan"
                                        }
                                        navController.navigate(route)
                                    },
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Jadwal yang Telah Dibooking:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(12.dp))

                        val allBookings = remember(state.data.detail_booking_per_tanggal) {
                            state.data.detail_booking_per_tanggal?.flatMap {
                                (dateString, details) ->
                                val day = try { dateString.split("-")[2].toInt() } catch (e: Exception) { 0 }
                                details.map {
                                    detail ->
                                    BookingInfo(
                                        day = day,
                                        bookedBy = detail.organisasi_komunitas,
                                        time = detail.waktu_booking
                                    )
                                }
                            } ?: emptyList()
                        }

                        if (allBookings.isNotEmpty()) {
                            allBookings.forEach {
                                BookingInfoCard(it)
                                Spacer(Modifier.height(8.dp))
                            }
                        } else {
                            Text(
                                "Tidak ada jadwal yang dibooking bulan ini.",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    is UiState.Error -> {
                        Text(state.errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                    is UiState.Idle -> { /* Do nothing */ }
                }
            }
        }
    )
}

@Composable
fun CalendarGrid(
    currentCalendar: Calendar,
    bookedDates: Map<String, com.example.sebook.data.model.JadwalResponseItem>,
    selectedDate: Int?,
    onDateClick: (DayCellInfo) -> Unit
) {
    val daysInMonth = getDaysInMonthWithCalendar(currentCalendar)
    val rows = (daysInMonth.size + 6) / 7
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        repeat(rows) { r ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                repeat(7) { c ->
                    val index = r * 7 + c
                    val day = if (index < daysInMonth.size) daysInMonth[index] else 0
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f).padding(vertical = 2.dp), contentAlignment = Alignment.Center) {
                        if (day > 0) {
                            val cal = currentCalendar.clone() as Calendar
                            cal.set(Calendar.DAY_OF_MONTH, day)
                            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
                            val bookingStatus = bookedDates[dateString]?.status

                            DayCell(
                                day = day,
                                bookingStatus = bookingStatus,
                                isSelected = selectedDate == day,
                                onClick = { onDateClick(DayCellInfo(day, bookingStatus)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DayCellInfo(val day: Int, val bookingStatus: String?)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangeDialog(
    timeOptions: List<String>,
    bookedSlots: List<SlotTerisiItem>,
    initialStart: String?,
    initialEnd: String?,
    onDismiss: () -> Unit,
    onConfirm: (start: String, end: String) -> Unit
) {
    var localStart by remember { mutableStateOf(initialStart ?: "") }
    var localEnd by remember { mutableStateOf(initialEnd ?: "") }
    var startExpanded by remember { mutableStateOf(false) }
    var endExpanded by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val unavailableHours = remember(bookedSlots) {
        bookedSlots.flatMap { slot ->
            val startHour = slot.mulai.split(":")[0].toInt()
            val endHour = slot.selesai.split(":")[0].toInt()
            (startHour until endHour).toList()
        }.toSet()
    }

    val availableStartOptions = timeOptions.filter { it.split(":")[0].toInt() !in unavailableHours }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (localStart.isBlank() || localEnd.isBlank()) {
                    error = "Silakan pilih jam mulai dan jam selesai."
                } else if (!isEndAfterStart(localStart, localEnd)) {
                    error = "Jam selesai harus lebih besar dari jam mulai."
                } else {
                    error = null
                    onConfirm(localStart, localEnd)
                }
            }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } },
        title = { Text("Pilih Jam Penggunaan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = startExpanded,
                    onExpandedChange = { startExpanded = !startExpanded }
                ) {
                    TextField(
                        value = if (localStart.isBlank()) "Pilih Jam Mulai" else localStart,
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        label = { Text("Jam Mulai") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = startExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = startExpanded,
                        onDismissRequest = { startExpanded = false }
                    ) {
                        availableStartOptions.dropLast(1).forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    localStart = option
                                    if (localEnd.isNotBlank() && !isEndAfterStart(option, localEnd)) {
                                        localEnd = ""
                                    }
                                    startExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = endExpanded,
                    onExpandedChange = { endExpanded = !endExpanded }
                ) {
                    TextField(
                        value = if (localEnd.isBlank()) "Pilih Jam Selesai" else localEnd,
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        label = { Text("Jam Selesai") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = endExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = endExpanded,
                        onDismissRequest = { endExpanded = false }
                    ) {
                        val endOptions = if (localStart.isNotBlank()) {
                            val startHour = localStart.split(":")[0].toInt()
                            val nextBookingStartHour = bookedSlots
                                .map { it.mulai.split(":")[0].toInt() }
                                .filter { it > startHour }
                                .minOrNull()

                            val maxHour = nextBookingStartHour?.let { minOf(it, 17) } ?: 17

                            timeOptions.filter {
                                val endHour = it.split(":")[0].toInt()
                                endHour > startHour && endHour <= maxHour
                            }
                        } else {
                            emptyList()
                        }

                        endOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    localEnd = option
                                    endExpanded = false
                                }
                            )
                        }
                    }
                }

                if (error != null) {
                    Text(text = error!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
                Text(
                    text = "Catatan: waktu dalam satuan jam penuh.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}


@Composable
fun BookingInfoCard(bookingInfo: BookingInfo) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color(0xFFF5F5F5)), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.fillMaxWidth().padding(10.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Box(Modifier.size(25.dp).background(Color(0xFF6B8E7F), CircleShape), Alignment.Center) { Text("${bookingInfo.day}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(bookingInfo.bookedBy, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Spacer(Modifier.height(4.dp))
                Text("Jam: ${bookingInfo.time}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun DayCell(day: Int, bookingStatus: String?, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val isClickable = bookingStatus?.equals("Full", ignoreCase = true) != true
    val backgroundColor = when {
        bookingStatus.equals("Full", ignoreCase = true) -> Color(0xFF6B8E7F)
        bookingStatus.equals("Partial", ignoreCase = true) -> Color(0xFFFFD700)
        isSelected -> Color(0xFFFF8C00)
        else -> Color(0xFFF0F5F0)
    }
    val textColor = if (bookingStatus.equals("Full", ignoreCase = true) || isSelected) Color.White else Color.Black

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text("$day", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 11.sp)
    }
}

fun getDaysInMonthWithCalendar(calendar: Calendar): List<Int> {
    val tempCal = calendar.clone() as Calendar
    tempCal.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
    val maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    val days = MutableList(firstDayOfWeek) { 0 }
    for (i in 1..maxDay) {
        days.add(i)
    }
    return days
}

fun generateTimeOptions(): List<String> {
    return (7..17).map { "$it:00" }
}

fun isEndAfterStart(start: String, end: String): Boolean {
    return try {
        val startHour = start.split(":")[0].toInt()
        val endHour = end.split(":")[0].toInt()
        endHour > startHour
    } catch (e: Exception) {
        false
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookingScreen() {
    BookingScreen(rememberNavController(), "1", null)
}
