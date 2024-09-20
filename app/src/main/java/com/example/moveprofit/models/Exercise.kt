package com.example.moveprofit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "exercises",
    foreignKeys = [
        ForeignKey(entity = WorkoutDay::class, parentColumns = ["id"], childColumns = ["workoutDayId"])
    ]
)
class Exercise(
    @PrimaryKey
    private var id: String,
    @ColumnInfo(name = "name")
    private var name: String,
    @ColumnInfo(name = "series")
    private var series: Int,
    @ColumnInfo(name = "repetitions")
    private var repetitions: String,
    @ColumnInfo(name = "weight")
    private var weight: Int,
    @ColumnInfo(name = "interval")
    private var interval: Int
): Serializable {
    @Ignore
    private var isFinished: Boolean = false
    @ColumnInfo(name = "workoutDayId")
    private var workoutDayId: String = ""

    public fun getId(): String {
        return id
    }

    public fun getWorkoutDayId(): String {
        return workoutDayId
    }

    public fun setWorkoutDayId(workoutDayId: String) {
        this.workoutDayId = workoutDayId
    }

    public fun getName(): String {
        return name
    }

    public fun getSeries(): Int {
        return series
    }

    public fun getRepetitions(): String {
        return repetitions
    }

    public fun getWeight(): Int {
        return weight
    }

    public fun setWeight(weight: Int) {
        this.weight = weight
    }

    public fun getInterval(): Int {
        return interval
    }

    public fun setFinish(value: Boolean) {
        isFinished = value
    }

    public fun isFinished(): Boolean {
        return isFinished
    }
}