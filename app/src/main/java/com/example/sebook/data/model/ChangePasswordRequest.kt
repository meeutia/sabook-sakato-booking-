package com.example.sebook.data.model

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
