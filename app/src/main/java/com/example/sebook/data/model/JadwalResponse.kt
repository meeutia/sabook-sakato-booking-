package com.example.sebook.data.model

// Root object of the response
data class JadwalResponse(
    val success: Boolean,
    val message: String,
    val tanggal_terbooking: List<JadwalResponseItem>,
    // This is a map where the key is the date string (e.g., "2025-11-07")
    val detail_booking_per_tanggal: Map<String, List<DetailBookingItem>>?
)

// Represents the items in the "tanggal_terbooking" array
data class JadwalResponseItem(
    val tanggal: String,
    val status: String
)

// Represents the items in the array for each date in "detail_booking_per_tanggal"
data class DetailBookingItem(
    val organisasi_komunitas: String,
    val waktu_booking: String
)
