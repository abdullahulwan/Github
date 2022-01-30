package com.dicoding.github

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestData {
    companion object {
        private const val TAG = "MainActivity"
        private const val TOKEN = BuildConfig.KEY

        fun getUserListHome(
            querySearch: String,
            res: (text:String,list: ArrayList<ItemsItem>) -> Unit,
            fail: () -> Unit
        ) {
            val client = ApiConfig.getApiService().getUserList(querySearch, TOKEN)
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            res(querySearch, responseBody.items as ArrayList<ItemsItem>)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        fail()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                    fail()
                }
            })
        }

        fun getUser(item: String, res: (user: User) -> Unit) {
            val client = ApiConfig.getApiService().getUserDetail(item, TOKEN)
            client.enqueue(object : Callback<UserDetail> {
                override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            val user = User(
                                responseBody.nameUser,
                                responseBody.avatarUrl,
                                responseBody.company,
                                responseBody.location,
                                responseBody.login,
                                responseBody.followersUser,
                                responseBody.followingUser,
                                responseBody.userRepos
                            )
                            res(user)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }

        fun getFollowerList(querySearch: String, res: (list: ArrayList<FollowResponseItem>) -> Unit) {
            val client = ApiConfig.getApiService().getUserFollower(querySearch, TOKEN)
            client.enqueue(object : Callback<List<FollowResponseItem>> {
                override fun onResponse(call: Call<List<FollowResponseItem>>, response: Response<List<FollowResponseItem>>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            res(responseBody as ArrayList<FollowResponseItem>)
                        }
                    } else {
                        Log.e(FollowerFragment.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                    Log.e(FollowerFragment.TAG, "onFailure: ${t.message}")
                }
            })
        }

        fun getFollowingList(querySearch: String, res: (list: ArrayList<FollowResponseItem>) -> Unit) {
            val client = ApiConfig.getApiService().getUserFollowing(querySearch, TOKEN)
            client.enqueue(object : Callback<List<FollowResponseItem>> {
                override fun onResponse(call: Call<List<FollowResponseItem>>, response: Response<List<FollowResponseItem>>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            res(responseBody as ArrayList<FollowResponseItem>)
                        }
                    } else {
                        Log.e(FollowingFragment.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                    Log.e(FollowingFragment.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
}