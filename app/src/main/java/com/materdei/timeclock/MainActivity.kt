package com.materdei.timeclock

import android.content.Context
import android.location.GnssStatus
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.*
import com.materdei.timeclock.databinding.ActivityMainBinding
import com.materdei.timeclock.viewmodels.*


class MainActivity : AppCompatActivity(){

    /*private val permissionId = 1000
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var permissionViewModel: PermissionHandlerAndroid
    private lateinit var locationViewModel: BkpLocationViewModel*/
    /*private val latRef: Double = -1.3566939
    private val lonRef: Double = -48.3934582*/
    /*private val latRef: Double = -1.431333
    private val lonRef: Double = -48.475374*/

    /* Coordination
    my house: canPunchCard(-1.3566939,-48.3934582)
    mater dei: canPunchCard(-1.431333,-48.475374)
    */

    /*private lateinit var locationBuilder: LocationBuilder*/

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