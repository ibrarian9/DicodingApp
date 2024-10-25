package com.app.fundamentalsubmission

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.fundamentalsubmission.di.Injection
import com.app.fundamentalsubmission.di.MainRepository
import com.app.fundamentalsubmission.ui.MainViewModel
import com.app.fundamentalsubmission.ui.detail.DetailViewModel
import com.app.fundamentalsubmission.ui.favorite.FavoriteViewModel
import com.app.fundamentalsubmission.ui.setting.SettingViewModel

class ViewModelsFactory(private val repo: MainRepository): ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repo) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repo) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repo) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Uknown ViewModel Class " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelsFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelsFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelsFactory::class.java) {
                    INSTANCE = ViewModelsFactory(Injection.provideRepo(context))
                }
            }
            return INSTANCE as ViewModelsFactory
        }
    }
}