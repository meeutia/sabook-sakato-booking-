package com.example.sebook.ui.theme.screens.panduan

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.R
import com.example.sebook.ui.theme.components.CustomButton
import com.github.barteksc.pdfviewer.PDFView
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanduanScreen(innerPadding: PaddingValues, navController: NavController) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Panduan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Kolom utama untuk konten
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(bottom = 100.dp)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Judul Halaman
                    Text(
                        text = "Panduan Peminjaman",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // PDF Viewer Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(450.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        PDFViewScreen()
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tombol Download di pojok kanan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CustomButton(
                            text = "Download",
                            onClick = { downloadPDF(context) }
                        )
                    }
                }

                // Gambar gelombang hijau di bawah halaman
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
    )
}

@Composable
fun PDFViewScreen() {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            PDFView(ctx, null).apply {
                fromStream(ctx.resources.openRawResource(R.raw.panduan))
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(true)
                    .spacing(10)
                    .onError { _ ->
                        Toast.makeText(
                            ctx,
                            "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .load()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

// Fungsi untuk download PDF ke folder Downloads publik
fun downloadPDF(context: Context) {
    val inputStream = context.resources.openRawResource(R.raw.panduan)
    val fileName = "Panduan_Peminjaman.pdf"
    var outputStream: OutputStream? = null

    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            outputStream = uri?.let { resolver.openOutputStream(it) }
        } else {
            // Untuk Android versi lama, kita butuh izin WRITE_EXTERNAL_STORAGE
            @Suppress("DEPRECATION")
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = java.io.File(downloadsDir, fileName)
            outputStream = FileOutputStream(file)
        }

        outputStream?.let {
            inputStream.copyTo(it)
            Toast.makeText(
                context,
                "PDF berhasil didownload ke folder Downloads!",
                Toast.LENGTH_LONG
            ).show()
        } ?: throw IOException("Gagal mendapatkan output stream.")

    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Gagal download PDF: ${e.message}",
            Toast.LENGTH_SHORT
        ).show()
        e.printStackTrace()
    } finally {
        try {
            inputStream.close()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPanduanScreen() {
    PanduanScreen(innerPadding = PaddingValues(0.dp), navController = rememberNavController())
}