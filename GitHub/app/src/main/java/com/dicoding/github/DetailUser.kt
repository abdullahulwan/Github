package com.dicoding.github

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.github.databinding.ActivityDetailUserBinding
import com.dicoding.github.fav.MainViewModelFavorite
import com.dicoding.github.fav.UserAddViewModel
import com.dicoding.github.fav.db.UserFav
import com.dicoding.github.fav.helper.ViewModelFactoryFav
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class DetailUser : AppCompatActivity() {

    private var _activityDetailUserBinding: ActivityDetailUserBinding? = null
    private val binding get() = _activityDetailUserBinding
    private lateinit var userAddViewModel: UserAddViewModel

    private var repository:String? = null
    private var deletedUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityDetailUserBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        userAddViewModel = obtainViewModel(this@DetailUser)

        intent.getStringExtra(EXTRA_USER)?.let { RequestData.getUser(it, ::addData) }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserAddViewModel {
        val factoty = ViewModelFactoryFav.getInstance(activity.application)
        return ViewModelProvider(activity, factoty).get(UserAddViewModel::class.java)
    }

    fun addData(user: User): Unit{
        binding?.imgUser?.let {
            Glide.with(this)
                .load(user.photo)
                .placeholder(R.drawable.logo)
                .error(Glide.with(this).load(R.drawable.logo))
                .circleCrop()
                .into(it)
        }

        binding?.imgUser?.contentDescription = "Foto " + user.name
        binding?.tvName?.text = user.name
        binding?.tvUsernameUser?.text = user.username
        binding?.tvUserCompany?.text =
            if (user.company != null && user.company!!.isNotEmpty()) {
                user.company
            } else {
                "-"
            }
        binding?.tvUserLocation?.text =
            if (user.location != null && user.location!!.isNotEmpty()) {
                user.location
            } else {
                "-"
            }
        binding?.tvUserRepository?.text = user.repository

        repository = user.repository.toString()

        var userFavorite = UserFav()
        user.username?.let {
            IsUserExist(it, fun (user:UserFav) :Unit {
                userFavorite = user
                deletedUser = true
                binding?.fabAdd?.setImageResource(R.drawable.ic_fav_fill)
            })
        }

        binding?.fabAdd?.setOnClickListener {
            if(deletedUser){
                userAddViewModel.delete(userFavorite)
                binding?.fabAdd?.setImageResource(R.drawable.ic_fav_border)
                showToast(getString(R.string.deleted))
                deletedUser = false
            }else{
                userFavorite.avUrl = user.photo
                userFavorite.username = user.username
                userAddViewModel.insert(userFavorite)
                binding?.fabAdd?.setImageResource(R.drawable.ic_fav_fill)
                showToast(getString(R.string.added))
                deletedUser = true
            }
        }

//        Tablayout
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.getUsername(user.username.toString())
        binding?.viewPager?.adapter = sectionsPagerAdapter

        binding?.tabs?.let {
            binding?.viewPager?.let { it1 ->
                TabLayoutMediator(it, it1) { tab, position ->
                    val count =
                        if(position == 0){
                            shortNumber(user.follower)
                        }else{
                            shortNumber(user.following)
                        }
                    tab.text = "(${count}) " + resources.getString(TAB_TITLES[position])
                }.attach()
            }
        }

        supportActionBar?.elevation = 0f
    }

    private fun shortNumber(number: Number): String? {
        val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
        val numValue = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.00").format(
                numValue / 10.0.pow((base * 3).toDouble())
            ) + suffix[base]
        } else {
            DecimalFormat().format(numValue)
        }
    }

    fun onClick(view: android.view.View) {
        when (view.id) {
            R.id.tv_user_repository -> {
                val goToWeb = Intent(Intent.ACTION_VIEW, Uri.parse(repository))
                startActivity(goToWeb)
            }
        }
    }

    private fun obitainViewModeGet(activity: AppCompatActivity): MainViewModelFavorite {
        val factory = ViewModelFactoryFav.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModelFavorite::class.java)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun IsUserExist(user :String, res: (fav: UserFav)-> Unit){
        val mainViewModel = obitainViewModeGet(this@DetailUser)
        mainViewModel.getAllNotes().observe(this, { userList ->
            if (userList != null) {
                userList.forEach {
                    if (user.equals(it.username)){
                        res(UserFav(it.id, it.avUrl, it.username))
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDetailUserBinding = null
    }

    companion object {
        const val EXTRA_USER = "extra_user"

        @StyleRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}