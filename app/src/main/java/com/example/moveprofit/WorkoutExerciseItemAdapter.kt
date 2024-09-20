package com.example.moveprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveprofit.models.Exercise

class WorkoutExerciseItemAdapter(private val listItems: ArrayList<Exercise>): RecyclerView.Adapter<WorkoutExerciseItemAdapter.ViewHolder>() {
    var onItemClick : ((Exercise) -> Unit)? = null

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_exercise_name)
        val tvSeries: TextView = view.findViewById(R.id.tv_exercise_series)
        val tvRepetitions: TextView = view.findViewById(R.id.tv_exercise_repetitions)
        val tvWeight: TextView = view.findViewById(R.id.tv_exercise_weight)
//        val imvThumb: ImageView = view.findViewById(R.id.imv_exercise_thumb)
        val cbxCompleted: CheckBox = view.findViewById(R.id.cbx_exercise_completed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false);

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = listItems[position];
        holder.tvName.setText(exercise.getName())
        holder.tvSeries.setText(exercise.getSeries().toString())
        holder.tvRepetitions.setText(exercise.getRepetitions())
        holder.tvWeight.setText(exercise.getWeight().toString())
//        holder.imvThumb.setBackgroundResource(holder.itemView.getResources().getIdentifier(listItems[position].thumb, "drawable", holder.itemView.getContext().getPackageName()));
        holder.cbxCompleted.isChecked = exercise.isFinished()

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(exercise)
        }
    }
}