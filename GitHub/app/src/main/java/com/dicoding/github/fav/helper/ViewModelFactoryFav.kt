package com.dicoding.github.fav.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.github.fav.MainViewModelFavorite
import com.dicoding.github.fav.UserAddViewModel

class ViewModelFactoryFav private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory(){
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryFav? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactoryFav {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryFav::class.java) {
                    INSTANCE = ViewModelFactoryFav(application)
                }
            }
            return  INSTANCE as ViewModelFactoryFav
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModelFavorite::class.java)) {
            return MainViewModelFavorite(mApplication) as T
        } else if (modelClass.isAssignableFrom(UserAddViewModel::class.java)) {
            return UserAddViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}