package com.example.moveprofit

import com.example.moveprofit.models.Workout

class AppData private constructor() {
    private var currentWorkout: Workout? = null

    companion object {
        @Volatile private var instance: AppData? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppData().also { instance = it }
        }
    }

    fun setCurrentWorkout(workout: Workout) {
        currentWorkout = workout
    }

    fun getCurrentWorkout(): Workout? {
        return currentWorkout
    }
}