package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.novatech.R
import com.app.novatech.databinding.FragmentProjectEditTaskBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Tasks
import com.app.novatech.util.ProjectCollaboratorsGetController
import com.app.novatech.util.TasksEditController
import com.google.gson.JsonParser

class ProjectEditTaskFragment : Fragment() {
    private var _binding: FragmentProjectEditTaskBinding? = null
    private val binding get() = _binding!!
    private val statusItems = arrayOf("To do", "Doing", "Done")
    private val collaboratorsList = ArrayList<String>()
    private lateinit var collaboratorsAdapter: ArrayAdapter<String>
    private lateinit var task: Tasks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectEditTaskBinding.inflate(inflater, container, false)
        setResponsibleSpinner()
        setStatusSpinner()
        getCollaboratorsList()
        setBackBtn()
        setBtn()
        return binding.root
    }

    private fun setResponsibleSpinner(){
        collaboratorsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, collaboratorsList)
        collaboratorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectEditTaskResponsible.adapter = collaboratorsAdapter
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
                    setData()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setStatusSpinner(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectEditTaskStatus.adapter = adapter
    }

    private fun setData(){
        task= arguments?.getParcelable("task")!!
        binding.projectEditTaskName.setText(task.name)
        binding.projectEditTaskDescription.setText(task.description)
        binding.projectEditTaskStoryPoints.setText(task.storyPoints.toString())
        binding.projectEditTaskResponsible.setSelection(collaboratorsList.indexOf(task.responsible))
        binding.projectEditTaskStatus.setSelection(statusItems.indexOf(task.status))
    }

    private fun setBtn(){
        binding.projectEditTaskBtn.setOnClickListener{
            val pupYesno= Dialog(requireContext())
            val bindingPup = PopupYesnoBinding.inflate(layoutInflater)
            pupYesno.setContentView(bindingPup.root)
            pupYesno.setCancelable(true)
            pupYesno.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupYesno.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val yesBtn = bindingPup.pupYesBtn
            val noBtn = bindingPup.pupNoBtn
            bindingPup.pupYesNoDescription.text = getString(R.string.popup_yesno_project_individual)
            yesBtn.setOnClickListener{
                val pupOk = Dialog(requireContext())
                val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
                pupOk.setContentView(bindingPupOk.root)
                pupOk.setCancelable(true)
                pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
                pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
                val okBtn = bindingPupOk.pupOkBtn
                try{
                    TasksEditController.tasksEditAttempt(arguments?.getString("name")!!,
                        task.name,
                        binding.projectEditTaskName.text.toString(),
                        binding.projectEditTaskDescription.text.toString(),
                        binding.projectEditTaskResponsible.selectedItem.toString(),
                        binding.projectEditTaskStoryPoints.text.toString().toIntOrNull(),
                        binding.projectEditTaskStatus.selectedItem.toString()){
                        val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                        activity?.runOnUiThread {
                            bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                        }
                    }
                }catch (e : Exception){
                    bindingPupOk.pupYesNoDescription.text = getString(R.string.failed_server)
                }
                okBtn.setOnClickListener{
                    pupOk.dismiss()
                }
                pupYesno.dismiss()
                pupOk.show()
            }
            noBtn.setOnClickListener {
                pupYesno.dismiss()
            }
            pupYesno.show()
        }
    }

    private fun setBackBtn(){
        binding.projectEditTaskBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}