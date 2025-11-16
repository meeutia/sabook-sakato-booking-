package com.example.sebook.ui.theme.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sebook.data.model.UpdateProfileRequest
import com.example.sebook.data.model.UpdateProfileResponse
import com.example.sebook.data.repository.AuthRepository
import com.example.sebook.ui.login.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(private val repository: AuthRepository) : ViewModel() {

    data class ProfileUiState(
        val fullName: String = "",
        val username: String = "",
        val email: String = "",
        val phoneNumber: String = "",
        val isLoading: Boolean = true,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<UpdateProfileResponse>>(UiState.Idle)
    val updateState: StateFlow<UiState<UpdateProfileResponse>> = _updateState

    init {
        getProfile()
    }

    private fun getProfile() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                if (response.success) {
                    val profile = response.profil
                    _uiState.value = _uiState.value.copy(
                        fullName = profile.nama,
                        username = profile.username,
                        email = profile.email,
                        phoneNumber = profile.nohp,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = response.message, isLoading = false)
                }
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message(), isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Terjadi kesalahan", isLoading = false)
            }
        }
    }

    fun updateFullName(value: String) {
        _uiState.value = _uiState.value.copy(fullName = value)
    }

    fun updateUsername(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun updatePhoneNumber(value: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = value)
    }

    fun updateProfile() {
        val currentState = _uiState.value
        val request = UpdateProfileRequest(
            nama = currentState.fullName,
            username = currentState.username,
            email = currentState.email,
            nohp = currentState.phoneNumber
        )

        _updateState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateProfile(request)
                if (response.success) {
                    _updateState.value = UiState.Success(response)
                } else {
                    _updateState.value = UiState.Error(response.message)
                }
            } catch (e: HttpException) {
                _updateState.value = UiState.Error(e.message() ?: "Terjadi kesalahan HTTP")
            } catch (e: Exception) {
                _updateState.value = UiState.Error(e.message ?: "Terjadi kesalahan tidak diketahui")
            }
        }
    }
}