package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ProjectAdapter
import com.app.novatech.databinding.FragmentProjectBinding
import com.app.novatech.model.ProjectForList
import com.app.novatech.model.User
import com.app.novatech.util.ProjectsListController
import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ProjectFragment : Fragment() {
    private val projectsList = ArrayList<ProjectForList>()
    private lateinit var adapter: ProjectAdapter
    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        if(projectsList.isNotEmpty())
            projectsList.clear()
        menu = requireActivity() as Menu
        user = (arguments?.getSerializable("user") as? User)!!
        setRecyclerView()
        getProjectsList()
        setSearchBar()
        setChart()
        setAddBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProjectsList(){
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        if(user.admin){
            try{
                ProjectsListController.getProjectsListAttempt {
                    val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                    for(jsonObject in jsonArray){
                        projectsList.add(ProjectForList(
                            jsonObject.asJsonObject.get("nombre").asString,
                            jsonObject.asJsonObject.get("responsable").asString,
                            jsonObject.asJsonObject.get("estado").asString,
                            format.parse(jsonObject.asJsonObject.get("fechaInicio").asString)!!,
                            format.parse(jsonObject.asJsonObject.get("fechaFin").asString)!!))
                    }
                    activity?.runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }catch(_: Exception){

            }
        }else if(user.proyecto != null){
            binding.projectsAdd.visibility = View.GONE
            projectsList.add(ProjectForList(
                user.proyecto!!.nombre,
                user.proyecto!!.responsable,
                user.proyecto!!.estado,
                format.parse(user.proyecto!!.fechaInicio)!!,
                format.parse(user.proyecto!!.fechaFin)!!
            ))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setRecyclerView(){
        adapter = ProjectAdapter(menu, activity ,requireContext(), layoutInflater, projectsList, user.admin, user.correo)
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

    private fun setChart(){
        binding.projectsChart.setOnClickListener{
            val barChartFragment = BarChartFragment()
            val bundle = Bundle().apply {
                putSerializable("user", user)
            }
            barChartFragment.arguments = bundle
            menu.replaceFragment(barChartFragment)
        }
    }

    private fun setAddBtn(){
        binding.projectsAdd.setOnClickListener {
            menu.replaceFragment(ProjectAddFragment())
        }
    }
}