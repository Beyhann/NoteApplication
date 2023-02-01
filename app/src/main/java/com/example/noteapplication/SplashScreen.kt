package com.task.noteapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class SplashScreen : AppCompatActivity() {

    private lateinit var splashImg:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        //hide upper action bar
        supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            //This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(i)
            // close this activity
            finish()
        }, 3000)

        splashImg = findViewById(R.id.splashImg)
        //Animations
        val splashAnimation : Animation = AnimationUtils.loadAnimation(this, R.anim.splashscreen)
        splashImg.startAnimation(splashAnimation)
    }
}