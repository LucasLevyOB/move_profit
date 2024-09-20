package com.example.moveprofit.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.moveprofit.models.WorkoutDay

@Dao
interface WorkoutDayDao {
    @Insert
    fun insertAll(vararg workoutDay: WorkoutDay)

    @Query("DELETE FROM workout_day")
    fun deleteAll()

    @Query("SELECT * FROM workout_day")
    fun selectAll(): List<WorkoutDay>
}