package com.dicoding.github

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.github.databinding.ActivityMainBinding
import com.dicoding.github.fav.FavoriteActivity
import com.dicoding.github.setting.MainViewModelSetting
import com.dicoding.github.setting.SettingActivity
import com.dicoding.github.setting.SettingPreferences
import com.dicoding.github.setting.ViewModelFactorySetting
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding
    private val data = ArrayList<UserList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setTheme()
        supportActionBar?.title = "GitHub"
        showLoading(true)

        binding?.rvUser?.setHasFixedSize(true)
        val layoutManager =
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                GridLayoutManager(this, 2)
            }else{
                LinearLayoutManager(this)
            }

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding?.rvUser?.addItemDecoration(itemDecoration)
        binding?.rvUser?.layoutManager = layoutManager
        RequestData.getUserListHome("a",::addUsernameList, ::onFailureData)
    }

    @SuppressLint("ServiceCast")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater =  menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    showLoading(true)
                    RequestData.getUserListHome(query,::addUsernameList, ::onFailureData)
                    searchView.clearFocus()
                    val text = "Result : " +query
                    binding?.tvResult?.text = text
                    binding?.tvResult?.visibility = View.VISIBLE
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> {
                val toSetting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(toSetting)
                return true
            }
            R.id.fab -> {
                val toFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(toFavorite)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
//        return super.onOptionsItemSelected(item)
    }

    fun onFailureData(): Unit{
        binding?.tvNotFound?.visibility = View.VISIBLE
    }

    private fun addUsernameList(text:String, list: ArrayList<ItemsItem>): Unit{
        data.clear()
        list.forEach{
           val user = UserList(it.login, it.avatarUrl)
            data.add(user)
        }
        showRecyclerList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerList(){
        val listUserAdapter = ListUserAdapter(data)
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemCilickCallback {
            override fun onItemClikced(data: String?) {
                showSelectedUser(data)
            }
        })
        binding?.rvUser?.adapter = listUserAdapter
        showLoading(false)
    }

    private fun showSelectedUser(user: String?){
        val moveToDetailUser = Intent(this@MainActivity, DetailUser::class.java)
        moveToDetailUser.putExtra(DetailUser.EXTRA_USER, user)
        startActivity(moveToDetailUser)
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding?.progressBar?.visibility = View.VISIBLE
        }else{
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun setTheme() {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactorySetting(pref)).get(
            MainViewModelSetting::class.java
        )
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true);
        exitProcess(-1)
    }
}