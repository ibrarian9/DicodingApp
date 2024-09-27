package com.app.fundamentalsubmission.di

import com.app.fundamentalsubmission.di.api.ApiCore

object Injection {
    fun provideRepo(): MainRepository {
        val apiService = ApiCore().getApiService()
        return MainRepository.getInstance(apiService)
    }
}