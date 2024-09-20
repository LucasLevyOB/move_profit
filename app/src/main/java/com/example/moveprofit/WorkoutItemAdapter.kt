package com.example.moveprofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moveprofit.models.Workout

class WorkoutItemAdapter(private val listItems: ArrayList<Workout>): RecyclerView.Adapter<WorkoutItemAdapter.ViewHolder>() {
    var onItemClick : ((Workout) -> Unit)? = null

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_workout_title)
        val tvAuth: TextView = view.findViewById(R.id.tv_workout_auth)
        val imvThumb: ImageView = view.findViewById(R.id.imv_workout_thumb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_item, parent, false);

        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = listItems[position];
        holder.tvTitle.setText(workout.getTitle())
        holder.tvAuth.setText(workout.getAuth())
//        holder.imvThumb.setBackgroundResource(holder.itemView.getResources().getIdentifier(listItems[position].thumb, "drawable", holder.itemView.getContext().getPackageName()));

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(workout)
        }
    }
}