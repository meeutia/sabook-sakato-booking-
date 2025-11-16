package com.example.sebook.data.model

import com.squareup.moshi.Json

data class RuanganResponse(
    val success: Boolean,
    val message: String,
    val ruangan: List<RuanganItem>
)

data class RuanganItem(
    // Nama properti disamakan persis dengan field di JSON
    val id_ruangan: String?,
    val nama_ruangan: String?,
    val gambar: String?
)
