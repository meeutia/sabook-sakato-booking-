package com.example.sebook.ui.theme.auth.register

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sebook.R
import com.example.sebook.ui.theme.components.CustomButton
import com.example.sebook.ui.theme.components.CustomTextField
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Regist(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(context))
    var fullName by remember { mutableStateOf("") }
    val nameInvalid = fullName.isNotBlank() && (
            fullName.trim().split(Regex("\\s+")).size < 2 || fullName.any { it.isDigit() }
            )

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val emailInvalid = email.isNotBlank() &&
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()

    var password by remember { mutableStateOf("") }

    var phoneNumber by remember { mutableStateOf("") }
    val phoneInvalid = phoneNumber.isNotBlank() && !Patterns.PHONE.matcher(phoneNumber).matches()

    val canSubmit = email.isNotBlank() && !emailInvalid && fullName.isNotBlank() && username.isNotBlank() && password.isNotBlank() && !nameInvalid && !phoneInvalid

    val registerState by viewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is RegisterState.Success -> {
                if (state.response.success) {
                    navController.navigate("login") { // Or your login route
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            is RegisterState.Error -> {
                // Show a snackbar or a dialog with the error message
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())

    ) {
        Image(
            painter = painterResource(id = R.drawable.rectangle_1), // Assuming this is the new rectangle image for the wave
            contentDescription = "Rectangle Wave",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp) // Decrease height if needed
                .align(Alignment.TopCenter) // Place it at the top of the screen
                .offset(y = -2.dp) // Shift image higher
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo SEBOOK
            Image(
                painter = painterResource(id = R.drawable.logo_sebook),
                contentDescription = "SEBOOK Logo",
                modifier = Modifier
                    .width(280.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Register Title
            Text(
                text = "REGISTER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A2B3C),
                modifier = Modifier.align(Alignment.Start),
                fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)),
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
                placeholder = "Nama Lengkap",
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
                isError = nameInvalid,
                supportingText = if (nameInvalid) "Tulis nama lengkap minimal 2 kata (tanpa angka)" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username only
            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                placeholder = "nama_pengguna",
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Done
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "nama@domain.com",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                isError = emailInvalid,
                supportingText = if (emailInvalid) "Format email tidak valid" else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Field
            CustomTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Phone Number",
                placeholder = "Nomor Handphone",
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done,
                isError = phoneInvalid,
                supportingText = if (phoneInvalid) "Format nomor tidak valid" else null
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Password Field
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "minimal 8 karakter",
                isPassword = true,
                imeAction = ImeAction.Done
            )

            Spacer(modifier = Modifier.height(74.dp))
            CustomButton(
                text = "DAFTAR",
                onClick = {
                    if (canSubmit) {
                        viewModel.registerUser(
                            nama = fullName,
                            email = email,
                            password = password,
                            nohp = phoneNumber,
                            username = username
                        )
                    } else {
                        Toast.makeText(context, "Harap isi semua data dengan benar", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .align(Alignment.End)
            )

            // Add some space before the background image
        }

        if (registerState is RegisterState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistPreview() {
    MaterialTheme {
        Regist(navController = rememberNavController())
    }
}
