package com.example.sebook.ui.theme.screens.riwayat

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.sebook.BuildConfig
import com.example.sebook.R
import com.example.sebook.data.model.HistoryItem
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState

@Composable
fun RiwayatPengajuan(
    navController: NavController,
    viewModel: RiwayatPengajuanViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var pengajuanToDelete by remember { mutableStateOf<HistoryItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getHistory()
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Pembatalan") },
            text = { Text("Apakah Anda yakin ingin membatalkan pengajuan untuk ${pengajuanToDelete?.ruangan?.nama_ruangan}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pengajuanToDelete?.let { viewModel.deletePengajuan(it.id_pengajuan) }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Ya")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Tidak")
                }
            }
        )
    }

    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is UiState.Success -> {
                Toast.makeText(context, state.data.message, Toast.LENGTH_SHORT).show()
            }
            is UiState.Error -> {
                Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        // Judul Halaman
        Text(
            text = "Riwayat Pengajuan",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 48.dp, bottom = 16.dp)
        )

        // Daftar Pengajuan
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                if (state.data.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada riwayat pengajuan.",
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        state.data.forEach { historyItem ->
                            PengajuanCard(
                                historyItem = historyItem,
                                onEditClick = { navController.navigate("jadwal/${historyItem.id_ruangan}?id_pengajuan=${historyItem.id_pengajuan}") },
                                onDeleteClick = {
                                    pengajuanToDelete = historyItem
                                    showDeleteDialog = true
                                },
                                onReviewClick = { navController.navigate("review/${historyItem.id_pengajuan}") }
                            )
                        }
                    }
                }
            }
            is UiState.Error -> {
                Text(text = state.errorMessage)
            }
            is UiState.Idle -> {
                // Do nothing
            }
        }

        // Spacer for bottom padding to avoid clipping with nav bar
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun PengajuanCard(
    historyItem: HistoryItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReviewClick: () -> Unit
) {
    val canReview = historyItem.status == "Disetujui"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Gambar ruangan
            AsyncImage(
                model = "${BuildConfig.BASE_URL}/uploads/${historyItem.ruangan.gambar}",
                contentDescription = "Room Image",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info pengajuan dan tombol
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Baris pertama: Nama ruangan + Status badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Nama ruangan
                    Text(
                        text = historyItem.ruangan.nama_ruangan,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    // Status badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (historyItem.status == "Disetujui") Color(0xFF4CAF50) else Color(0xFFFF5252)
                    ) {
                        Text(
                            text = historyItem.status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Tanggal
                Text(
                    text = historyItem.tanggal_sewa,
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Waktu
                Text(
                    text = "${historyItem.waktu_mulai} - ${historyItem.waktu_selesai}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledIconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFF8C00),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    FilledIconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFF8C00),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Delete",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    FilledIconButton(
                        onClick = onReviewClick,
                        enabled = canReview,
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color(0xFFFF8C00),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_review),
                            contentDescription = "Review",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRiwayatPengajuan() {
    RiwayatPengajuan(navController = rememberNavController())
}
