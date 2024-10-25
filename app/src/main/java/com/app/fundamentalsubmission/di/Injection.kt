package com.app.fundamentalsubmission.di

import android.content.Context
import android.widget.Toast
import com.app.fundamentalsubmission.di.api.ApiCore
import com.app.fundamentalsubmission.di.database.FavoriteDatabase

object Injection {
    fun provideRepo(context: Context): MainRepository {
        val apiService = ApiCore().getApiService()
        val pref = SettingPreference.getInstance(dataStore = context.dataStore)
        val db = FavoriteDatabase.getInstance(context = context)
        val dao = db.favoriteDao()
        return MainRepository.getInstance(apiService, dao, pref)
    }

    fun messageError(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}