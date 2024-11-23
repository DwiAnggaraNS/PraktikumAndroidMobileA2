package com.example

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply()
    }

    fun savePassword(password: String) {
        sharedPreferences.edit().putString(KEY_PASSWORD, password).apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "") ?: ""
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
