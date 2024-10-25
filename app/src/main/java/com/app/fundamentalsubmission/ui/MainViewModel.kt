package com.app.fundamentalsubmission.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.fundamentalsubmission.di.MainRepository
import com.app.fundamentalsubmission.di.models.EventModel
import kotlinx.coroutines.launch

class MainViewModel(private val repo: MainRepository): ViewModel() {

    val isLoading: LiveData<Boolean> get() = repo.isLoading

    private var hasLoadedUpcoming = false
    private var hasLoadedFinishing = false

    private val _upcomingEvents = MutableLiveData<EventModel?>()
    val upcomingEvents: LiveData<EventModel?> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<EventModel?>()
    val finishedEvent: LiveData<EventModel?> get() = _finishedEvents

    fun loadAllUpcomingEvent(limit: Int) {
        if (!hasLoadedUpcoming) {
            viewModelScope.launch {
                val result = repo.getAllUpcomingEvent(limit = limit)
                _upcomingEvents.postValue(result)
                hasLoadedUpcoming = true
            }
        }
    }

    fun loadAllFinishedEvent(limit: Int) {
        if (!hasLoadedFinishing) {
            viewModelScope.launch {
                val result = repo.getAllFinishedEvent(limit = limit)
                _finishedEvents.postValue(result)
                hasLoadedFinishing = true
            }
        }
    }

    suspend fun getSearchUpcomingEvent(q: String): LiveData<EventModel> {
        return repo.getSearchUpcomingEvent(query = q).asFlow().asLiveData()
    }

    suspend fun getSearchFinishedEvent(q: String): LiveData<EventModel> {
        return repo.getSearchFinishedEvent(query = q).asFlow().asLiveData()
    }

    fun getThemeSet(): LiveData<Boolean> {
        return repo.getTheme()
    }

}