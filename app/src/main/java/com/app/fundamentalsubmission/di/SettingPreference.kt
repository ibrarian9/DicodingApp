package com.app.fundamentalsubmission.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getThemeSet(): Flow<Boolean> {
        return dataStore.data.map {
            it[THEME] ?: false
        }
    }

    suspend fun saveThemeSet(isDarkModeActive: Boolean) {
        dataStore.edit {
            it[THEME] = isDarkModeActive
        }
    }

    fun getDailyReminder(): Flow<Boolean> {
        return dataStore.data.map {
            it[DAILY_REMINDER] ?: false
        }
    }

    suspend fun saveDailyReminder(isActive: Boolean) {
        dataStore.edit {
            it[DAILY_REMINDER] = isActive
        }
    }

    companion object {
        private val THEME = booleanPreferencesKey("SetTheme")
        private val DAILY_REMINDER = booleanPreferencesKey("setDailyReminder")

        @Volatile
        private var INSTANCE: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}