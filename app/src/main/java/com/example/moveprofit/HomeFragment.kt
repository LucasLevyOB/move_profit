package com.example.moveprofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moveprofit.database.AppDatabase
import com.example.moveprofit.databinding.FragmentHomeBinding
import com.example.moveprofit.models.Exercise
import com.example.moveprofit.models.Workout
import com.example.moveprofit.models.WorkoutDay
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var appDatabase: AppDatabase;

    private lateinit var rcvMyWorkouts: RecyclerView;
//    private var currentWorkout: Workout? = null
    val appData = AppData.getInstance()

    private fun getCurrentWorkout(): Workout? {
        var currentWorkout: Workout? = null
        lifecycleScope.launch {
            val workout = appDatabase.workoutDao().selectAll()
            if (workout.size > 0) {
                val workoutDays = appDatabase.workoutDayDao().selectAll()
                for (wd in workoutDays) {
                    val exercises = appDatabase.exercisesDao().selectByWorkDay(wd.getId())
                    wd.setExercises(ArrayList(exercises))
                }
                workout[0].setDays(ArrayList(workoutDays))

                appData.setCurrentWorkout(workout[0])

                currentWorkout = workout[0]
            }
        }
        return currentWorkout;

//        val currentWorkout = Workout("id5", "Treino de treino", "Prof. Chico", "", "Treino treinável", "Hipertrofia 2");
//
//        val peito = WorkoutDay("id-1", "A", "Peito")
//        val biceps = WorkoutDay("id-2", "B", "Bíceps")
//        val triceps = WorkoutDay("id-3", "C", "Tríceps")
//
//        peito.addExercise(Exercise("id-1", "Supino Reto", 3, "12-10-08", 0, 30))
//        peito.addExercise(Exercise("id-2", "Supino Inclinado", 3, "12-10-08", 0, 45))
//        peito.addExercise(Exercise("id-3", "Supino Declinado", 3, "10", 0, 60))
//
//        currentWorkout.addDay(peito)
//        currentWorkout.addDay(biceps)
//        currentWorkout.addDay(triceps)
//
//        appData.setCurrentWorkout(currentWorkout)
//
//        return currentWorkout;
    }

    private fun displayCurrentWorkout(currentWorkout: Workout) {
        binding.cdvMessageNotCurrentWorkout.visibility = View.GONE
        binding.cdvSelectedWorkout.visibility = View.VISIBLE
        binding.tvWorkoutTitle.setText(currentWorkout.getTitle())
        binding.tvWorkoutAuthName.setText(currentWorkout.getAuth())
        binding.tvDayNextWorkouts.setText(currentWorkout.getNextDayName())
        binding.tvCounterFinishedWorkouts.setText(currentWorkout.getCountFinishedWorkouts().toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appDatabase = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "main-database")
            .enableMultiInstanceInvalidation()
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();

        val currentWorkout = if (appData.getCurrentWorkout() == null) getCurrentWorkout() else appData.getCurrentWorkout()

        if (currentWorkout != null) {
            displayCurrentWorkout(currentWorkout)
        } else {
            binding.cdvSelectedWorkout.visibility = View.GONE
            binding.cdvMessageNotCurrentWorkout.visibility = View.VISIBLE
        }

//        val listItems = ArrayList<Workout>();
//        listItems.add(Workout("id1","Teste", "Chico", "img", "Descriçao", "Hipertrofia"))
//        listItems.add(Workout("id3", "Teste 2", "Chico 2", "img 2", "Descriçao 2", "Emagrecimento"))
//        listItems.add(Workout("id4", "Teste 3", "Chico 3", "img 3", "Descriçao 3", "Recuperação"))
//        listItems.add(Workout("id5", "Teste 4", "Chico 4", "img 4", "Descriçao 4", "Hipertrofia"))

//        rcvMyWorkouts = view.findViewById(R.id.rcv_my_workouts)
//        rcvMyWorkouts = binding.rcvMyWorkouts
//        rcvMyWorkouts.layoutManager = LinearLayoutManager(context)
//        rcvMyWorkouts.setHasFixedSize(true)
//        val workoutAdapter = WorkoutItemAdapter(listItems)
//        rcvMyWorkouts.adapter = workoutAdapter
//
//        workoutAdapter.onItemClick = {
//            Log.e("tag-teste", "clickou no item")
//        }

        binding.btnNextWorkout.setOnClickListener {
            val intent = Intent(context, PerformActivity::class.java);
//            intent.putExtra("currentWorkout", currentWorkout as Serializable);

            startActivity(intent);
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}