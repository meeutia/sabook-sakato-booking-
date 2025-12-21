package com.example.sebook.ui.theme.screens.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.R
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.CustomButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    navController: NavController,
    idPengajuan: String,
    viewModel: ReviewViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    var showValidationDialog by remember { mutableStateOf(false) }
    var validationMessage by remember { mutableStateOf("") }

    val isRatingValid = rating > 0
    val isCommentValid = reviewText.trim().isNotEmpty()
    val isFormValid = isRatingValid && isCommentValid

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Review",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        content = {
                paddingValues ->
            Box(modifier = Modifier.fillMaxSize()){
                if (showValidationDialog) {
                    AlertDialog(
                        onDismissRequest = { showValidationDialog = false },
                        title = { Text("Oops") },
                        text = { Text(validationMessage) },
                        confirmButton = {
                            TextButton(onClick = { showValidationDialog = false }) {
                                Text("OK")
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    // Question Title
                    Text(
                        text = "Bagaimana Pengalamannya?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Rating
                    Text("Rating", fontSize = 14.sp, color = Color.Gray)

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        for (i in 1..5) {
                            IconButton(onClick = { rating = i }) {
                                Icon(
                                    painter = painterResource(id = if (i <= rating) R.drawable.ic_star_filled else R.drawable.ic_star_empty),
                                    contentDescription = "Star $i",
                                    modifier = Modifier.size(32.dp),
                                    tint = if (i <= rating) Color(0xFFF96300) else Color(0xFFF96300)
                                )
                            }
                        }
                    }
                    if (!isRatingValid && reviewText.isNotBlank()) {
                        Text(
                            text = "Pilih rating bintang dulu ya.",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }

                    // Review Text Area
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        label = { Text("Tulis Ulasan Anda") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 5,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            disabledContainerColor = Color(0xFFF5F5F5),
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color(0xFFDBE5DB),
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tombol Ajukan
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CustomButton(
                            text = "Submit",
                            enabled = isFormValid,
                            onClick = {
                                if (!isRatingValid) {
                                    validationMessage = "Rating bintang wajib diisi."
                                    showValidationDialog = true
                                    return@CustomButton
                                }
                                if (!isCommentValid) {
                                    validationMessage = "Komentar wajib diisi."
                                    showValidationDialog = true
                                    return@CustomButton
                                }
                                viewModel.submitReview(idPengajuan, reviewText.trim(), rating)
                            }
                        )
                    }

                    LaunchedEffect(uiState) {
                        when (val state = uiState) {
                            is UiState.Success -> {
                                navController.navigate("forum") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                            is UiState.Error -> {
                                // Optionally, show a snackbar or toast with state.errorMessage
                            }
                            else -> {}
                        }
                    }
                }

                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    ReviewScreen(navController = rememberNavController(), idPengajuan = "dummy-id")
}