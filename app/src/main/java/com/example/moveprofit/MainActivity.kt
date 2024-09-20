package com.example.moveprofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.moveprofit.database.AppDatabase
import com.example.moveprofit.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var appDatabase: AppDatabase;

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_main, fragment)
        fragmentTransaction.commit()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, LoginActivity::class.java);
        startActivity(intent);
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "main-database")
            .enableMultiInstanceInvalidation()
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();

        binding.bnvMain.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nbi_home -> replaceFragment(HomeFragment())
                R.id.nbi_create_workout -> replaceFragment(CreateWorkoutFragment())
                R.id.nbi_workouts -> replaceFragment(WorkoutsFragment())
                else -> replaceFragment(HomeFragment())
            }
            true
        }

        binding.tbMainToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.tbi_logout -> logout()
            }
            true
        }

    }

    override fun onStart() {
        super.onStart()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
        }
    }
}