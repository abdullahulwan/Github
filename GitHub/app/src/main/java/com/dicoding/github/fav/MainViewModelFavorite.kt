package com.dicoding.github.fav

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.github.fav.db.UserFav
import com.dicoding.github.fav.repository.UserRepository

class MainViewModelFavorite(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    fun getAllNotes(): LiveData<List<UserFav>> = mUserRepository.getAllNotes()
}