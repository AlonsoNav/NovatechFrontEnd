package com.app.novatech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ProjectCollaboratorsAdapter
import com.app.novatech.databinding.FragmentProjectCollaboratorsBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.util.ProjectCollaboratorsGetController
import com.google.gson.JsonParser

class ProjectCollaboratorsFragment : Fragment() {
    private var _binding: FragmentProjectCollaboratorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu
    private val collaboratorsList = ArrayList<Collaborator>()
    private lateinit var adapter: ProjectCollaboratorsAdapter
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectCollaboratorsBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        isAdmin = arguments?.getBoolean("isAdmin")!! or
                ((arguments?.getString("responsible")!! == arguments?.getString("user")!!))
        if(collaboratorsList.isNotEmpty())
            collaboratorsList.clear()
        setRecyclerView()
        getCollaboratorsList()
        setAddBtn()
        setSearchBar()
        setBackBtn()
        return binding.root
    }

    private fun getCollaboratorsList(){
        try{
            ProjectCollaboratorsGetController.getProjectCollaboratorsAttempt(arguments?.getString("name")!!) {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    val correo = jsonObject.asJsonObject.get("correo").asString
                    if(!correo.equals(arguments?.getString("responsible"))){
                        collaboratorsList.add(Collaborator(
                            jsonObject.asJsonObject.get("nombre").asString,
                            correo,
                            jsonObject.asJsonObject.get("cedula").asString
                            )
                        )
                    }
                }
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setSearchBar(){
        binding.projectCollaboratorsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
        if(!isAdmin)
            binding.projectCollaboratorsAdd.visibility = View.GONE
        binding.projectCollaboratorsAdd.setOnClickListener{
            val projectAddCollaboratorsFragment = ProjectAddCollaboratorsFragment()
            val bundle = Bundle().apply {
                putString("name", arguments?.getString("name")!!)
            }
            projectAddCollaboratorsFragment.arguments = bundle
            menu.replaceFragment(projectAddCollaboratorsFragment)
        }
    }

    private fun setRecyclerView(){
        adapter = ProjectCollaboratorsAdapter(activity ,requireContext(), layoutInflater, collaboratorsList, isAdmin, arguments?.getString("name")!!)
        binding.projectCollaboratorsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectCollaboratorsRv.setHasFixedSize(true)
        binding.projectCollaboratorsRv.adapter = adapter
    }

    private fun setBackBtn(){
        binding.projectCollaboratorsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
