package com.example.sebook.ui.theme.auth.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.RegisterRequest
import com.example.sebook.data.model.RegisterResponse
import com.example.sebook.data.network.ApiConfig
import com.example.sebook.data.repository.RegisterRepository
import com.example.sebook.data.repository.UserPreferenceRepository
import com.example.sebook.data.repository.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val response: RegisterResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun registerUser(
        nama: String,
        email: String,
        password: String,
        nohp: String,
        username: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val response = repository.register(
                    RegisterRequest(
                        nama = nama,
                        email = email,
                        password = password,
                        nohp = nohp,
                        username = username
                    )
                )
                if (response.success) {
                    _registerState.value = RegisterState.Success(response)
                } else {
                    _registerState.value = RegisterState.Error(response.message)
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}

class RegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val userPreferenceRepository = UserPreferenceRepository.getInstance(context.dataStore)
            val apiService = ApiConfig.getApiService(userPreferenceRepository)
            val repository = RegisterRepository(apiService)
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
