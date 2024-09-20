package com.example.moveprofit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moveprofit.databinding.FragmentWorkoutsBinding
import com.example.moveprofit.models.Workout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!

    private lateinit var rcvMyWorkouts: RecyclerView;
    private lateinit var workoutAdapter: WorkoutItemAdapter
    private var workoutsList: ArrayList<Workout> = ArrayList()

    private fun findAllWorkouts() {
        val db = Firebase.firestore
        binding.pbLoadingWorkouts.visibility = View.VISIBLE
        workoutsList.clear()
        db.collection("workouts").get().addOnSuccessListener { result ->
            for (document in result) {
                Log.e("tag-result", "${document.id} - ${document.data}")
                val workout = Workout(document.id, document.data.get("title").toString(), document.data.get("auth").toString(), document.data.get("thumb").toString(), document.data.get("description").toString(), document.data.get("goal").toString())
                workoutsList.add(workout)
            }
            workoutAdapter.notifyDataSetChanged()
            binding.pbLoadingWorkouts.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Log.d("tag-result-error", "Error getting documents: ", exception)
        }



//        db.collection("users").whereEqualTo("email", "chuico@gmail.com").get().addOnSuccessListener { result ->
//            for (document in result) {
//                Log.e("tag-result", "${document.id} - ${document.data}")
//            }
//        }.addOnFailureListener { exception ->
//            Log.d("tag-result-error", "Error getting documents: ", exception)
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findAllWorkouts()

        rcvMyWorkouts = binding.rcvWorkouts
        rcvMyWorkouts.layoutManager = LinearLayoutManager(context)
        rcvMyWorkouts.setHasFixedSize(true)
        workoutAdapter = WorkoutItemAdapter(workoutsList)
        rcvMyWorkouts.adapter = workoutAdapter

        workoutAdapter.onItemClick = {
            Log.e("tag-teste", "clickou no item")
            val workoutViewFragment = WorkoutViewFragment();
            val bundle = Bundle()
            bundle.putSerializable("currentWorkout", it)
            workoutViewFragment.arguments = bundle
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.fl_main, workoutViewFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
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
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WorkoutsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WorkoutsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}