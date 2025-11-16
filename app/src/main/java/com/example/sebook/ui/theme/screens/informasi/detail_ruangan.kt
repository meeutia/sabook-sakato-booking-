package com.example.sebook.ui.theme.screens.informasi

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.example.sebook.data.model.DetailRuanganItem
import com.example.sebook.data.model.GambarRuanganItem
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRuangan(
    navController: NavController,
    idRuangan: String,
    viewModel: DetailRuanganViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    // Memanggil API saat composable pertama kali ditampilkan
    LaunchedEffect(idRuangan) {
        viewModel.getDetailRuangan(idRuangan)
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Ruangan", fontFamily = FontFamily(Font(R.font.poppins_extrabold)), fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                windowInsets = WindowInsets(top = 24.dp)
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                when (val state = uiState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is UiState.Error -> {
                        Text(
                            text = state.errorMessage,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center).padding(16.dp)
                        )
                    }
                    is UiState.Success -> {
                        DetailRuanganContent(navController, state.data)
                    }
                    is UiState.Idle -> { /* Do nothing */ }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailRuanganContent(navController: NavController, detail: DetailRuanganItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 26.dp, horizontal = 36.dp)
    ) {
        if (!detail.gambar_ruangans.isNullOrEmpty()) {
            val pagerState = rememberPagerState(pageCount = { detail.gambar_ruangans.size })

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) { page ->
                    AsyncImage(
                        model = "${BuildConfig.BASE_URL}/uploads/${detail.gambar_ruangans[page].gambar}",
                        contentDescription = detail.nama_ruangan,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.placeholder_image),
                        error = painterResource(id = R.drawable.error_image)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Page Indicator
                Row(
                    Modifier
                        .height(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color(0xFFF96300) else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }
        } else {
            // Placeholder jika tidak ada gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Gambar tidak tersedia")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tombol aksi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomButton(text = "Ajukan Booking", onClick = { navController.navigate("jadwal/${detail.id_ruangan}") }, modifier = Modifier.weight(1f).padding(end = 4.dp), fontSize = 11.sp)
            CustomButton(text = "Jadwal Tersedia", onClick = { navController.navigate("jadwal/0") }, modifier = Modifier.weight(1f).padding(start = 4.dp), fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Nama Ruangan
        Text(
            text = detail.nama_ruangan ?: "Nama tidak tersedia",
            fontFamily = FontFamily(Font(R.font.poppins_extrabold)),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Deskripsi Ruangan
        Text(
            text = detail.deskripsi ?: "Deskripsi tidak tersedia",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.spacegrotesk_regular)),
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailRuangan() {
    val navController = rememberNavController()
    val dummyDetail = DetailRuanganItem(
        id_ruangan = "123",
        nama_ruangan = "Ruang Rapat Fantasi",
        gambar_ruangans = listOf(GambarRuanganItem("gambar1.jpg"), GambarRuanganItem("gambar2.jpg")),
        deskripsi = "Ini adalah deskripsi ruangan yang sangat detail dan informatif untuk keperluan preview di Jetpack Compose."
    )
    MaterialTheme {
        Scaffold {
            DetailRuanganContent(navController, dummyDetail)
        }
    }
}
