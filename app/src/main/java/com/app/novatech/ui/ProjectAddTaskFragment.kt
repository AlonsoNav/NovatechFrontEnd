package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.novatech.databinding.FragmentProjectAddTaskBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.util.CollaboratorsGetFreeController
import com.app.novatech.util.ProjectCollaboratorsGetController
import com.app.novatech.util.ResourcesAddController
import com.app.novatech.util.TasksAddController
import com.google.gson.JsonParser

class ProjectAddTaskFragment : Fragment() {
    private var _binding: FragmentProjectAddTaskBinding? = null
    private val binding get() = _binding!!
    private val collaboratorsList = ArrayList<String>()
    private lateinit var collaboratorsAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectAddTaskBinding.inflate(inflater, container, false)
        setResponsibleSpinner()
        getCollaboratorsList()
        setBackBtn()
        setBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCollaboratorsList(){
        try{
            ProjectCollaboratorsGetController.getProjectCollaboratorsAttempt(arguments?.getString("name")!!) {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    collaboratorsList.add(
                        jsonObject.asJsonObject.get("correo").asString
                    )
                }
                activity?.runOnUiThread {
                    collaboratorsAdapter.notifyDataSetChanged()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setResponsibleSpinner(){
        collaboratorsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, collaboratorsList)
        collaboratorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectAddTaskResponsible.adapter = collaboratorsAdapter
    }

    private fun setBtn(){
        binding.projectAddTaskBtn.setOnClickListener {
            val pupOk = Dialog(requireContext())
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = com.app.novatech.R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                TasksAddController.tasksAddAttempt(arguments?.getString("name")!!,
                    binding.projectAddTaskName.text.toString(),
                    binding.projectAddTaskDescription.text.toString(),
                    binding.projectAddTaskResponsible.selectedItem.toString(),
                    binding.projectAddTaskStoryPoints.text.toString().toIntOrNull()){
                    val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                    activity?.runOnUiThread {
                        bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                    }
                }
            }catch (e : Exception){
                bindingPupOk.pupYesNoDescription.text = getString(com.app.novatech.R.string.failed_server)
            }
            okBtn.setOnClickListener{
                pupOk.dismiss()
            }
            pupOk.show()
        }
    }

    private fun setBackBtn(){
        binding.projectAddTaskBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}