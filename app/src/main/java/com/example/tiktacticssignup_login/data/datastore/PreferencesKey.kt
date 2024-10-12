package com.example.tiktacticssignup_login.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKey {
    val EMAIL = stringPreferencesKey("user_email")
    val EMAIL_APP_PASSWORD = stringPreferencesKey("user_email_app_password")
    val AUTH_TOKEN = stringPreferencesKey("auth_token")
}