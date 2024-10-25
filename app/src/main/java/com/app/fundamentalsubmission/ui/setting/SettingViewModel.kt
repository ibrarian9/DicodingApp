package com.app.fundamentalsubmission.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.fundamentalsubmission.di.MainRepository
import com.app.fundamentalsubmission.di.models.EventModel
import kotlinx.coroutines.launch

class SettingViewModel(private val repo: MainRepository): ViewModel() {

    private val _nearestEvent = MutableLiveData<EventModel?>()
    val nearestEvent: LiveData<EventModel?> get() = _nearestEvent

    fun getAllNearestEvent() {
        viewModelScope.launch {
            val result = repo.getNearestEvent()
            _nearestEvent.postValue(result)
        }
    }

    fun getTheme(): LiveData<Boolean> {
        return repo.getTheme()
    }

    suspend fun saveThemeSet(isDarkMode: Boolean){
        return repo.saveTheme(isDarkMode)
    }

    fun getDailyReminder(): LiveData<Boolean> {
        return repo.getDailyReminder()
    }

    suspend fun saveDailyReminder(isActive: Boolean) {
        return repo.saveDailyReminder(isActive)
    }


}