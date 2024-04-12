package com.app.novatech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ProjectAddCollaboratorsAdapter
import com.app.novatech.databinding.FragmentProjectAddCollaboratorsBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.util.CollaboratorsGetFreeController
import com.google.gson.JsonParser

class ProjectAddCollaboratorsFragment : Fragment() {
    private var _binding: FragmentProjectAddCollaboratorsBinding? = null
    private val binding get() = _binding!!
    private val collaboratorsList = ArrayList<Collaborator>()
    private lateinit var adapter: ProjectAddCollaboratorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectAddCollaboratorsBinding.inflate(inflater, container, false)
        setRecyclerView()
        getCollaboratorsList()
        setSearchBar()
        setBackBtn()
        return binding.root
    }

    private fun getCollaboratorsList(){
        try{
            CollaboratorsGetFreeController.getFreeCollaboratorsAttempt {
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
        binding.projectAddCollaboratorsSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

    private fun setRecyclerView(){
        adapter = ProjectAddCollaboratorsAdapter(activity ,requireContext(), layoutInflater, collaboratorsList, arguments?.getString("name")!!)
        binding.projectAddCollaboratorsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectAddCollaboratorsRv.setHasFixedSize(true)
        binding.projectAddCollaboratorsRv.adapter = adapter
    }

    private fun setBackBtn(){
        binding.projectAddCollaboratorsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
