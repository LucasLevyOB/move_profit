package com.example.moveprofit.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "workout")
class Workout(
    @PrimaryKey
    private var id: String,
    @ColumnInfo(name = "title")
    private var title: String,
    @ColumnInfo(name = "auth")
    private var auth: String,
    @ColumnInfo(name = "thumb")
    private var thumb: String,
    @ColumnInfo(name = "description")
    private var description: String,
    @ColumnInfo(name = "goal")
    private var goal: String,
): Serializable {
    @Ignore
    private var days: ArrayList<WorkoutDay> = ArrayList()
    @ColumnInfo(name = "nextDayIndex")
    private var nextDayIndex = 0
    @ColumnInfo(name = "finishedWorks")
    private var finishedWorks = 0

    fun getId(): String {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun getAuth(): String {
        return auth
    }

    fun getThumb(): String {
        return thumb
    }

    fun getDescription(): String {
        return description
    }

    fun getNextDayIndex(): Int {
        return nextDayIndex
    }

    fun setNextDayIndex(value: Int) {
        nextDayIndex = value
    }

    fun getFinishedWorks(): Int {
        return finishedWorks
    }

    fun setFinishedWorks(value: Int) {
        finishedWorks = value
    }

    fun setDays(days: ArrayList<WorkoutDay>) {
        this.days = days
    }

    fun addDay(day: WorkoutDay) {
        this.days.add(day)
    }

    fun getDays(): ArrayList<WorkoutDay> {
        return days
    }

    fun getNextDay(): WorkoutDay? {
        return days.get(nextDayIndex)
    }

    fun getNextDayName(): String? {
        val nextDay = days.get(nextDayIndex) ?: return "Não Realizado"

        return nextDay.getName()
    }

    fun finishWorkout() {
        if (nextDayIndex >= days.size) {
            nextDayIndex = 0
        } else {
            nextDayIndex++;
        }
        finishedWorks++;
    }

    fun getCountFinishedWorkouts(): Int {
        return finishedWorks
    }

    fun getGoal(): String {
        return goal
    }
}
