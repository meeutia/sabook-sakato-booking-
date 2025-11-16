package com.example.sebook.ui.theme.auth.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import android.util.Patterns


@Parcelize
data class RegistFormState(
    val fullName: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = ""
) : Parcelable {

    val nameInvalid: Boolean
        get() = fullName.isNotBlank() &&
                (fullName.trim().split(Regex("\\s+")).size < 2 || fullName.any { it.isDigit() })

    val emailInvalid: Boolean
        get() = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val phoneInvalid: Boolean
        get() = phoneNumber.isNotBlank() && !Patterns.PHONE.matcher(phoneNumber).matches()

    val passwordInvalid: Boolean
        get() = password.isNotBlank() && password.length < 8

    val isComplete: Boolean
        get() = fullName.isNotBlank() && username.isNotBlank() && email.isNotBlank() &&
                phoneNumber.isNotBlank() && password.isNotBlank()

    val isValid: Boolean
        get() = isComplete && !nameInvalid && !emailInvalid && !phoneInvalid && !passwordInvalid

    /** Peta ke nama field backend */
    fun toBackendMap(): Map<String, String> = mapOf(
        "nama" to fullName.trim(),
        "username" to username.trim(),
        "email" to email.trim(),
        "nohp" to phoneNumber.trim(),
        "password" to password
    )
}
