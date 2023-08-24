package com.example.youtubeapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import com.example.youtubeapp.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {
    private lateinit var binding : ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityHomePageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutId, AllVideosFragment()).commit()

        binding.explorerText2.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayoutId, WatchLaterFragment()).commit()
        }

        binding.explorerText1.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.frameLayoutId, AllVideosFragment()).commit()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Handle logout here
                // For example, show a confirmation dialog or perform logout actions
                // After logout, navigate to the login screen
                // You can start your LoginActivity using an Intent
                // startActivity(Intent(this, LoginActivity::class.java))
                // Finish the current activity

                this.getSharedPreferences("com.example.youtubeapp",Context.MODE_PRIVATE).edit().remove("session").apply()

                startActivity(Intent(this , MainActivity::class.java))

                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}