package com.example.sebook.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sebook.R
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Icon
import androidx.compose.ui.text.input.KeyboardCapitalization

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
    imeAction: ImeAction = if (isPassword) ImeAction.Done else ImeAction.Next,
    keyboardType: KeyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None, // ⟵ tambahkan ini
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    leadingIcon: (@Composable (() -> Unit))? = null,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    val labelFamily = FontFamily(Font(R.font.spacegrotesk_regular))
    val labelStyle = TextStyle(
        fontFamily = labelFamily,
        fontSize = 16.sp,
        color = Color(0xFF6B8E7F)
    )

    var passwordVisible by remember { mutableStateOf(false) }
    val visualTransformation =
        if (isPassword && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None

    val trailingIcon: (@Composable () -> Unit)? =
        if (isPassword) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"
                    )
                }
            }
        } else null


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        enabled = enabled,
        isError = isError,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            // container warna abu terang
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedContainerColor = Color(0xFFF5F5F5),
            disabledContainerColor = Color(0xFFF5F5F5).copy(alpha = 0.7f),
            errorContainerColor = Color(0xFFF5F5F5),
            // border transparan (desain outlined tapi tampak fill)
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            // teks & cursor
            cursorColor = Color(0xFF6B8E7F),
            focusedTextColor = Color(0xFF111827),
            unfocusedTextColor = Color(0xFF111827),
            disabledTextColor = Color(0xFF111827).copy(alpha = 0.6f),
            errorTextColor = Color(0xFF111827),
            // label & placeholder
            focusedLabelColor = Color(0xFF6B8E7F),
            unfocusedLabelColor = Color(0xFF6B8E7F),
            errorLabelColor = Color(0xFF6B8E7F),
            focusedPlaceholderColor = Color(0xFF6B7280),
            unfocusedPlaceholderColor = Color(0xFF6B7280)
        ),
        label = { Text(text = label, style = labelStyle) },
        placeholder = placeholder?.let {
            { Text(text = it, style = labelStyle.copy(color = Color(0xFF6B7280))) }
        },
        trailingIcon = trailingIcon,
        supportingText = supportingText?.let { { Text(it) } },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = capitalization
        ),
        visualTransformation = visualTransformation
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewUsername() {
    var fullName by remember { mutableStateOf("") } // <-- definisikan state di sini
    // Validasi ringan (opsional)
    val nameInvalid = fullName.isNotBlank() && (
            fullName.trim().split(Regex("\\s+")).size < 2 || fullName.any { it.isDigit() }
            )
    CustomTextField(
        value = fullName,
        onValueChange = { fullName = it },
        label = "Full Name",
        placeholder = "Nama Lengkap",
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Next,
        supportingText = if (nameInvalid) "Tulis nama lengkap (tanpa angka)" else null
    )
}

//@Preview(showBackground = true)
//@Composable
//private fun PreviewPassword() {
//    CustomTextField(
//        value = barang ?: "",
//        onValueChange = {},                 // diabaikan karena readOnly
//        label = "Barang yang di pakai",
//        placeholder = "Choose",
//        readOnly = true,
//        onClick = { expanded = true },      // state dropdown-mu
//        trailingIcon = { Icon(Icons.Outlined.KeyboardArrowDown, null) }
//    )
//}