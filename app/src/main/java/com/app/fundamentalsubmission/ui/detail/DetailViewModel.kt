package com.app.fundamentalsubmission.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.fundamentalsubmission.di.MainRepository
import com.app.fundamentalsubmission.di.database.FavoriteEvent
import com.app.fundamentalsubmission.di.models.DetailEventModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: MainRepository): ViewModel() {

    fun getFavoriteById(id: Int): LiveData<FavoriteEvent> {
        return repo.getFavoriteById(id = id)
    }

    fun deleteFavorite(favoriteEvent: FavoriteEvent){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFavorite(favoriteEvent)
        }
    }

    fun insertFavorite(favoriteEvent: FavoriteEvent){
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertFavorite(favoriteEvent)
        }
    }

    private val _detailEvent= MutableLiveData<DetailEventModel?>()
    val detailEvent: LiveData<DetailEventModel?> get() = _detailEvent

    fun getDetailEvent(id: Int) {
        viewModelScope.launch {
            val result = repo.getDetailEvent(id)
            _detailEvent.postValue(result)
        }
    }
}