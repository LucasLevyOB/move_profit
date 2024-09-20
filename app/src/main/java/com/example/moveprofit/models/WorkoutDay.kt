package com.example.moveprofit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import java.io.Serializable

@Entity(
    tableName = "workout_day",
    foreignKeys = [
        ForeignKey(entity = Workout::class, parentColumns = ["id"], childColumns = ["workoutId"])
    ]
)
class WorkoutDay(
    @PrimaryKey
    private var id: String,
    @ColumnInfo(name = "name")
    private var name: String,
    @ColumnInfo(name = "muscleGroups")
    private var muscleGroups: String,
): Serializable, Cloneable {
    @Ignore
    private var exercises: ArrayList<Exercise> = ArrayList()
    @ColumnInfo(name = "workoutId")
    private var workoutId: String = ""

    fun getId(): String {
        return id
    }

    fun getWorkoutId(): String {
        return workoutId
    }

    fun setWorkoutId(id: String) {
        workoutId = id
    }

    fun getName(): String {
        return name
    }

    fun getMuscleGroups(): String {
        return muscleGroups
    }

    fun getExercises(): ArrayList<Exercise> {
        return exercises
    }

    fun setExercises(ex: ArrayList<Exercise>) {
        exercises = ex
    }

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }

    public override fun clone(): WorkoutDay {
        return Gson().toJson(this).let { Gson().fromJson(it, WorkoutDay::class.java) }
    }
}