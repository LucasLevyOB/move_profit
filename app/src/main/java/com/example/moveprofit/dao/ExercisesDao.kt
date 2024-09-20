package com.example.moveprofit.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.moveprofit.models.Exercise

@Dao
interface ExercisesDao {
    @Insert
    fun insertAll(vararg exercise: Exercise)

    @Query("DELETE FROM exercises")
    fun deleteAll()

    @Query("SELECT * FROM exercises WHERE workoutDayId = :workDayId")
    fun selectByWorkDay(workDayId: String): List<Exercise>
}