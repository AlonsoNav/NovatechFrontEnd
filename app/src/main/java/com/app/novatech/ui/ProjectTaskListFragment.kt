package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.TaskAdapter
import com.app.novatech.databinding.FragmentProjectTaskListBinding
import com.app.novatech.model.Tasks

class ProjectTaskListFragment : Fragment() {
    private lateinit var filterTasksList: ArrayList<Tasks>
    private var _binding: FragmentProjectTaskListBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectTaskListBinding.inflate(inflater, container, false)
        arguments?.let{
            filterTasksList = it.getParcelableArrayList("taskList")!!
        }
        menu = requireActivity() as Menu
        setRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView(){
        binding.projectTaskRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectTaskRv.setHasFixedSize(true)
        binding.projectTaskRv.adapter = TaskAdapter(menu, filterTasksList, arguments?.getString("name")!!,
            arguments?.getBoolean("isAdmin")!!, arguments?.getString("responsible")!!,
            arguments?.getString("user")!!)
    }
}