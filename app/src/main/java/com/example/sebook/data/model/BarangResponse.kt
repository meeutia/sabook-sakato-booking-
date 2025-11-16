package com.example.sebook.data.model

data class BarangResponse(
    val success: Boolean,

    val message: String,

    val barang: List<BarangItem>
)

data class BarangItem(
    val id_barang: String,

    val nama_barang: String,

    val stok_tersedia_saat_ini: String
)
