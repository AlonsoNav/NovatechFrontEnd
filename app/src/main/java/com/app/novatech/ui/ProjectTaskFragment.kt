package com.app.novatech.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.novatech.adapters.TaskListAdapter
import com.app.novatech.databinding.FragmentProjectTaskBinding
import com.app.novatech.model.Tasks
import com.app.novatech.util.TasksGetController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonParser

class ProjectTaskFragment : Fragment() {
    private var _binding: FragmentProjectTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu
    private lateinit var adapter: TaskListAdapter
    private val taskList = ArrayList<Tasks>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectTaskBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        if(taskList.isNotEmpty())
            taskList.clear()
        setViewPager()
        getTask()
        setAddBtn()
        setBackBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTask(){
        try{
            TasksGetController.getTasksAttempt(arguments?.getString("name")!!) {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    taskList.add(
                        Tasks(
                            jsonObject.asJsonObject.get("nombre").asString,
                            jsonObject.asJsonObject.get("descripcion").asString,
                            jsonObject.asJsonObject.get("storyPoints").asInt,
                            jsonObject.asJsonObject.get("responsable").asString,
                            jsonObject.asJsonObject.get("estado").asString
                        )
                    )
                }
                activity?.runOnUiThread {
                    setViewPager()
                }
            }
        }catch(_: Exception){

        }
    }

    private fun setViewPager(){
        adapter = TaskListAdapter(taskList, childFragmentManager, lifecycle, arguments?.getString("name")!!,
            arguments?.getString("responsible")!!, arguments?.getString("user")!!, arguments?.getBoolean("isAdmin")!!)
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

    private fun setAddBtn(){
        if(!arguments?.getBoolean("isAdmin")!! and (arguments?.getString("user")!! != arguments?.getString(
                "responsible"
            )!!)
        ){
            binding.projectTaskAddBtn.visibility = View.GONE
        }
        binding.projectTaskAddBtn.setOnClickListener {
            val taskAddFragment = ProjectAddTaskFragment()
            val bundle = Bundle().apply {
                putString("name", arguments?.getString("name")!!)
            }
            taskAddFragment.arguments = bundle
            menu.replaceFragment(taskAddFragment)
        }
    }

    private fun setBackBtn(){
        binding.projectTaskBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}