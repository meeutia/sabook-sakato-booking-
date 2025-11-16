package com.example.sebook.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import com.example.sebook.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.sp

// Light Theme Colors
private val LightColorScheme = lightColorScheme(
//    primary = GreenWave,
//    onPrimary = OnGreenWave,
//    secondary = OrangeButton,
//    onSecondary = OnOrangeButton,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color(0xFFFAFAFA),
    onSurface = Color.Black
)

// Dark Theme Colors
private val DarkColorScheme = darkColorScheme(
//    primary = GreenWave,
//    onPrimary = OnGreenWave,
//    secondary = OrangeButton,
//    onSecondary = OnOrangeButton,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE6E1E5)
)

// Menambahkan beberapa varian font Poppins
val Poppins = FontFamily(
    Font(R.font.poppins_regular),  // Pastikan nama font sesuai dengan yang ada di folder res/font
    Font(R.font.poppins_extrabold),
    Font(R.font.poppins_bold),
    Font(R.font.poppins_light),
    Font(R.font.poppins_semibold),
    Font(R.font.poppins_medium)
)

val SpaceGrotesk = FontFamily(
    Font(R.font.spacegrotesk_regular),
    Font(R.font.spacegrotesk_bold, FontWeight.Bold)
)
// Mendefinisikan Typography dengan Poppins sebagai font default
private val typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Poppins,  // Menetapkan Poppins sebagai font default untuk body
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Poppins,  // Menetapkan Poppins sebagai font default untuk medium body
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Poppins,  // Menetapkan Poppins sebagai font default untuk small body
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)


@Composable
fun SebookTheme(
    darkTheme: Boolean = false, // true for dark theme, false for light theme
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = typography,  // Menggunakan typography yang sudah didefinisikan
        content = content
    )
}

