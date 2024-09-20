package com.example.moveprofit.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moveprofit.models.Workout

@Dao
interface WorkoutDao {
    @Insert
    fun insertAll(vararg workout: Workout)

    @Query("DELETE FROM workout")
    fun deleteAll()

    @Query("SELECT * FROM workout")
    fun selectAll(): List<Workout>

    @Update
    fun update(workout: Workout)
}