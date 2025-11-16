package com.example.sebook.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sebook.data.network.ApiConfig
import com.example.sebook.data.repository.AuthRepository
import com.example.sebook.data.repository.CheckBarangRepository
import com.example.sebook.data.repository.ForumRepository
import com.example.sebook.data.repository.HistoryRepository
import com.example.sebook.data.repository.LoginRepository
import com.example.sebook.data.repository.ReviewRepository
import com.example.sebook.data.repository.RuanganRepository
import com.example.sebook.data.repository.UserPreferenceRepository
import com.example.sebook.data.repository.dataStore
import com.example.sebook.ui.login.LoginViewModel
import com.example.sebook.ui.theme.screens.ChangePasswordViewModel
import com.example.sebook.ui.theme.screens.ProfileViewModel
import com.example.sebook.ui.theme.screens.booking.BookingViewModel
import com.example.sebook.ui.theme.screens.booking.CheckBarangViewModel
import com.example.sebook.ui.theme.screens.forum.ForumViewModel
import com.example.sebook.ui.theme.screens.informasi.DetailRuanganViewModel
import com.example.sebook.ui.theme.screens.informasi.RuanganViewModel
import com.example.sebook.ui.theme.screens.riwayat.ReviewViewModel
import com.example.sebook.ui.theme.screens.riwayat.RiwayatPengajuanViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val userPreferenceRepository: UserPreferenceRepository by lazy {
        UserPreferenceRepository.getInstance(context.dataStore)
    }

    private val apiService by lazy {
        ApiConfig.getApiService(userPreferenceRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                val loginRepository = LoginRepository(apiService)
                LoginViewModel(loginRepository, userPreferenceRepository) as T
            }
            modelClass.isAssignableFrom(RuanganViewModel::class.java) -> {
                val ruanganRepository = RuanganRepository(apiService)
                RuanganViewModel(ruanganRepository) as T
            }
            modelClass.isAssignableFrom(DetailRuanganViewModel::class.java) -> {
                val ruanganRepository = RuanganRepository(apiService)
                DetailRuanganViewModel(ruanganRepository) as T
            }
            modelClass.isAssignableFrom(BookingViewModel::class.java) -> {
                val checkBarangRepository = CheckBarangRepository(apiService)
                BookingViewModel(checkBarangRepository) as T
            }
            modelClass.isAssignableFrom(CheckBarangViewModel::class.java) -> {
                val checkBarangRepository = CheckBarangRepository(apiService)
                CheckBarangViewModel(checkBarangRepository) as T
            }
            modelClass.isAssignableFrom(RiwayatPengajuanViewModel::class.java) -> {
                val historyRepository = HistoryRepository(apiService)
                RiwayatPengajuanViewModel(historyRepository) as T
            }
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                val reviewRepository = ReviewRepository(apiService)
                ReviewViewModel(reviewRepository) as T
            }
            modelClass.isAssignableFrom(ForumViewModel::class.java) -> {
                val forumRepository = ForumRepository(apiService)
                ForumViewModel(forumRepository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                val authRepository = AuthRepository(apiService)
                ChangePasswordViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                val authRepository = AuthRepository(apiService)
                ProfileViewModel(authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}