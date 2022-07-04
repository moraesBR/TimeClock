package com.materdei.timeclock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.materdei.timeclock.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        with (this) {
            navController = findNavController(R.id.myNavHostFragment)

            /*appBarConfiguration = AppBarConfiguration(
                navController.graph
            )*/

            /*NavigationUI.setupActionBarWithNavController(
                this,
                navController,
                appBarConfiguration
            )*/

            NavigationUI.setupActionBarWithNavController(
                this,
                navController
            )
        }
        supportActionBar?.hide()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}