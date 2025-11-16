package com.example.sebook.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sebook.R
import com.example.sebook.ui.theme.components.CustomButton

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Spacer(modifier = Modifier.height(100.dp))

        // Logo SEBOOK
        Image(
            painter = painterResource(id = R.drawable.logo_sebook), // tambahkan logo kamu di drawable folder
            contentDescription = "SEBOOK Logo",
            modifier = Modifier
                .width(280.dp)
                .height(80.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(40.dp))
        // Box with Rectangle3 image as background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopStart
        ) {
            // Background image Rectangle3
            Image(
                painter = painterResource(id = R.drawable.rectangle_3),  // Pastikan gambar ada di drawable folder
                contentDescription = "Rectangle3 Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Welcome Text
            Column(
                modifier = Modifier
                    .padding(start = 28.dp, end = 28.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                // Top content
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(80.dp))
                    Text(
                        text = "Welcome",
                        modifier = Modifier
                            .width(172.dp)
                            .height(45.dp),
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),  // Pastikan kamu sudah menambahkan font Poppins
                            fontWeight = FontWeight(700),
                            color = Color(0xFFFFFFFF)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Selamat Datang di Sistem\nBooking Ruangan Sakato Community Hub",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(200.dp),
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        fontSize = 14.sp,
                        color = Color(0xFFFFFFFF)
                    )

                    Spacer(modifier = Modifier.height(80.dp))


                // Tombol Login dan Daftar
                Row(
                    horizontalArrangement = Arrangement.spacedBy(60.dp)
                ) {
                    CustomButton(
                        text = "LOGIN",
                        onClick = onLoginClick
                    )

                    CustomButton(
                        text = "DAFTAR",
                        onClick = onRegisterClick
                    )

                }
            }
            }
        }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(
        onLoginClick = { /* Arahkan ke Login Screen */ },
        onRegisterClick = { /* Arahkan ke Register Screen */ }
    )
}
