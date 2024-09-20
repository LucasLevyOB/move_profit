package com.example.moveprofit

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moveprofit.databinding.FragmentHomeBinding
import com.example.moveprofit.databinding.FragmentWorkoutBinding
import com.example.moveprofit.models.PerformViewModel
import com.example.moveprofit.models.Workout
import com.example.moveprofit.models.WorkoutDay

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CURRENT_WORKOUT = "currentWorkout"

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var currentWorkout: Workout? = null

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var rcvWorkoutDays: RecyclerView
    private val viewModel: PerformViewModel by activityViewModels()
    private val appData = AppData.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        currentWorkout = viewModel.currentWorkout.value
        currentWorkout = appData.getCurrentWorkout()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNextWorkout.setText(currentWorkout?.getNextDayName())
        binding.tvFocusWorkout.setText(currentWorkout?.getGoal())
        binding.tvCounterFinishedWorkouts.setText(currentWorkout?.getCountFinishedWorkouts().toString())
        viewModel.setTitle(currentWorkout!!.getTitle())

        rcvWorkoutDays = binding.rcvWorkoutDays
        rcvWorkoutDays.layoutManager = LinearLayoutManager(context)
        rcvWorkoutDays.setHasFixedSize(true)
        val workoutDayAdapter = WorkoutDayItemAdapter(currentWorkout!!.getDays() as ArrayList<WorkoutDay>, isCreateView = false)
        rcvWorkoutDays.adapter = workoutDayAdapter

        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java);

            startActivity(intent);
        }

        workoutDayAdapter.onItemClick = {
            val performListFragment = PerformListFragment();
            val bundle = Bundle()
            viewModel.setCurrentDay(it.clone())
            performListFragment.arguments = bundle
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fl_perform_view, performListFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        binding.btnNextWorkout.setOnClickListener {
            val currentWorkoutDay = currentWorkout!!.getNextDay();
            if (currentWorkoutDay != null) {
                val performListFragment = PerformListFragment();
                val bundle = Bundle()
                viewModel.setCurrentDay(currentWorkoutDay.clone())
                performListFragment.arguments = bundle
                val fragmentManager = activity?.supportFragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()

                if (fragmentTransaction != null) {
                    fragmentTransaction.replace(R.id.fl_perform_view, performListFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
        }

        if (viewModel.isRunning.value == true) {
            binding.btnNextWorkout.setText("Continuar Treino")
        } else {
            binding.btnNextWorkout.setText("Iniciar Treino")
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
         * @return A new instance of fragment WorkoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            WorkoutFragment().apply {
                arguments = Bundle().apply {
//                    putSerializable(CURRENT_WORKOUT, currentWorkout)
                }
            }
    }
}