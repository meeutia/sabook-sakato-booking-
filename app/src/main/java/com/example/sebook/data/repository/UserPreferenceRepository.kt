package com.example.sebook.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferenceRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    fun getSession(): Flow<String?> {
        return dataStore.data.map {
            it[TOKEN_KEY]
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")

        @Volatile
        private var INSTANCE: UserPreferenceRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferenceRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferenceRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}