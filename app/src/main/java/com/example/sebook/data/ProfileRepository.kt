package com.example.sebook.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore(name = "profile_prefs")

class ProfileRepository(private val context: Context) {

    private val dataStore = context.profileDataStore

    private object Keys {
        val FULL_NAME = stringPreferencesKey("full_name")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val IMAGE_URI = stringPreferencesKey("image_uri")
    }

    val profileFlow: Flow<Profile> = dataStore.data.map { prefs ->
        Profile(
            fullName = prefs[Keys.FULL_NAME] ?: "",
            username = prefs[Keys.USERNAME] ?: "",
            email = prefs[Keys.EMAIL] ?: "",
            imageUri = prefs[Keys.IMAGE_URI]
        )
    }

    suspend fun saveProfile(profile: Profile) {
        dataStore.edit { prefs ->
            prefs[Keys.FULL_NAME] = profile.fullName
            prefs[Keys.USERNAME] = profile.username
            prefs[Keys.EMAIL] = profile.email
            if (profile.imageUri != null) {
                prefs[Keys.IMAGE_URI] = profile.imageUri
            } else {
                prefs.remove(Keys.IMAGE_URI)
            }
        }
    }

    suspend fun logout() {
        dataStore.edit { it.clear() }
    }
}
