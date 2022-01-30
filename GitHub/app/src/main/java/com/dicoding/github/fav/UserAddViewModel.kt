package com.dicoding.github.fav

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.github.fav.db.UserFav
import com.dicoding.github.fav.repository.UserRepository

class UserAddViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: UserRepository = UserRepository(application)
    fun insert(user: UserFav) {
        mNoteRepository.insert(user)
    }

    fun update(user: UserFav) {
        mNoteRepository.update(user)
    }

    fun delete(user: UserFav) {
        mNoteRepository.delete(user)
    }
}