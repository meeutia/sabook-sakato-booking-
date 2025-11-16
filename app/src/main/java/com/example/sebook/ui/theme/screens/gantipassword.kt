package com.example.sebook.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.R
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.CustomButton
import com.example.sebook.ui.theme.components.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(navController: NavController, viewModel: ChangePasswordViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val changePasswordState by viewModel.changePasswordState.collectAsState()

    // Validation states
    val newPasswordTooShort = newPassword.isNotBlank() && newPassword.length < 8
    val passwordsNotMatch = confirmPassword.isNotBlank() && newPassword != confirmPassword
    val canSubmit = currentPassword.isNotBlank() &&
            newPassword.length >= 8 &&
            newPassword == confirmPassword

    LaunchedEffect(changePasswordState) {
        when (val state = changePasswordState) {
            is UiState.Success -> {
                showSuccessDialog = true
            }
            is UiState.Error -> {
                errorMessage = state.errorMessage
                showErrorDialog = true
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ganti Password",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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
                    .background(Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Lock Icon
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                Color(0xFFFFF8F0),
                                shape = androidx.compose.foundation.shape.CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Lock Icon",
                            tint = Color(0xFFF96300),
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Keamanan Akun",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A2B3C)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Pastikan password baru Anda kuat dan mudah diingat",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Current Password Field
                    Text(
                        text = "Password Lama",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A2B3C),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Password Lama",
                        placeholder = "Masukkan password lama",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // New Password Field
                    Text(
                        text = "Password Baru",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A2B3C),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = "Password Baru",
                        placeholder = "Minimal 8 karakter",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        isError = newPasswordTooShort,
                        supportingText = if (newPasswordTooShort) "Password minimal 8 karakter" else null
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Confirm Password Field
                    Text(
                        text = "Konfirmasi Password Baru",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A2B3C),
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CustomTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Konfirmasi Password",
                        placeholder = "Ulangi password baru",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        isError = passwordsNotMatch,
                        supportingText = if (passwordsNotMatch) "Password tidak cocok" else null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password Requirements
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Persyaratan Password:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A2B3C)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            PasswordRequirementItem(
                                text = "Minimal 8 karakter",
                                isMet = newPassword.length >= 8
                            )
                            PasswordRequirementItem(
                                text = "Kombinasi huruf dan angka (disarankan)",
                                isMet = newPassword.any { it.isDigit() } && newPassword.any { it.isLetter() }
                            )
                            PasswordRequirementItem(
                                text = "Password harus cocok",
                                isMet = newPassword.isNotEmpty() && newPassword == confirmPassword
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Submit Button
                    CustomButton(
                        text = "UBAH PASSWORD",
                        onClick = {
                            if (changePasswordState !is UiState.Loading) {
                                if (canSubmit) {
                                    viewModel.changePassword(currentPassword, newPassword)
                                } else {
                                    errorMessage = when {
                                        currentPassword.isBlank() -> "Password lama harus diisi"
                                        newPasswordTooShort -> "Password baru minimal 8 karakter"
                                        passwordsNotMatch -> "Konfirmasi password tidak cocok"
                                        else -> "Mohon lengkapi semua field"
                                    }
                                    showErrorDialog = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (changePasswordState is UiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    )

    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(64.dp)
                )
            },
            title = {
                Text(
                    text = "Berhasil!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Password Anda berhasil diubah",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gunakan password baru untuk login selanjutnya",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF96300)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Gagal",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(errorMessage)
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF96300)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun PasswordRequirementItem(text: String, isMet: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isMet) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isMet) Color(0xFF4CAF50) else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isMet) Color(0xFF4CAF50) else Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreen(navController = rememberNavController())
}
