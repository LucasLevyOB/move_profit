package com.example.moveprofit

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moveprofit.database.AppDatabase
import com.example.moveprofit.databinding.FragmentPerformExerciseBinding
import com.example.moveprofit.databinding.StepperItemBinding
import com.example.moveprofit.models.Exercise
import com.example.moveprofit.models.PerformViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CURRENT_EXERCISE = "currentExercise"
private const val EXERCISES_LIST = "exercisesList"

/**
 * A simple [Fragment] subclass.
 * Use the [PerformExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerformExerciseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var currentExercise: Exercise? = null
    private var exercisesList: ArrayList<Exercise>? = null
    private val viewModel: PerformViewModel by activityViewModels()
    private var currentSerie: Int = 0
    private var stepsItemsSeries: ArrayList<View> = ArrayList()
    private lateinit var appDatabase: AppDatabase;

    private var _binding: FragmentPerformExerciseBinding? = null
    private val binding get() = _binding!!

    private var countDown: CountDownTimer? = null

    private fun finishWorkout() {
        val appData = AppData.getInstance()
        appData.getCurrentWorkout()!!.finishWorkout()
        appDatabase.workoutDao().update(appData.getCurrentWorkout()!!)
        viewModel.toggleIsRunning()
        val intent = Intent(context, MainActivity::class.java);
        intent.putExtra("message", "Treino Finalizado!");

        startActivity(intent);
    }

    private fun nextExercise() {
        currentSerie = 0
        val currentExerciseIndex = exercisesList!!.indexOf(currentExercise)
        var nextNotFinished = -1

        exercisesList!!.forEachIndexed{ index, exercise ->
            if (nextNotFinished == -1 && index > currentExerciseIndex && !exercise.isFinished()) {
                nextNotFinished = index
            }
        }

        val finishedWorkout: Boolean = exercisesList!!.find { !it.isFinished() } == null

        if (nextNotFinished > -1) {
            currentExercise = exercisesList!![nextNotFinished]
            setValuesOnDisplay()
            mountStepper()
        } else if (nextNotFinished == -1 && !finishedWorkout) {
            AlertDialog.Builder(requireContext())
                .setTitle("Finalizar Treino")
                .setMessage("Deseja finalizar o treino sem realizar todos os exercícios?")
                .setPositiveButton("Finalizar", DialogInterface.OnClickListener{ dialog, id -> Log.e("tag-finalizar-treino-cancelar", "continuar treino") })
                .setNegativeButton("Cancelar", DialogInterface.OnClickListener{ dialog, id -> Log.e("tag-finalizar-treino-cancelar", "continuar treino") })
                .create()
                .show()
        } else if (nextNotFinished == -1 && finishedWorkout) {
            val appData = AppData.getInstance()
            appData.getCurrentWorkout()!!.finishWorkout()
            appDatabase.workoutDao().update(appData.getCurrentWorkout()!!)
            viewModel.toggleIsRunning()
            val intent = Intent(context, MainActivity::class.java);
            intent.putExtra("message", "Treino Finalizado!");

            startActivity(intent);
        }

    }

    private fun nextSerie() {
        if (currentSerie < stepsItemsSeries.size - 1) {
            stepsItemsSeries[currentSerie].findViewById<LinearLayout>(R.id.ll_stepper_item_circle).background = ContextCompat.getDrawable(requireContext(), R.drawable.background_circle_green)
            currentSerie++
            stepsItemsSeries[currentSerie].findViewById<LinearLayout>(R.id.ll_stepper_item_circle).background = ContextCompat.getDrawable(requireContext(), R.drawable.background_circle_primary)
        } else {
            val index = viewModel.currentDay.value!!.getExercises().indexOf(currentExercise)
            viewModel.currentDay.value!!.getExercises().get(index).setFinish(true)
            nextExercise()
        }
        binding.btnControllerExercise.setText("Realizado")
    }

    private fun countDownCreate(final: Long): CountDownTimer {
        val countDown = object : CountDownTimer(final, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnControllerExercise.setText((millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                binding.btnControllerExercise.setText("Próximo")
            }
        }
        return countDown
    }

    private fun setValuesOnDisplay() {
        binding.tvWorkoutWeight.setText("${currentExercise!!.getWeight().toString()} kg")
        binding.tvWorkoutRepetitions.setText(currentExercise!!.getRepetitions())
        viewModel.setTitle(currentExercise!!.getName())
    }

    private fun mountStepper() {
        binding.llStepperWrapper.removeAllViews()
        stepsItemsSeries.clear()

        val series = currentExercise!!.getSeries()
        val repetitions = currentExercise!!.getRepetitions().split("-")

        for (i in 0..< series) {
            val stepperItem: View = layoutInflater.inflate(R.layout.stepper_item, null)
            val mainStepperValue: TextView = stepperItem.findViewById(R.id.tv_stepper_item_value)
            mainStepperValue.setText((i + 1).toString())
            val subStepperValue: TextView = stepperItem.findViewById(R.id.tv_stepper_item_sub_value)
            if (repetitions.size == series) {
                subStepperValue.setText(repetitions[i])
            } else {
                subStepperValue.setText(repetitions[0])
            }
            if (i == 0) {
                stepperItem.findViewById<LinearLayout>(R.id.ll_stepper_item_circle).background = ContextCompat.getDrawable(requireContext(), R.drawable.background_circle_primary)
            }
            binding.llStepperWrapper.addView(stepperItem)
            stepsItemsSeries.add(stepperItem)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentExercise = it.getSerializable(CURRENT_EXERCISE) as Exercise
            exercisesList = it.getSerializable(EXERCISES_LIST) as ArrayList<Exercise>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPerformExerciseBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appDatabase = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "main-database")
            .enableMultiInstanceInvalidation()
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();

        setValuesOnDisplay()

        mountStepper()

        binding.btnReturnToList.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.popBackStack()
        }

        binding.btnPreviousExercise.setOnClickListener {
            val index = exercisesList!!.indexOf(currentExercise)
            val newIndex = index - 1;
            if (newIndex >= 0) {
                currentExercise = exercisesList!![newIndex]
                setValuesOnDisplay()
            }
        }

        binding.btnNextExercise.setOnClickListener {
            nextExercise()
        }

        binding.btnControllerExercise.setOnClickListener { it ->
            val currentButton: Button = it.findViewById(R.id.btn_controller_exercise)

            if (currentButton.text.equals("Realizado")) {
                countDown = countDownCreate((currentExercise!!.getInterval() * 1000).toLong())
                countDown!!.start()
            } else if (currentButton.text.equals("Próximo")) {
                nextSerie()
            } else {
                countDown!!.cancel()
                countDown = null
                nextSerie()
            }
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
         * @param currentExercise Parameter 1.
         * @param exercisesList Parameter 1.
         * @return A new instance of fragment PerformExerciseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(currentExercise: Exercise, exercisesList: ArrayList<Exercise>) =
            PerformExerciseFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CURRENT_EXERCISE, currentExercise)
                    putSerializable(EXERCISES_LIST, exercisesList)
                }
            }
    }
}