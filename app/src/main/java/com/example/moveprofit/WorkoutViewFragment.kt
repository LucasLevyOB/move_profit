package com.example.moveprofit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moveprofit.database.AppDatabase
import com.example.moveprofit.databinding.FragmentWorkoutViewBinding
import com.example.moveprofit.models.Exercise
import com.example.moveprofit.models.Workout
import com.example.moveprofit.models.WorkoutDay
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CURRENT_WORKOUT = "currentWorkout"

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentWorkoutViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var appDatabase: AppDatabase;
    private lateinit var rcvWorkoutDay: RecyclerView;
    private lateinit var workoutDayAdapter: WorkoutDayItemAdapter
    private var currentWorkout: Workout? = null
    private var workoutDayList: ArrayList<WorkoutDay> = ArrayList()

    private fun showSnackbar(view: View, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun getDays() {
        workoutDayList.clear()
        val db = Firebase.firestore
        db.collection("workouts").document(currentWorkout?.getId()!!).collection("workout_day").orderBy("name").get().addOnSuccessListener { result ->
            for (document in result) {
                val workoutDay = WorkoutDay(document.id, document.data.get("name").toString(), document.data.get("muscleGroups").toString())
                workoutDay.setWorkoutId(currentWorkout!!.getId())
                workoutDayList.add(workoutDay)
            }
            workoutDayAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.d("tag-result-error", "Error getting documents: ", exception)
        }
    }

    private suspend fun getExercisesByDay(idWorkoutDay: String) {
        val db = Firebase.firestore
        db.collection("workouts").document(currentWorkout?.getId()!!).collection("workout_day").document(idWorkoutDay).collection("exercises").get().addOnSuccessListener { result ->
            for (document in result) {

                val exercise = Exercise(
                    document.id,
                    document.data.get("name").toString(),
                    document.data.get("series").toString().toInt(),
                    document.data.get("repetitions").toString(),
                    document.data.get("weight").toString().toInt(),
                    document.data.get("interval").toString().toInt()
                )
                exercise.setWorkoutDayId(idWorkoutDay)

                lifecycleScope.launch {
                    appDatabase.exercisesDao().insertAll(exercise)
                }
            }
        }.addOnFailureListener { exception ->
            Log.d("tag-result-error", "Error getting documents: ", exception)
        }.await()
    }

    private fun displayValues() {
        binding.tvWorkoutTitle.setText(currentWorkout?.getTitle())
        binding.tvWorkoutAuth.setText(currentWorkout?.getAuth())
        binding.tvWorkoutFocus.setText(currentWorkout?.getGoal())
        binding.tvWorkoutDescription.setText(currentWorkout?.getDescription())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentWorkout = it.getSerializable(CURRENT_WORKOUT) as Workout
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutViewBinding.inflate(inflater, container, false)
                // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "main-database")
            .enableMultiInstanceInvalidation()
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();

        displayValues()

        rcvWorkoutDay = binding.rcvWorkoutDays
        rcvWorkoutDay.layoutManager = LinearLayoutManager(context)
        rcvWorkoutDay.setHasFixedSize(true)
        workoutDayAdapter = WorkoutDayItemAdapter(workoutDayList, false)
        rcvWorkoutDay.adapter = workoutDayAdapter

        getDays()

        binding.btnAddCurrentWorkout.setOnClickListener {
            it.isEnabled = false
            showSnackbar(it, "Salvando dados do treino.")
            Log.e("tag-current-workout", currentWorkout!!.getTitle() + " - " + currentWorkout!!.getId())

            lifecycleScope.launch {
                appDatabase.exercisesDao().deleteAll()
                appDatabase.workoutDayDao().deleteAll()
                appDatabase.workoutDao().deleteAll()
            }

            lifecycleScope.launch {
                Log.e("tag-w", currentWorkout!!.getId())
                appDatabase.workoutDao().insertAll(currentWorkout!!)
            }

            for (wd in workoutDayList) {
                Log.e("tag-current-wd", wd.getName() + " - " + wd.getId())
                lifecycleScope.launch {
                    appDatabase.workoutDayDao().insertAll(wd)
                }

                lifecycleScope.launch {
                    getExercisesByDay(wd.getId())
                }
            }

            showSnackbar(it, "Treino salvo!")
            it.isEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param currentWorkout Parameter 1.
         * @return A new instance of fragment WorkoutViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(currentWorkout: Workout) =
            WorkoutViewFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CURRENT_WORKOUT, currentWorkout)
                }
            }
    }
}