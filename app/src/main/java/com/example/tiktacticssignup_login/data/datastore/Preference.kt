package com.example.tiktacticssignup_login.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


class PreferenceManager(
    private val context: Context
) {
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.EMAIL] = email
        }
    }

    suspend fun saveUserEmailAppPassword(emailAppPassword: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.EMAIL_APP_PASSWORD] = emailAppPassword
        }
    }

    suspend fun saveAuthToken(authToken: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.AUTH_TOKEN] = authToken
        }
    }

    fun getUserEmail(): Flow<String?> = context.dataStore.data.map { preferences -> preferences[PreferencesKey.EMAIL] }

    fun getUserEmailAppPassword(): Flow<String?> = context.dataStore.data.map { preferences -> preferences[PreferencesKey.EMAIL_APP_PASSWORD] }

    fun getAuthToken(): Flow<String?> = context.dataStore.data.map { preferences -> preferences[PreferencesKey.AUTH_TOKEN] }
}