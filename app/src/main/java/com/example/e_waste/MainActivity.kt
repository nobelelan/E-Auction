package com.example.e_waste

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.example.e_waste.databinding.ActivityMainBinding
import com.example.e_waste.utils.ExtensionFunctions.showToast
import com.example.e_waste.utils.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null)
            setUpBottomNav()

        binding.navigationView.setNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.about_project ->{
                    AlertDialog.Builder(this)
                        .setTitle("About this Project")
                        .setMessage(getString(R.string.about_project))
                        .setPositiveButton("Ok"){_,_->}
                        .create().show()
                    true
                }
                R.id.about_supervisor ->{
                    AlertDialog.Builder(this)
                        .setTitle("Afroza Islam")
                        .setMessage("Lecturer at CCN University of Science & Technology")
                        .setPositiveButton("Ok"){_,_->}
                        .create().show()
                    true
                }
                R.id.about_us ->{
                    AlertDialog.Builder(this)
                        .setTitle("About Us")
                        .setMessage("Ripan Chandra Bhowmik\n" +
                                "ID:111219013\n" +
                                "\n" +
                                "Omar Faruk\n" +
                                "ID: 111219005\n" +
                                "\n" +
                                "Sohel Ahmed\n" +
                                "ID:111219010")
                        .setPositiveButton("Ok"){_,_->}
                        .create().show()
                    true
                }
                else -> false
            }
        }
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

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
            binding.drawerLayout.closeDrawer(binding.navigationView)
        } else {
            super.onBackPressed()
        }
    }
}