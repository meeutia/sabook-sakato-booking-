package com.example.sebook.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sebook.R

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF638763) // Warna navbar sesuai tema SEBOOK
    ) {
        // Home Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home"
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)), // Use Space Grotesk font
                    fontSize = 12.sp
                )
            },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color(0xFF4A6B5C),
                unselectedIconColor = Color(0xFFB0C4BC),
                unselectedTextColor = Color(0xFFB0C4BC)
            )
        )

        // History Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History"
                )
            },
            label = {
                Text(
                    text = "Riwayat",
                    fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)), // Use Space Grotesk font
                    fontSize = 12.sp
                )
            },
            selected = currentRoute == "history",
            onClick = {
                navController.navigate("history") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color(0xFF4A6B5C),
                unselectedIconColor = Color(0xFFB0C4BC),
                unselectedTextColor = Color(0xFFB0C4BC)
            )
        )

        // Information Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Informasi"
                )
            },
            label = {
                Text(
                    text = "Informasi",
                    fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)), // Use Space Grotesk font
                    fontSize = 12.sp
                )
            },
            selected = currentRoute == "information",
            onClick = {
                navController.navigate("information") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color(0xFF4A6B5C),
                unselectedIconColor = Color(0xFFB0C4BC),
                unselectedTextColor = Color(0xFFB0C4BC)
            )
        )

        // Forum Tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Forum, // Bukan Forums, tapi Forum
                    contentDescription = "Forum"
                )
            },
            label = {
                Text(
                    text = "Forum",
                    fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)), // Use Space Grotesk font
                    fontSize = 12.sp
                )
            },
            selected = currentRoute == "forum",
            onClick = {
                navController.navigate("forum") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                indicatorColor = Color(0xFF4A6B5C),
                unselectedIconColor = Color(0xFFB0C4BC),
                unselectedTextColor = Color(0xFFB0C4BC)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    BottomNavBar(navController = navController)
}