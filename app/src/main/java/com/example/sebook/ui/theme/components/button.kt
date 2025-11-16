package com.example.sebook.ui.theme.components

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sebook.R
import com.example.sebook.ui.theme.components.CustomButton

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp // Menambahkan parameter fontSize untuk CustomButton
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF96300)),
        modifier = modifier
            .wrapContentWidth() // Automatically adjusts the width based on the text content            .height(44.37183.dp)  // Menentukan tinggi tombol sesuai Figma
            .shadow(elevation = 8.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))  // Menambahkan shadow
            .border(width = 1.dp, color = Color(0xFF4E8BC4), shape = RoundedCornerShape(32.dp))  // Menambahkan border dengan rounded corner
            .background(color = Color(0xFFF96300), shape = RoundedCornerShape(32.dp))  // Menambahkan background dengan rounded corner sesuai Figma

    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize, // Menggunakan parameter fontSize                fontFamily = FontFamily(Font(R.font.poppins_extrabold)),
                fontFamily = FontFamily(Font(R.font.poppins_extrabold)),

                color = Color(0xFFFFFFFF), // Warna teks putih
                textAlign = TextAlign.Center, // Teks rata tengah
                letterSpacing = 2.56.sp // Menambahkan jarak antar huruf sesuai Figma
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun CustomButtonPreview() {
    CustomButton(
        text = "DAFTAR",
        onClick = { /* Handle Button Click */ }
    )
}