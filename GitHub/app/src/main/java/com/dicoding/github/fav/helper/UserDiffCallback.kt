package com.dicoding.github.fav.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.github.fav.db.UserFav

class UserDiffCallback(private val mOldUserList: List<UserFav>, private val mNewUserList: List<UserFav>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldUserList[oldItemPosition]
        val newUser = mNewUserList[newItemPosition]
        return oldUser.username == newUser.username && oldUser.avUrl == newUser.avUrl
    }

}