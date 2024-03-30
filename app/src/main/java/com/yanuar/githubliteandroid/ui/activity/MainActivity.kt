package com.yanuar.githubliteandroid.ui.activity

import SettingAppPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.yanuar.githubliteandroid.databinding.ActivityMainBinding
import com.yanuar.githubliteandroid.ui.fragment.SearchUserFragment
import dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedFragment: Fragment = SearchUserFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        SettingAppPreferences.getInstance(this.dataStore).getThemeSetting().asLiveData().observe(this, { isDarkMode ->
            val mode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        })
    }

}