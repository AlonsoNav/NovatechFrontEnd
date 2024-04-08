package com.app.novatech.ui

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ProjectAdapter
import com.app.novatech.databinding.FragmentProjectBinding
import com.app.novatech.model.ProjectForList
import com.app.novatech.util.ProjectsListController
import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale
import java.util.TimeZone

class ProjectFragment : Fragment() {
    private val projectsList = ArrayList<ProjectForList>()
    private lateinit var adapter: ProjectAdapter
    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        if(projectsList.isNotEmpty())
            projectsList.clear()
        menu = requireActivity() as Menu
        getProjectsList()
        setRecyclerView()
        setSearchBar()
        setAddBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProjectsList(){
        try{
            ProjectsListController.getProjectsListAttempt {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                format.timeZone = TimeZone.getTimeZone("UTC")
                for(jsonObject in jsonArray){
                    projectsList.add(ProjectForList(
                        jsonObject.asJsonObject.get("nombre").asString,
                        "example@example.cr", // TODO: get the responsible and status correctly
                        "Active",
                        format.parse(jsonObject.asJsonObject.get("fechaInicio").asString)!!,
                        format.parse(jsonObject.asJsonObject.get("fechaFin").asString)!!))
                }
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }catch(_: Exception){

        }
    }

    private fun setRecyclerView(){
        adapter = ProjectAdapter(menu, activity ,requireContext(), layoutInflater, projectsList)
        binding.projectsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectsRv.setHasFixedSize(true)
        binding.projectsRv.adapter = adapter
    }

    private fun setSearchBar(){
        binding.projectsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setAddBtn(){
        binding.projectsAdd.setOnClickListener {
            menu.replaceFragment(ProjectAddFragment())
        }
    }
}