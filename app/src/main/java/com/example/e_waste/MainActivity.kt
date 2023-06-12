package com.example.e_waste

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import com.example.e_waste.databinding.ActivityMainBinding
import com.example.e_waste.utils.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null)
            setUpBottomNav()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        setUpBottomNav()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setUpBottomNav() {
        val graphIds = listOf(
            R.navigation.home_nav,
            R.navigation.fav_nav,
            R.navigation.profile_nav
        )
        binding.bottomNav.setupWithNavController(
            graphIds,
            supportFragmentManager,
            R.id.nav_host_fragment,
            intent
        )
    }
}