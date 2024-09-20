package com.example.moveprofit.models

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PerformViewModel : ViewModel() {
    private val mutableTitle = MutableLiveData<String>()
    val currentTitle: LiveData<String> get() = mutableTitle
    private val mutableIsRunning = MutableLiveData<Boolean>(false)
    val isRunning: LiveData<Boolean> get() = mutableIsRunning
    private val mutableDay = MutableLiveData<WorkoutDay>()
    val currentDay: LiveData<WorkoutDay> = mutableDay
//    private val mutableWorkout = MutableLiveData<Workout>()
//    val currentWorkout: LiveData<Workout> = mutableWorkout

    fun setTitle(title: String) {
        mutableTitle.value = title
    }

    fun toggleIsRunning() {
        mutableIsRunning.value = !mutableIsRunning.value!!
    }

    fun setCurrentDay(day: WorkoutDay) {
        mutableDay.value = day
    }

//    fun setCurrentWorkout(workout: Workout) {
//        mutableWorkout.value = workout
//    }
}