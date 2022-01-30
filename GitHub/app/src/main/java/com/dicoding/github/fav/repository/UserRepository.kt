package com.dicoding.github.fav.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.github.fav.db.UserFav
import com.dicoding.github.fav.db.UserFavDao
import com.dicoding.github.fav.db.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserFavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllNotes(): LiveData<List<UserFav>> = mUserDao.getAllNotes()

    fun insert(user: UserFav) {
        executorService.execute { mUserDao.insert(user)}
    }
    fun delete(user: UserFav) {
        executorService.execute { mUserDao.delete(user)}
    }
    fun update(user: UserFav) {
        executorService.execute { mUserDao.update(user)}
    }
}