package com.app.fundamentalsubmission.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.fundamentalsubmission.di.MainRepository
import com.app.fundamentalsubmission.di.database.FavoriteEvent

class FavoriteViewModel(private val repo: MainRepository): ViewModel() {

    val isLoading: LiveData<Boolean> get() = repo.isLoading

    fun getAllFavorite(): LiveData<List<FavoriteEvent>> {
        return repo.getAllFavorite()
    }
}
