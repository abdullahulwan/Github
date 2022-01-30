package com.dicoding.github

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String?,
    var photo: String?,
    var company: String?,
    var location: String?,
    var username: String?,
    var follower: Int,
    var following: Int,
    var repository: String?
) : Parcelable