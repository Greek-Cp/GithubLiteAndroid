package com.yanuar.githubliteandroid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yanuar.githubliteandroid.R
class ActivitySplashScreen : AppCompatActivity() {
    private val splashTimeOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val splashIcon = findViewById<ImageView>(R.id.splashIcon)
        val splashText = findViewById<TextView>(R.id.splashText)
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        splashIcon.startAnimation(bounceAnimation)
        splashText.startAnimation(bounceAnimation)
        Handler(Looper.getMainLooper()).postDelayed({
            splashIcon.startAnimation(fadeOutAnimation)
            splashText.startAnimation(fadeOutAnimation)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 500)
        }, splashTimeOut - 500)
    }
}