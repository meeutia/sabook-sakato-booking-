package com.example.sebook.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.example.sebook.data.model.BarangResponse
import com.example.sebook.data.model.BookingRequest
import com.example.sebook.data.model.HistoryDetailResponse
import com.example.sebook.data.model.HistoryResponse
import com.example.sebook.data.model.SubmitBookingResponse
import com.example.sebook.data.network.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class CheckBarangRepository(private val apiService: ApiService) {
    suspend fun checkAvailableBarang(bookingRequest: BookingRequest, idPengajuan: String? = null): BarangResponse {
        return if (idPengajuan != null) {
            apiService.checkAvailableBarangForEdit(idPengajuan, bookingRequest)
        } else {
            apiService.checkAvailableBarang(bookingRequest)
        }
    }

    suspend fun getHistoryDetail(idPengajuan: String): HistoryDetailResponse {
        return apiService.getHistoryDetail(idPengajuan)
    }

    suspend fun submitBooking(
        idRuangan: String,
        tanggalSewa: String,
        waktuMulai: String,
        waktuSelesai: String,
        organisasiKomunitas: String,
        kegiatan: String,
        barangDipinjam: List<String>,
        suratPeminjaman: Uri,
        contentResolver: ContentResolver
    ): SubmitBookingResponse {
        val jsonObject = JSONObject()
        jsonObject.put("barang", JSONArray(barangDipinjam))
        val barangDipinjamJson = jsonObject.toString()

        val requestFile = contentResolver.openInputStream(suratPeminjaman)?.readBytes()?.toRequestBody("application/pdf".toMediaTypeOrNull())
        val suratPart = requestFile?.let { MultipartBody.Part.createFormData("surat_peminjaman", contentResolver.getFileName(suratPeminjaman), it) }

        return apiService.submitBooking(
            idRuangan = idRuangan,
            tanggalSewa = tanggalSewa.toRequestBody("text/plain".toMediaTypeOrNull()),
            waktuMulai = waktuMulai.toRequestBody("text/plain".toMediaTypeOrNull()),
            waktuSelesai = waktuSelesai.toRequestBody("text/plain".toMediaTypeOrNull()),
            organisasiKomunitas = organisasiKomunitas.toRequestBody("text/plain".toMediaTypeOrNull()),
            kegiatan = kegiatan.toRequestBody("text/plain".toMediaTypeOrNull()),
            barangDipinjam = barangDipinjamJson.toRequestBody("application/json".toMediaTypeOrNull()),
            suratPeminjaman = suratPart!!
        )
    }

    suspend fun updateBooking(
        idPengajuan: String,
        tanggalSewa: String,
        waktuMulai: String,
        waktuSelesai: String,
        organisasiKomunitas: String,
        kegiatan: String,
        barangDipinjam: List<String>,
        suratPeminjaman: Uri?,
        contentResolver: ContentResolver
    ): SubmitBookingResponse {
        val jsonObject = JSONObject()
        jsonObject.put("barang", JSONArray(barangDipinjam))
        val barangDipinjamJson = jsonObject.toString()

        val suratPart = suratPeminjaman?.let { uri ->
            val requestFile = contentResolver.openInputStream(uri)?.readBytes()?.toRequestBody("application/pdf".toMediaTypeOrNull())
            requestFile?.let { MultipartBody.Part.createFormData("surat_peminjaman", contentResolver.getFileName(uri), it) }
        }

        return apiService.updateBooking(
            idPengajuan = idPengajuan,
            tanggalSewa = tanggalSewa.toRequestBody("text/plain".toMediaTypeOrNull()),
            waktuMulai = waktuMulai.toRequestBody("text/plain".toMediaTypeOrNull()),
            waktuSelesai = waktuSelesai.toRequestBody("text/plain".toMediaTypeOrNull()),
            organisasiKomunitas = organisasiKomunitas.toRequestBody("text/plain".toMediaTypeOrNull()),
            kegiatan = kegiatan.toRequestBody("text/plain".toMediaTypeOrNull()),
            barangDipinjam = barangDipinjamJson.toRequestBody("application/json".toMediaTypeOrNull()),
            suratPeminjaman = suratPart
        )
    }

    private fun ContentResolver.getFileName(uri: Uri): String? {
        var name: String? = null
        val cursor = query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }
}