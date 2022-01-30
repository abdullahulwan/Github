package com.dicoding.github.fav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.github.databinding.ActivityFavoriteBinding
import com.dicoding.github.fav.helper.ViewModelFactoryFav

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Favorite"

        adapter = FavoriteAdapter()
        binding?.rvUserFav?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserFav?.setHasFixedSize(true)

        val mainViewModel = obitainViewMode(this@FavoriteActivity)
        mainViewModel.getAllNotes().observe(this, { userList ->
            if (userList != null) {
                adapter.setListUsers(userList)
            }
            binding?.rvUserFav?.adapter = adapter
        })
    }

    private fun obitainViewMode(activity: AppCompatActivity): MainViewModelFavorite {
        val factory = ViewModelFactoryFav.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModelFavorite::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }
}