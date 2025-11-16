package com.example.sebook.data.model

data class DetailRuanganResponse(
    val success: Boolean,
    val message: String,
    val detail: DetailRuanganItem
)

data class DetailRuanganItem(
    val id_ruangan: String?,
    val nama_ruangan: String?,
    // Mengubah nama field agar cocok dengan JSON dari backend
    val gambar_ruangans: List<GambarRuanganItem>?,
    val deskripsi: String?
)

// Class baru untuk menampung object gambar
data class GambarRuanganItem(
    val gambar: String?
)
