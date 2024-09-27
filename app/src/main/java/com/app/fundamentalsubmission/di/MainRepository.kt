package com.app.fundamentalsubmission.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.fundamentalsubmission.di.api.ApiService
import com.app.fundamentalsubmission.di.models.Event
import com.app.fundamentalsubmission.di.models.EventModel

class MainRepository private constructor(
    private val apiService: ApiService
){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun getAllUpcomingEvent(limit: Int): EventModel? {
        _isLoading.postValue(true)
        return try {
            val res = apiService.getAllUpcomingEvent(limit = limit)
            if (res.isSuccessful) {
                res.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            null
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getAllFinishedEvent(limit: Int): EventModel? {
        _isLoading.postValue(true)
        return try {
            val res = apiService.getAllFinishedEvent(limit = limit)
            if (res.isSuccessful) {
                res.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            null
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getSearchUpcomingEvent(query: String): LiveData<EventModel> {
        val data = MutableLiveData<EventModel>()
        try {
            val res = apiService.getSearchUpcomingEvent(query = query)
            if (res.isSuccessful){
                data.postValue(res.body())
            }
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
        return data
    }

    suspend fun getSearchFinishedEvent(query: String): LiveData<EventModel> {
        val data = MutableLiveData<EventModel>()
        try {
            val res = apiService.getSearchFinishedEvent(query = query)
            if (res.isSuccessful){
                data.postValue(res.body())
            }
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
        return data
    }

    suspend fun getDetailEvent(id: Int): LiveData<Event> {
        val data = MutableLiveData<Event>()
        try {
            val res = apiService.getDetailEvent(id)
            if (res.isSuccessful){
                data.postValue(res.body()!!.event)
            }
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }
        return data
    }

    companion object {

        @Volatile
        var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService)
            }.also { instance = it }
    }
}

