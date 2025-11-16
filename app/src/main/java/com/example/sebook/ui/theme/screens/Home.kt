package com.example.sebook.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.BuildConfig
import com.example.sebook.R
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.BottomNavBar
import com.example.sebook.ui.theme.components.CustomButton
import com.example.sebook.ui.theme.components.RoomCard
import com.example.sebook.ui.theme.screens.informasi.RuanganViewModel
import kotlin.math.absoluteValue


@Composable
fun Home() {
    val previewNav = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = previewNav)
        },
        content = { innerPadding ->
            HomeContent(innerPadding, previewNav)
        }
    )
}


@Composable
fun HomeContent(
    innerPadding: PaddingValues, 
    navController: NavHostController, 
    viewModel: RuanganViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .background(Color.White)

    ) {
        Spacer(modifier = Modifier.height(18.dp))

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sebook),
                contentDescription = "SEBOOK Logo",
                modifier = Modifier.width(100.dp).height(35.dp),
                contentScale = ContentScale.Fit
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // Notification Icon
                Box {
                    IconButton(onClick = { navController.navigate("notifications") }, modifier = Modifier.size(45.dp)) {
                        Icon(painter = painterResource(id = R.drawable.ic_notification), contentDescription = "Notifications", modifier = Modifier.size(28.dp), tint = Color.Black)
                    }
                    val notificationCount by remember { mutableStateOf(5) }
                    if (notificationCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = (-4).dp, y = 4.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF3B30)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if (notificationCount > 9) "9+" else notificationCount.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Profile Image
                Image(
                    painter = painterResource(id = R.drawable.profile_placeholder),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFF96300), CircleShape)
                        .clickable { navController.navigate("profile") },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Welcome Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(300.dp)
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(painter = painterResource(id = R.drawable.profile_placeholder), contentDescription = "Welcome Banner", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    Image(painter = painterResource(id = R.drawable.rectangle_2), contentDescription = "Curved Background", contentScale = ContentScale.FillWidth, alignment = Alignment.TopCenter, modifier = Modifier.fillMaxWidth().offset(y = 150.dp).align(Alignment.TopCenter))
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                            .offset(y = 35.dp)
                    ) {
                        Text(text = "Welcome,", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // User and Booking
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Pengguna", fontWeight = FontWeight.SemiBold, fontSize = 26.sp, color = Color.Black, modifier = Modifier.padding(start = 12.dp))
//            CustomButton(text = "Booking", onClick = { navController.navigate("booking") }, modifier = Modifier.padding(top = 12.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Informasi Ruangan Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Informasi Ruangan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "Selengkapnya", fontSize = 13.sp, color = Color(0xFF6B8E7F), modifier = Modifier.clickable { navController.navigate("information") })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic HorizontalPager
        when (val state = uiState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                val ruanganList = state.data.filter { it.id_ruangan != null && it.nama_ruangan != null && it.gambar != null }
                val pagerState = rememberPagerState(pageCount = { ruanganList.size })

                if (ruanganList.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 40.dp), // Adjust padding for peek effect
                        pageSpacing = 16.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val ruangan = ruanganList[page]
                        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        val scale = lerp(start = 0.85f, stop = 1f, fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))

                        Box(modifier = Modifier.graphicsLayer { 
                            scaleX = scale
                            scaleY = scale
                        }) {
                            RoomCard(
                                roomName = ruangan.nama_ruangan!!,
                                onClick = { navController.navigate("detail_ruangan/${ruangan.id_ruangan}") },
                                imageUrl = "${BuildConfig.BASE_URL}/uploads/${ruangan.gambar}"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pager Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(ruanganList.size) { index ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index) Color(0xFFFF8C00)
                                        else Color(0xFFD9D9D9)
                                    )
                            )
                        }
                    }
                }

            }
            is UiState.Error -> {
                Text(text = state.errorMessage, modifier = Modifier.padding(horizontal = 16.dp))
            }
            is UiState.Idle -> { }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Preview (opsional, bisa disesuaikan atau dihapus jika tidak diperlukan)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Home()
}
