package com.example.sebook.data.repository

import com.example.sebook.data.model.DetailRuanganResponse
import com.example.sebook.data.model.JadwalResponse
import com.example.sebook.data.model.RuanganResponse
import com.example.sebook.data.model.SlotResponse
import com.example.sebook.data.network.ApiService

class RuanganRepository(private val apiService: ApiService) {
    suspend fun getRuangan(): RuanganResponse {
        return apiService.getRuangan()
    }

    suspend fun getDetailRuangan(idRuangan: String): DetailRuanganResponse {
        return apiService.getDetailRuangan(idRuangan)
    }

    suspend fun getJadwalRuangan(idRuangan: String): JadwalResponse {
        return apiService.getJadwalRuangan(idRuangan)
    }

    suspend fun getJadwalRuanganForEdit(idRuangan: String, idPengajuan: String): JadwalResponse {
        return apiService.getJadwalRuanganForEdit(idRuangan, idPengajuan)
    }

    suspend fun getSlotTerisi(idRuangan: String, tanggalSewa: String, idPengajuan: String? = null): SlotResponse {
        return if (idPengajuan != null) {
            apiService.getSlotTerisiForEdit(idRuangan, idPengajuan, tanggalSewa)
        } else {
            apiService.getSlotTerisi(idRuangan, tanggalSewa)
        }
    }
}