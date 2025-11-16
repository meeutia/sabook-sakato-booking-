package com.example.sebook.data.model


// Response wrapper for the detail history API call
data class HistoryDetailResponse(
    val success: Boolean,
    val message: String,
    val history: List<HistoryDetailItem>
)

// The detailed item itself, containing all fields from the JSON
data class HistoryDetailItem(
    val surat_peminjaman: String,
    val organisasi_komunitas: String,
    val kegiatan: String,
    val ruangan: DetailRuangan,
    val pengajuan_barangs: List<DetailPengajuanBarang>
)

// Nested data class for the room details
data class DetailRuangan(
    val nama_ruangan: String
)

// Nested data class for borrowed items
data class DetailPengajuanBarang(
    val id_pengajuan_barang: String,
    val id_barang: String,
    val barang: DetailBarang
)

// Nested data class for the item details
data class DetailBarang(
    val nama_barang: String
)
