package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.CollaboratorAdapter
import com.app.novatech.databinding.FragmentCollaboratorsAdminBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.util.CollaboratorsListController
import com.google.gson.JsonParser

class CollaboratorsAdminFragment : Fragment() {
    private val collaboratorsList = ArrayList<Collaborator>()
    private lateinit var adapter: CollaboratorAdapter
    private var _binding: FragmentCollaboratorsAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollaboratorsAdminBinding.inflate(inflater, container, false)
        if(collaboratorsList.isNotEmpty())
            collaboratorsList.clear()
        menu = requireActivity() as Menu
        getCollaboratorsList()
        setRecyclerView()
        setSearchBar()
        setAddBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCollaboratorsList(){
        try{
            CollaboratorsListController.getCollaboratorsListAttempt {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    collaboratorsList.add(Collaborator(jsonObject.asJsonObject.get("nombre").asString,
                        jsonObject.asJsonObject.get("correo").asString,
                        jsonObject.asJsonObject.get("cedula").asString))
                }
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setRecyclerView(){
        adapter = CollaboratorAdapter(activity ,requireContext(), layoutInflater, collaboratorsList)
        binding.collaboratorsAdminRv.layoutManager = LinearLayoutManager(requireContext())
        binding.collaboratorsAdminRv.setHasFixedSize(true)
        binding.collaboratorsAdminRv.adapter = adapter
    }

    private fun setSearchBar(){
        binding.collaboratorsAdminSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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
        binding.collaboratorsAdminAdd.setOnClickListener {
            menu.replaceFragment(CollaboratorsAddFragment())
        }
    }
}