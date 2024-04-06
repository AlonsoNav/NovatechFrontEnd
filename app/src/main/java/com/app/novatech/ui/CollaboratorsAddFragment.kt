package com.app.novatech.ui

import CollaboratorsAddController
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.novatech.R
import com.app.novatech.databinding.FragmentCollaboratorsAddBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.util.ProjectsListController
import com.google.gson.JsonParser

class CollaboratorsAddFragment : Fragment() {
    private var _binding: FragmentCollaboratorsAddBinding? = null
    private val binding get() = _binding!!
    private val departments = ArrayList<String>()
    private lateinit var projectAdapter: ArrayAdapter<String>
    private val projectsList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollaboratorsAddBinding.inflate(inflater, container, false)
        getProjectsList()
        setBackBtn()
        setProjectSpinner()
        setDepartmentSpinner()
        setBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProjectsList(){
        projectsList.add(getString(R.string.free))
        try{
            ProjectsListController.getProjectsListAttempt {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    projectsList.add(
                        jsonObject.asJsonObject.get("nombre").asString
                    )
                }
                activity?.runOnUiThread {
                    projectAdapter.notifyDataSetChanged()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setProjectSpinner(){
        projectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, projectsList)
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.collaboratorsAddProject.adapter = projectAdapter
    }

    private fun setDepartmentSpinner(){
        departments.add(getString(R.string.department_it))
        departments.add(getString(R.string.department_admin))
        departments.add(getString(R.string.department_accountability))
        departments.sort()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departments)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.collaboratorsAddDeparment.adapter = adapter
    }

    private fun setBackBtn(){
        binding.collaboratorsAddBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setBtn(){
        binding.collaboratorsAddBtn.setOnClickListener {
            val pupOk = Dialog(requireContext())
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                CollaboratorsAddController.collaboratorsAddAttempt(binding.collaboratorsAddId.text.toString(),
                    binding.collaboratorsAddName.text.toString(),
                    binding.collaboratorsAddEmail.text.toString(),
                    binding.collaboratorsAddDeparment.selectedItem.toString(),
                    binding.collaboratorsAddPhone.text.toString(),
                    binding.collaboratorsAddPassword.text.toString(),
                    binding.collaboratorsAddProject.selectedItem.toString()){
                    val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                    activity?.runOnUiThread {
                        bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                    }
                }
            }catch(e : Exception){
                bindingPupOk.pupYesNoDescription.text = getString(R.string.failed_server)
            }
            okBtn.setOnClickListener{
                pupOk.dismiss()
            }
            pupOk.show()
        }
    }
}