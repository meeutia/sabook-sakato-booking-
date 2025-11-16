package com.example.sebook.ui.theme.screens.booking

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.BarangItem
import com.example.sebook.data.model.BookingRequest
import com.example.sebook.data.model.HistoryDetailItem
import com.example.sebook.data.model.SubmitBookingResponse
import com.example.sebook.data.repository.CheckBarangRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class BookingViewModel(private val repository: CheckBarangRepository) : ViewModel() {

    private val _barangState = MutableStateFlow<UiState<List<BarangItem>>>(UiState.Idle)
    val barangState: StateFlow<UiState<List<BarangItem>>> = _barangState

    private val _submitBookingState = MutableStateFlow<UiState<SubmitBookingResponse>>(UiState.Idle)
    val submitBookingState: StateFlow<UiState<SubmitBookingResponse>> = _submitBookingState

    private val _detailState = MutableStateFlow<UiState<HistoryDetailItem>>(UiState.Idle)
    val detailState: StateFlow<UiState<HistoryDetailItem>> = _detailState

    fun getDetailPengajuan(idPengajuan: String) {
        _detailState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getHistoryDetail(idPengajuan)
                if (response.success && response.history.isNotEmpty()) {
                    _detailState.value = UiState.Success(response.history.first())
                } else {
                    _detailState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _detailState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getAvailableBarang(bookingRequest: BookingRequest, idPengajuan: String? = null) {
        _barangState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.checkAvailableBarang(bookingRequest, idPengajuan)
                if (response.success) {
                    _barangState.value = UiState.Success(response.barang)
                } else {
                    _barangState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _barangState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _barangState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun submitBooking(
        idRuangan: String,
        tanggalSewa: String,
        waktuMulai: String,
        waktuSelesai: String,
        organisasiKomunitas: String,
        kegiatan: String,
        barangDipinjam: List<String>,
        suratPeminjaman: Uri,
        contentResolver: ContentResolver
    ) {
        _submitBookingState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.submitBooking(
                    idRuangan,
                    tanggalSewa,
                    waktuMulai,
                    waktuSelesai,
                    organisasiKomunitas,
                    kegiatan,
                    barangDipinjam,
                    suratPeminjaman,
                    contentResolver
                )
                if (response.success) {
                    _submitBookingState.value = UiState.Success(response)
                } else {
                    _submitBookingState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _submitBookingState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _submitBookingState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun updateBooking(
        idPengajuan: String,
        tanggalSewa: String,
        waktuMulai: String,
        waktuSelesai: String,
        organisasiKomunitas: String,
        kegiatan: String,
        barangDipinjam: List<String>,
        suratPeminjaman: Uri?,
        contentResolver: ContentResolver
    ) {
        _submitBookingState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateBooking(
                    idPengajuan,
                    tanggalSewa,
                    waktuMulai,
                    waktuSelesai,
                    organisasiKomunitas,
                    kegiatan,
                    barangDipinjam,
                    suratPeminjaman,
                    contentResolver
                )
                if (response.success) {
                    _submitBookingState.value = UiState.Success(response)
                } else {
                    _submitBookingState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _submitBookingState.value = UiState.Error(e.message ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _submitBookingState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
