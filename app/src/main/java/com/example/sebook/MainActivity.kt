package com.example.sebook

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sebook.ui.theme.SebookTheme
import com.example.sebook.ui.theme.auth.register.Regist
import com.example.sebook.ui.theme.components.BottomNavBar
import com.example.sebook.ui.theme.screens.*
import com.example.sebook.ui.theme.screens.booking.BookingFormScreen
import com.example.sebook.ui.theme.screens.booking.BookingScreen
import com.example.sebook.ui.theme.screens.forum.ForumScreen
import com.example.sebook.ui.theme.screens.informasi.DetailRuangan
import com.example.sebook.ui.theme.screens.informasi.RuanganDiSakato
import com.example.sebook.ui.theme.screens.notification.NotificationScreen
import com.example.sebook.ui.theme.screens.riwayat.RiwayatPengajuan
import com.example.sebook.ui.theme.screens.panduan.PanduanScreen
import com.example.sebook.ui.theme.screens.ChangePasswordScreen
import com.example.sebook.ui.theme.screens.riwayat.ReviewScreen
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val requestNotifPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            Log.d("NOTIF_PERMISSION", "granted = $granted")
        }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermissionIfNeeded()
        android.util.Log.d("FCM_TOKEN", "onCreate jalan ✅") // <-- ini harus muncul

        com.google.firebase.messaging.FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    android.util.Log.e("FCM_TOKEN", "Gagal ambil token", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                android.util.Log.d("FCM_TOKEN", "TOKEN: $token")
            }

        setContent {
            SebookTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = when (currentRoute) {
                    "home", "history", "information", "forum" -> true
                    else -> false
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) BottomNavBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "welcome",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable("welcome") {
                            WelcomeScreen(
                                onLoginClick = { navController.navigate("login") },
                                onRegisterClick = { navController.navigate("regist") }
                            )
                        }
                        composable("regist") {
                            Regist(navController = navController)
                        }
                        composable("login") {
                            Login(navController = navController)
                        }
                        composable("home") { HomeContent(innerPadding, navController) }
                        composable("history") { RiwayatPengajuan(navController) }
                        composable("information") { RuanganDiSakato(innerPadding, navController) }
                        composable("forum") { ForumScreen(navController) }

                        // Additional screens
                        composable("notifications") { NotificationScreen(navController) }
                        composable("profile") { ProfileScreen(navController) }
                        composable("change_password") { ChangePasswordScreen(navController) }
                        composable(
                            "jadwal/{id_ruangan}?id_pengajuan={id_pengajuan}",
                            arguments = listOf(
                                navArgument("id_ruangan") { type = NavType.StringType },
                                navArgument("id_pengajuan") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) { backStackEntry ->
                            val idRuangan = backStackEntry.arguments?.getString("id_ruangan") ?: "0"
                            val idPengajuan = backStackEntry.arguments?.getString("id_pengajuan")
                            BookingScreen(
                                navController = navController,
                                idRuangan = idRuangan,
                                idPengajuan = idPengajuan
                            )
                        }
                        composable("panduan") { PanduanScreen(innerPadding, navController) }

                        // Updated DetailRuangan composable call
                        composable("detail_ruangan/{id_ruangan}") { backStackEntry ->
                            val idRuangan = backStackEntry.arguments?.getString("id_ruangan") ?: ""
                            DetailRuangan(
                                navController = navController,
                                idRuangan = idRuangan
                            )
                        }

                        composable(
                            "form?id_ruangan={id_ruangan}&tanggal={tanggal}&mulai={mulai}&selesai={selesai}&id_pengajuan={id_pengajuan}",
                            arguments = listOf(
                                navArgument("id_ruangan") { type = NavType.StringType },
                                navArgument("tanggal") { type = NavType.StringType; nullable = true },
                                navArgument("mulai") { type = NavType.StringType; nullable = true },
                                navArgument("selesai") { type = NavType.StringType; nullable = true },
                                navArgument("id_pengajuan") { type = NavType.StringType; nullable = true }
                            )
                        ) { backStackEntry ->
                            val idRuangan = backStackEntry.arguments?.getString("id_ruangan") ?: ""
                            BookingFormScreen(
                                navController = navController,
                                idRuangan = idRuangan,
                                tanggal = backStackEntry.arguments?.getString("tanggal"),
                                mulai = backStackEntry.arguments?.getString("mulai"),
                                selesai = backStackEntry.arguments?.getString("selesai"),
                                idPengajuan = backStackEntry.arguments?.getString("id_pengajuan")
                            )
                        }
                        composable(
                            "review/{id_pengajuan}",
                            arguments = listOf(navArgument("id_pengajuan") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val idPengajuan = backStackEntry.arguments?.getString("id_pengajuan")
                            if (idPengajuan != null) {
                                ReviewScreen(navController = navController, idPengajuan = idPengajuan)
                            }
                        }
                    }
                }
            }
        }
    }
}