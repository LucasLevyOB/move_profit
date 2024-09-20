package com.example.moveprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveprofit.models.Workout
import com.example.moveprofit.models.WorkoutDay

class WorkoutDayItemAdapter(private val listItems: ArrayList<WorkoutDay>, private val isCreateView: Boolean?): RecyclerView.Adapter<WorkoutDayItemAdapter.ViewHolder>() {
    var onItemClick : ((WorkoutDay) -> Unit)? = null

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_workout_day_name)
        val tvGroups: TextView = view.findViewById(R.id.tv_workout_day_groups)
        val tvCountExercises: TextView = view.findViewById(R.id.tv_workout_day_count_exercises)
        val btnActionItem: Button = view.findViewById(R.id.btn_action_workout_day_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_day_item, parent, false);

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutDay = listItems[position];
        holder.tvName.setText(workoutDay.getName())
        holder.tvGroups.setText(workoutDay.getMuscleGroups())
        holder.tvCountExercises.setText("${workoutDay.getExercises().size} Exercícios")
        if (isCreateView == true) {
            holder.btnActionItem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_delete_outline)
        }

//        holder.itemView.setOnClickListener {
//            onItemClick?.invoke(workoutDay)
//        }
        holder.btnActionItem.setOnClickListener {
            onItemClick?.invoke(workoutDay)
        }
    }
}