package com.example.moveprofit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moveprofit.dao.ExercisesDao
import com.example.moveprofit.dao.WorkoutDao
import com.example.moveprofit.dao.WorkoutDayDao
import com.example.moveprofit.models.Exercise
import com.example.moveprofit.models.Workout
import com.example.moveprofit.models.WorkoutDay

@Database(entities = [Exercise::class, WorkoutDay::class, Workout::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exercisesDao() : ExercisesDao
    abstract fun workoutDayDao() : WorkoutDayDao
    abstract fun workoutDao() : WorkoutDao
}