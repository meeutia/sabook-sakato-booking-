package com.example.sebook.data.model

data class SlotResponse(
    val success: Boolean,
    val message: String,
    val data_slot_terisi: List<SlotTerisiItem>
)

data class SlotTerisiItem(
    val mulai: String,
    val selesai: String,
    val kegiatan: String
)
