package com.example.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView: BottomNavigationView =findViewById(R.id.nav_view)
        val navController=findNavController(R.id.nav_host_fragment)

        val appConfiguration= AppBarConfiguration(
            setOf(R.id.navigation_home,R.id.navigation_search,R.id.navigation_favorites)
        )
        setupActionBarWithNavController(navController,appConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController=findNavController(R.id.nav_host_fragment)
        return  navController.navigateUp() || super.onSupportNavigateUp()


}}