package com.example.sebook.ui.theme.screens.booking

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sebook.R
import com.example.sebook.data.model.BarangItem
import com.example.sebook.data.model.BookingRequest
import com.example.sebook.data.model.HistoryDetailItem
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.CustomButton
import com.example.sebook.ui.theme.components.CustomTextField

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        color = Color(0xFF1A2B3C),
        fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun BarangDropdown(
    label: String = "Barang yang di pakai",
    options: List<String>,
    selected: List<String>,
    onSelectedChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF1A2B3C),
            fontFamily = FontFamily(Font(R.font.spacegrotesk_bold))
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            val anchorText = if (selected.isEmpty()) "" else selected.joinToString(", ")
            CustomTextField(
                value = anchorText,
                onValueChange = {},
                label = "",
                placeholder = if (enabled) "Choose" else "Loading...",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { expanded = true },
                isError = false,
                enabled = enabled
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 24.dp)
                    .padding(end = 12.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF1A2B3C)
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (options.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("Tidak ada barang tersedia") },
                        onClick = { },
                        enabled = false
                    )
                } else {
                    options.forEach { item ->
                        val checked = item in selected
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = checked, onCheckedChange = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text(item)
                                }
                            },
                            onClick = {
                                val newSelected = if (checked) selected - item else selected + item
                                onSelectedChange(newSelected)
                            }
                        )
                    }
                }
            }
        }

        if (selected.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selected.forEach { item ->
                    androidx.compose.material3.AssistChip(
                        onClick = { onSelectedChange(selected - item) },
                        label = { Text(item) },
                        trailingIcon = {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Remove"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UploadField(
    label: String = "Surat Peminjaman",
    fileName: String?,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF1A2B3C),
            fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF5F5F5),
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onUploadClick() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Outlined.CloudUpload,
                    contentDescription = null,
                    tint = Color(0xFF1A2B3C)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = fileName ?: "Upload (PDF)",
                    color = if (fileName != null) Color(0xFF1A2B3C) else Color(0xFF6B8E7F),
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun ContentResolver.getFileName(uri: Uri): String? {
    var name: String? = null
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}

@Composable
fun BookingFormScreen(
    navController: NavController,
    idRuangan: String,
    tanggal: String?,
    mulai: String?,
    selesai: String?,
    idPengajuan: String?,
    bookingViewModel: BookingViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    var asalKomunitas by rememberSaveable { mutableStateOf("") }
    var kegiatan by rememberSaveable { mutableStateOf("") }
    var selectedBarang by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
    var suratFileName by rememberSaveable { mutableStateOf<String?>(null) }
    var suratFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val isEditMode = idPengajuan != null

    val detailState by bookingViewModel.detailState.collectAsState()

    LaunchedEffect(idPengajuan) {
        if (isEditMode) {
            bookingViewModel.getDetailPengajuan(idPengajuan!!)
        }
    }

    LaunchedEffect(detailState) {
        if (detailState is UiState.Success) {
            val detail = (detailState as UiState.Success<HistoryDetailItem>).data
            asalKomunitas = detail.organisasi_komunitas
            kegiatan = detail.kegiatan
            selectedBarang = detail.pengajuan_barangs.map { it.barang.nama_barang }
            suratFileName = detail.surat_peminjaman
        }
    }


    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        suratFileUri = uri
        suratFileName = uri?.let { context.contentResolver.getFileName(it) }
    }

    LaunchedEffect(tanggal, mulai, selesai, idPengajuan) {
        if (tanggal != null && mulai != null && selesai != null) {
            val request = BookingRequest(
                tanggal_sewa = tanggal,
                waktu_mulai = mulai,
                waktu_selesai = selesai
            )
            bookingViewModel.getAvailableBarang(request, idPengajuan)
        }
    }

    val barangState by bookingViewModel.barangState.collectAsState()
    val submitState by bookingViewModel.submitBookingState.collectAsState()

    val (barangOptions, isBarangLoading, barangErrorMessage) = when (val state = barangState) {
        is UiState.Loading -> Triple(emptyList<BarangItem>(), true, null)
        is UiState.Success -> Triple(state.data, false, null)
        is UiState.Error -> Triple(emptyList<BarangItem>(), false, state.errorMessage)
        is UiState.Idle -> Triple(emptyList<BarangItem>(), true, null)
    }

    LaunchedEffect(barangErrorMessage) {
        barangErrorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    when (val state = submitState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Success -> {
            LaunchedEffect(Unit) {
                Toast.makeText(context, state.data.message, Toast.LENGTH_SHORT).show()
                navController.navigate("history") {
                    popUpTo("home") { inclusive = false }
                }
            }
        }
        is UiState.Error -> {
            LaunchedEffect(state.errorMessage) {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        else -> {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(48.dp))
            SectionTitle(if (isEditMode) "Edit Pengajuan Peminjaman" else "Isi Pengajuan Peminjaman")

            CustomTextField(
                value = asalKomunitas,
                onValueChange = { asalKomunitas = it },
                label = "Asal Komunitas/Organisasi",
                placeholder = "Enter",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(16.dp))
            CustomTextField(
                value = kegiatan,
                onValueChange = { kegiatan = it },
                label = "Kegiatan",
                placeholder = "Enter",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(16.dp))

            if (isBarangLoading) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
            } else {
                BarangDropdown(
                    options = barangOptions.map { it.nama_barang },
                    selected = selectedBarang,
                    onSelectedChange = { selectedBarang = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isBarangLoading
                )
            }

            Spacer(Modifier.height(16.dp))

            UploadField(
                label = "Surat Peminjaman",
                fileName = suratFileName,
                onUploadClick = {
                    filePickerLauncher.launch("application/pdf")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomButton(
                    text = if (isEditMode) "Simpan Perubahan" else "Ajukan",
                    onClick = {
                        if (tanggal != null && mulai != null && selesai != null) {
                            val selectedBarangIds = barangOptions
                                .filter { it.nama_barang in selectedBarang }
                                .map { it.id_barang }

                            if (isEditMode) {
                                bookingViewModel.updateBooking(
                                    idPengajuan = idPengajuan!!,
                                    tanggalSewa = tanggal,
                                    waktuMulai = "$mulai:00",
                                    waktuSelesai = "$selesai:00",
                                    organisasiKomunitas = asalKomunitas,
                                    kegiatan = kegiatan,
                                    barangDipinjam = selectedBarangIds,
                                    suratPeminjaman = suratFileUri, // Can be null
                                    contentResolver = context.contentResolver
                                )
                            } else {
                                if (suratFileUri != null) {
                                    bookingViewModel.submitBooking(
                                        idRuangan = idRuangan,
                                        tanggalSewa = tanggal,
                                        waktuMulai = "$mulai:00",
                                        waktuSelesai = "$selesai:00",
                                        organisasiKomunitas = asalKomunitas,
                                        kegiatan = kegiatan,
                                        barangDipinjam = selectedBarangIds,
                                        suratPeminjaman = suratFileUri!!,
                                        contentResolver = context.contentResolver
                                    )
                                } else {
                                    Toast.makeText(context, "Surat peminjaman wajib diisi", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingFormPreview() {
    MaterialTheme {
//        BookingFormScreen(onSubmit = { _, _ -> }, tanggal = "2024-01-01", mulai = "09:00", selesai = "11:00")
    }
}
