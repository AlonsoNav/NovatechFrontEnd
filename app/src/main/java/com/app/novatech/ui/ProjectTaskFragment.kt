package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.novatech.adapters.TaskListAdapter
import com.app.novatech.databinding.FragmentProjectTaskBinding
import com.app.novatech.model.Tasks
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectTaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentProjectTaskBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentProjectTaskBinding.inflate(inflater, container, false)
        setViewPager()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewPager(){
        val taskList = arrayListOf(
            Tasks("Task1", "Description1", 18, "User1", "Todo"),
            Tasks("Task2", "Description2", 8, "User2", "Doing"),
            Tasks("Task3", "Description3", 28, "User3", "Done"))
        val adapter = TaskListAdapter(taskList, childFragmentManager, lifecycle)
        binding.projectTaskViewPager.adapter = adapter
        TabLayoutMediator(binding.projectTaskTabLayout, binding.projectTaskViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "To Do"
                1 -> "Doing"
                2 -> "Done"
                else -> "Unknown"
            }
        }.attach()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}