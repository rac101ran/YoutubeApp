package com.example.youtubeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.youtubeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutId, AllVideosFragment()).commit()

        binding.explorerText2.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayoutId, WatchLaterFragment()).commit()
        }

        binding.explorerText1.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayoutId, AllVideosFragment()).commit()
        }

    }
}