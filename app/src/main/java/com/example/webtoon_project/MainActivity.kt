package com.example.webtoon_project

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.webtoon_project.Retrofit.INodeJS
import com.example.webtoon_project.databinding.ActivityMainBinding
import com.example.webtoon_project.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_mypage
            )
        )

        val recommendedWebtoons: ArrayList<INodeJS.Webtoon> =
            intent.getParcelableArrayListExtra("recommendedWebtoons") ?: ArrayList()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val bundle = Bundle().apply {
            putParcelableArrayList("recommendedWebtoons", recommendedWebtoons)
        }
        navController.navigate(R.id.navigation_home, bundle)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }


}