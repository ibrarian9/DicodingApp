package com.app.fundamentalsubmission.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.app.fundamentalsubmission.di.api.ApiService
import com.app.fundamentalsubmission.di.database.FavoriteDao
import com.app.fundamentalsubmission.di.database.FavoriteEvent
import com.app.fundamentalsubmission.di.models.DetailEventModel
import com.app.fundamentalsubmission.di.models.EventModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao,
    private val preference: SettingPreference
){
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTheme(): LiveData<Boolean> { return preference.getThemeSet().asLiveData() }

    suspend fun saveTheme(isDarkMode: Boolean) = preference.saveThemeSet(isDarkMode)

    fun getDailyReminder(): LiveData<Boolean> { return preference.getDailyReminder().asLiveData() }

    suspend fun saveDailyReminder(isActive: Boolean) { return preference.saveDailyReminder(isActive = isActive) }

    fun getFavoriteById(id: Int): LiveData<FavoriteEvent> = favoriteDao.getFavoriteById(id)

    fun insertFavorite(favoriteEvent: FavoriteEvent) = executorService.execute { favoriteDao.insertFavorite(favoriteEvent) }

    fun deleteFavorite(favoriteEvent: FavoriteEvent) = executorService.execute { favoriteDao.deleteFavorite(favoriteEvent) }

    fun getAllFavorite(): LiveData<List<FavoriteEvent>> {
        _isLoading.postValue(true)
        return favoriteDao.getAllFavorite().apply {
            observeForever{
                _isLoading.postValue(false)
            }
        }
    }

    suspend fun getNearestEvent(): EventModel? {
        _isLoading.postValue(true)
        return try {
          val res = apiService.getNearestEvent()
          if (res.isSuccessful){
              res.body()
          } else {
              null
          }
        } catch (e: Exception){
            null
        } finally {
            _isLoading.postValue(false)
        }
    }

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

    suspend fun getDetailEvent(id: Int): DetailEventModel? {
        _isLoading.postValue(true)
        return try {
            val res = apiService.getDetailEvent(id = id)
            if (res.isSuccessful){
                res.body()
            } else {
                null
            }
        } catch (e: Exception){
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

    companion object {

        @Volatile
        var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao,
            preference: SettingPreference
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(
                    apiService = apiService,
                    favoriteDao = favoriteDao,
                    preference = preference
                )
            }.also { instance = it }
    }
}

