package com.app.fundamentalsubmission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.fundamentalsubmission.di.Injection
import com.app.fundamentalsubmission.di.MainRepository
import com.app.fundamentalsubmission.ui.MainViewModel

class ViewModelsFactory(private val repo: MainRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repo) as T
            }
            else -> throw IllegalArgumentException("Uknown ViewModel Class " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelsFactory? = null
        @JvmStatic
        fun getInstance(): ViewModelsFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelsFactory::class.java) {
                    INSTANCE = ViewModelsFactory(Injection.provideRepo())
                }
            }
            return INSTANCE as ViewModelsFactory
        }
    }
}