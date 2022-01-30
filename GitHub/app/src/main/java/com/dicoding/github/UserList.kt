package com.dicoding.github

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserList(
    var name: String?,
    var photo: String?
) : Parcelable