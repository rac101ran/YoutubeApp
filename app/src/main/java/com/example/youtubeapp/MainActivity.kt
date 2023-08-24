package com.example.youtubeapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import com.example.youtubeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(this.getSharedPreferences("com.example.youtubeapp", Context.MODE_PRIVATE).getBoolean("session",false)) {
            startActivity(Intent(this,HomePage::class.java))
        }else {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayoutHomeId, WelcomepageChoose()).commit()
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.findFragmentById(R.id.frameLayoutHomeId)
                if(fragment is SignUpFragment) {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutHomeId, WelcomepageChoose()).commit()
                }else if(fragment is LoginUser) {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutHomeId, WelcomepageChoose()).commit()
                }else {
                    this@MainActivity.finish()
                }
            }
        })

    }
}