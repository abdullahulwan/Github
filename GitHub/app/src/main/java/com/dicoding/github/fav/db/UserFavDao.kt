package com.dicoding.github.fav.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserFavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserFav)

    @Update
    fun update(user: UserFav)

    @Delete
    fun delete(user: UserFav)

    @Query("SELECT * from user ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<UserFav>>
}