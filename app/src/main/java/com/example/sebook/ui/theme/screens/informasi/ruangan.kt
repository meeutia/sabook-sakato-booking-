package com.example.sebook.ui.theme.screens.informasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.BuildConfig
import com.example.sebook.R
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.CustomButton
import com.example.sebook.ui.theme.components.RoomCard

@Composable
fun RuanganDiSakato(
    innerPadding: PaddingValues, 
    navController: NavController, 
    viewModel: RuanganViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(bottom = 100.dp)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(13.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_sebook),
                    contentDescription = "SEBOOK Logo",
                    modifier = Modifier
                        .width(100.dp)
                        .height(35.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Ruangan di Sakato",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.poppins_bold)),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when (val state = uiState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        state.data.forEach { ruangan ->
                            // Pastikan data yang dibutuhkan tidak null
                            if (ruangan.id_ruangan != null && ruangan.nama_ruangan != null && ruangan.gambar != null) {
                                RoomCard(
                                    roomName = ruangan.nama_ruangan,
                                    onClick = { navController.navigate("detail_ruangan/${ruangan.id_ruangan}") },
                                    imageUrl = "${BuildConfig.BASE_URL}/uploads/${ruangan.gambar}"
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

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                CustomButton(
                    text = "Panduan",
                    onClick = { navController.navigate("panduan") }
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.rectangle_4),
            contentDescription = "Gelombang Hijau",
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 40.dp)
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRuanganDiSakato() {
    RuanganDiSakato(innerPadding = PaddingValues(0.dp), navController = rememberNavController())
}
