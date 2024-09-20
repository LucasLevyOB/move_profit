package com.example.moveprofit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.moveprofit.databinding.ActivityPerformBinding
import com.example.moveprofit.models.PerformViewModel
import com.example.moveprofit.models.Workout
import com.google.firebase.auth.FirebaseAuth

class PerformActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerformBinding
    private val viewModel: PerformViewModel by viewModels()

    private var initialTime: Long = 0
    private var handler: Handler? = null
    private val MILLIS_IN_SEC = 1000L
    private val SECS_IN_MIN = 60

    private final val runnable: Runnable = object : Runnable {
        override fun run() {
            if (viewModel.isRunning.value == true) {
                val seconds: Long = (System.currentTimeMillis() - initialTime) / MILLIS_IN_SEC
                binding.tvWorkoutTime.setText(String.format("%02d:%02d", seconds / SECS_IN_MIN, seconds % SECS_IN_MIN))
                handler!!.postDelayed(this, MILLIS_IN_SEC)
            }
        }
    }

    private fun playTimer() {
        initialTime = System.currentTimeMillis()
        handler!!.postDelayed(runnable, MILLIS_IN_SEC)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.currentTitle.observe(this, Observer { title ->
            binding.tvWorkoutName.setText(title)
        })
        viewModel.isRunning.observe(this, Observer { isRunning ->
            if (isRunning) {
                playTimer()
            }
        })

//        val currentWorkout = intent.extras!!.get("currentWorkout") as Workout
//        viewModel.setCurrentWorkout(currentWorkout)

        val workoutFragment = WorkoutFragment();
//        val bundle = Bundle()
//        bundle.putSerializable("currentWorkout", currentWorkout)
//        workoutFragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_perform_view, workoutFragment)
        fragmentTransaction.commit()

        handler = Handler(Looper.getMainLooper())

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