package com.example.sebook.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.LoginRequest
import com.example.sebook.data.model.LoginResponse
import com.example.sebook.data.repository.LoginRepository
import com.example.sebook.data.repository.UserPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class UiState<out T: Any?> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T: Any>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
}

class LoginViewModel(
    private val repository: LoginRepository, 
    private val userPreferenceRepository: UserPreferenceRepository
    ) : ViewModel() {

    private val _loginState = MutableStateFlow<UiState<LoginResponse>>(UiState.Idle)
    val loginState: StateFlow<UiState<LoginResponse>> = _loginState.asStateFlow()

    fun login(loginRequest: LoginRequest) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.login(loginRequest)
                if (response.success) {
                    response.token?.let { userPreferenceRepository.saveSession(it) }
                    _loginState.value = UiState.Success(response)
                } else {
                    _loginState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val jsonAdapter = com.example.sebook.data.network.ApiConfig.moshi.adapter(LoginResponse::class.java)
                val errorResponse = errorBody?.let { jsonAdapter.fromJson(it) }
                _loginState.value = UiState.Error(errorResponse?.message ?: "Terjadi kesalahan")
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun onLoginErrorShown() {
        _loginState.value = UiState.Idle
    }
}
