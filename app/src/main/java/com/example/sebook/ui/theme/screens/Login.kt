package com.example.sebook.ui.theme.screens

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sebook.R
import com.example.sebook.data.model.LoginRequest
import com.example.sebook.ui.ViewModelFactory
import com.example.sebook.ui.login.LoginViewModel
import com.example.sebook.ui.login.UiState
import com.example.sebook.ui.theme.components.CustomButton
import com.example.sebook.ui.theme.components.CustomTextField

@Composable
fun Login(
    modifier: Modifier = Modifier, 
    navController: NavController, 
    viewModel: LoginViewModel = viewModel(factory = ViewModelFactory(LocalContext.current))
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Image(
                painter = painterResource(id = R.drawable.logo_sebook),
                contentDescription = "SEBOOK Logo",
                modifier = Modifier
                    .width(280.dp)
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "LOGIN",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A2B3C),
                modifier = Modifier.align(Alignment.Start),
                fontFamily = FontFamily(Font(R.font.spacegrotesk_bold)),
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "nama@domain.com",
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "minimal 8 karakter",
                isPassword = true,
                imeAction = ImeAction.Done
            )

            TextButton(
                onClick = { /* Handle forgot password */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Punya akun?",
                    color = Color(0xFF6B8E7F),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            CustomButton(
                text = "LOGIN",
                onClick = {
                    viewModel.login(LoginRequest(email, password))
                },
                modifier = Modifier
                    .align(Alignment.End)
            )

        }

        when (val state = loginState) {
            is UiState.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Success -> {
                LaunchedEffect(state) {
                    Toast.makeText(context, state.data.message, Toast.LENGTH_SHORT).show()
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is UiState.Error -> {
                AlertDialog(
                    onDismissRequest = { viewModel.onLoginErrorShown() },
                    title = {
                        Text(text = "Login Gagal")
                    },
                    text = {
                        Text(text = state.errorMessage)
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.onLoginErrorShown() }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
            is UiState.Idle -> {
                // Do Nothing
            }
        }

        Image(
            painter = painterResource(id = R.drawable.rectangle_3),
            contentDescription = "Rectangle3 Image",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 280.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    MaterialTheme {
        Login(navController = rememberNavController())
    }
}
