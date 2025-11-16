package com.example.sebook.data.model

data class HistoryResponse(
    val success: Boolean,
    val message: String,
    val history: List<HistoryItem>?
)

data class HistoryItem(
    val id_pengajuan: String,
    val id_user: String,
    val id_ruangan: String,
    val tanggal_sewa: String,
    val waktu_mulai: String,
    val waktu_selesai: String,
    val surat_peminjaman: String,
    val organisasi_komunitas: String,
    val kegiatan: String,
    val status: String,
    val created_at: String,
    val updated_at: String,
    val ruangan: RuanganHistory
)

data class RuanganHistory(
    val nama_ruangan: String,
    val gambar: String
)
