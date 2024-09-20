package com.example.moveprofit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moveprofit.databinding.FragmentPerformListBinding
import com.example.moveprofit.databinding.FragmentWorkoutBinding
import com.example.moveprofit.models.Exercise
import com.example.moveprofit.models.PerformViewModel
import com.example.moveprofit.models.WorkoutDay

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CURRENT_WORKOUT_DAY = "currentWorkoutDay"


/**
 * A simple [Fragment] subclass.
 * Use the [PerformListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerformListFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var currentWorkoutDay: WorkoutDay? = null
    lateinit var rcvWorkoutExercises: RecyclerView
    private val viewModel: PerformViewModel by activityViewModels()


    private var _binding: FragmentPerformListBinding? = null
    private val binding get() = _binding!!

    fun switchView(currentWorkoutDay: WorkoutDay, exercise: Exercise) {
        val performExerciseFragment = PerformExerciseFragment();
        val bundle = Bundle()
        bundle.putSerializable("currentExercise", exercise)
        bundle.putSerializable("exercisesList", currentWorkoutDay.getExercises())
        performExerciseFragment.arguments = bundle
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.fl_perform_view, performExerciseFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            if (viewModel.isRunning.value == false) {
                viewModel.toggleIsRunning()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            currentWorkoutDay = it.getSerializable(CURRENT_WORKOUT_DAY) as WorkoutDay

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerformListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentWorkoutDay : WorkoutDay? = viewModel.currentDay.value

        viewModel.setTitle(currentWorkoutDay!!.getName())

        if (currentWorkoutDay != null && currentWorkoutDay!!.getExercises().size > 0) {
            rcvWorkoutExercises = binding.rcvWorkoutExercises
            rcvWorkoutExercises.layoutManager = LinearLayoutManager(context)
            rcvWorkoutExercises.setHasFixedSize(true)
            val workoutExerciseAdapter = WorkoutExerciseItemAdapter(currentWorkoutDay!!.getExercises())
            rcvWorkoutExercises.adapter = workoutExerciseAdapter

            workoutExerciseAdapter.onItemClick = {
                switchView(currentWorkoutDay, it)
            }
        }

        binding.btnBackToHomeWorkout.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.popBackStack()
        }

        binding.btnStartWorkout.setOnClickListener {
            val list = currentWorkoutDay.getExercises()
            val firstExerciseNotFinished = list.find { !it.isFinished() }
            if (firstExerciseNotFinished != null) {
                switchView(currentWorkoutDay, firstExerciseNotFinished)
            } else {
                switchView(currentWorkoutDay, list[0])
            }
        }

        if (viewModel.isRunning.value == true) {
            binding.btnStartWorkout.setText("Continuar Treino")
        } else {
            binding.btnStartWorkout.setText("Iniciar Treino")
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
         * @param currentWorkoutDay Parameter 1.
         * @return A new instance of fragment PerformListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(currentWorkoutDay: WorkoutDay) =
            PerformListFragment().apply {
                arguments = Bundle().apply {
//                    putSerializable(CURRENT_WORKOUT_DAY, currentWorkoutDay)
                }
            }
    }
}