package com.example.sebook.data.model

data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val profil: ProfileData
)

data class ProfileData(
    val id_user: String,
    val email: String,
    val nama: String,
    val nohp: String,
    val username: String
)
