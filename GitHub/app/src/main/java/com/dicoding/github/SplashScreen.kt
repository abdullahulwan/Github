package com.dicoding.github

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide

class SplashScreen : AppCompatActivity() {
    private lateinit var imgLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        imgLogo = findViewById(R.id.logo)
        Glide.with(this).load(R.drawable.github_logo).into(imgLogo)

        Handler(Looper.getMainLooper()).postDelayed({
            val gotoMain = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(gotoMain)
            finish()
        }, delaySplashScreen)

    }
    companion object{
        private const val delaySplashScreen = 3000L
    }
}