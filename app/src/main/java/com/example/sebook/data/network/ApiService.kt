package com.example.sebook.data.network

import com.example.sebook.data.model.BarangResponse
import com.example.sebook.data.model.BookingRequest
import com.example.sebook.data.model.ChangePasswordRequest
import com.example.sebook.data.model.ChangePasswordResponse
import com.example.sebook.data.model.DetailRuanganResponse
import com.example.sebook.data.model.ForumReviewResponse
import com.example.sebook.data.model.HistoryDetailResponse
import com.example.sebook.data.model.HistoryResponse
import com.example.sebook.data.model.JadwalResponse
import com.example.sebook.data.model.LoginRequest
import com.example.sebook.data.model.LoginResponse
import com.example.sebook.data.model.ProfileResponse
import com.example.sebook.data.model.RegisterRequest
import com.example.sebook.data.model.RegisterResponse
import com.example.sebook.data.model.ReviewRequest
import com.example.sebook.data.model.ReviewResponse
import com.example.sebook.data.model.RuanganResponse
import com.example.sebook.data.model.SlotResponse
import com.example.sebook.data.model.SubmitBookingResponse
import com.example.sebook.data.model.UpdateProfileRequest
import com.example.sebook.data.model.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/changepass")
    suspend fun changePassword(@Body request: ChangePasswordRequest): ChangePasswordResponse

    @GET("auth/profile")
    suspend fun getProfile(): ProfileResponse

    @POST("auth/update")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UpdateProfileResponse

    @GET("order/ruangan")
    suspend fun getRuangan(): RuanganResponse

    @GET("order/ruangan/{id_ruangan}")
    suspend fun getDetailRuangan(@Path("id_ruangan") idRuangan: String): DetailRuanganResponse

    @GET("order/jadwal/{id_ruangan}")
    suspend fun getJadwalRuangan(
        @Path("id_ruangan") idRuangan: String
    ): JadwalResponse

    @GET("order/jadwal/edit/{id_ruangan}/{id_pengajuan}")
    suspend fun getJadwalRuanganForEdit(
        @Path("id_ruangan") idRuangan: String,
        @Path("id_pengajuan") idPengajuan: String
    ): JadwalResponse

    @GET("order/jadwal/{id_ruangan}/{tanggal_sewa}")
    suspend fun getSlotTerisi(
        @Path("id_ruangan") idRuangan: String,
        @Path("tanggal_sewa") tanggalSewa: String
    ): SlotResponse

    @GET("order/jadwal/edit/{id_ruangan}/{id_pengajuan}/{tanggal_sewa}")
    suspend fun getSlotTerisiForEdit(
        @Path("id_ruangan") idRuangan: String,
        @Path("id_pengajuan") idPengajuan: String,
        @Path("tanggal_sewa") tanggalSewa: String
    ): SlotResponse

    @POST("order/barang")
    suspend fun checkAvailableBarang(@Body request: BookingRequest): BarangResponse

    @POST("order/barang/{id_pengajuan}")
    suspend fun checkAvailableBarangForEdit(
        @Path("id_pengajuan") idPengajuan: String,
        @Body request: BookingRequest
    ): BarangResponse

    @GET("order/history")
    suspend fun getHistory(): HistoryResponse

    @GET("order/history/{id_pengajuan}")
    suspend fun getHistoryDetail(@Path("id_pengajuan") idPengajuan: String): HistoryDetailResponse

    @POST("order/delete/{id_pengajuan}")
    suspend fun deletePengajuan(@Path("id_pengajuan") idPengajuan: String): SubmitBookingResponse

    @POST("review/{id_pengajuan}")
    suspend fun submitReview(
        @Path("id_pengajuan") idPengajuan: String,
        @Body request: ReviewRequest
    ): ReviewResponse

    @GET("/review")
    suspend fun getForumReviews(): ForumReviewResponse

    @Multipart
    @POST("order/{id_ruangan}")
    suspend fun submitBooking(
        @Path("id_ruangan") idRuangan: String,
        @Part("tanggal_sewa") tanggalSewa: RequestBody,
        @Part("waktu_mulai") waktuMulai: RequestBody,
        @Part("waktu_selesai") waktuSelesai: RequestBody,
        @Part("organisasi_komunitas") organisasiKomunitas: RequestBody,
        @Part("kegiatan") kegiatan: RequestBody,
        @Part("barang_dipinjam") barangDipinjam: RequestBody,
        @Part suratPeminjaman: MultipartBody.Part
    ): SubmitBookingResponse

    @Multipart
    @POST("order/edit/{id_pengajuan}")
    suspend fun updateBooking(
        @Path("id_pengajuan") idPengajuan: String,
        @Part("tanggal_sewa") tanggalSewa: RequestBody,
        @Part("waktu_mulai") waktuMulai: RequestBody,
        @Part("waktu_selesai") waktuSelesai: RequestBody,
        @Part("organisasi_komunitas") organisasiKomunitas: RequestBody,
        @Part("kegiatan") kegiatan: RequestBody,
        @Part("barang_dipinjam") barangDipinjam: RequestBody,
        @Part suratPeminjaman: MultipartBody.Part? // Surat is nullable
    ): SubmitBookingResponse
}