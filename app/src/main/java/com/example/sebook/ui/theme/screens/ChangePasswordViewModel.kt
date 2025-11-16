package com.example.sebook.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.ChangePasswordRequest
import com.example.sebook.data.model.ChangePasswordResponse
import com.example.sebook.data.repository.AuthRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ChangePasswordViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _changePasswordState = MutableStateFlow<UiState<ChangePasswordResponse>>(UiState.Idle)
    val changePasswordState: StateFlow<UiState<ChangePasswordResponse>> = _changePasswordState

    fun changePassword(oldPassword: String, newPassword: String) {
        _changePasswordState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val request = ChangePasswordRequest(oldPassword = oldPassword, newPassword = newPassword)
                val response = repository.changePassword(request)
                if (response.success) {
                    _changePasswordState.value = UiState.Success(response)
                } else {
                    _changePasswordState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "Password lama tidak sesuai"
                    else -> e.message() ?: "Terjadi kesalahan HTTP"
                }
                _changePasswordState.value = UiState.Error(errorMessage)
            } catch (e: Exception) {
                _changePasswordState.value = UiState.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
            }
        }
    }
}