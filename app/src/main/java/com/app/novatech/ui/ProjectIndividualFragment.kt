package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.novatech.R
import com.app.novatech.databinding.FragmentProjectIndividualBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Project
import com.app.novatech.util.CollaboratorsGetFreeController
import com.app.novatech.util.ProjectsGetController
import com.app.novatech.util.ProjectsListController
import com.google.gson.JsonParser

class ProjectIndividualFragment : Fragment() {
    private var _binding: FragmentProjectIndividualBinding? = null
    private val binding get() = _binding!!
    private val historyFragment = ProjectLogsFragment()
    private val resourcesFragment = ProjectResourcesFragment()
    private val taskFragment = ProjectTaskFragment()
    private val meetingsFragment = ProjectMeetingsFragment()
    private val collaboratorsFragment = ProjectCollaboratorsFragment()
    private val statusItems = arrayOf("Active", "Inactive", "Completed")
    private lateinit var menu : Menu
    private lateinit var project : Project
    private lateinit var collaboratorsAdapter: ArrayAdapter<String>
    private val collaboratorsList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectIndividualBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        if(collaboratorsList.isNotEmpty())
            collaboratorsList.clear()
        setStatusSpinner()
        setResponsibleSpinner()
        setEditableTextViews()
        getCollaboratorsList()
        getProject(arguments?.getString("name")!!)
        setBackBtn()
        setHistoryBtn()
        setResourcesBtn()
        setMeetingsBtn()
        setTasksBtn()
        setCollaboratorsBtn()
        setAddBtn()
        setBarChartBtn()
        setBurndownChartBtn()
        setButton()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setResponsibleSpinner(){
        collaboratorsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, collaboratorsList)
        collaboratorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectIndividualCoordinator.adapter = collaboratorsAdapter
    }

    private fun getCollaboratorsList(){
        try{
            CollaboratorsGetFreeController.getFreeCollaboratorsAttempt {
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

    private fun getProject(name : String){
        try{
            ProjectsGetController.getProjectsAttempt(name) {
                val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                Log.d("Proyecto", jsonObject.asJsonObject.toString())
                project = Project(
                    jsonObject.asJsonObject.get("nombre").asString,
                    jsonObject.asJsonObject.get("presupuesto").asDouble,
                    jsonObject.asJsonObject.get("estado").asString,
                    jsonObject.asJsonObject.get("descripcion").asString,
                    jsonObject.asJsonObject.get("fechaInicio").asString,
                    jsonObject.asJsonObject.get("fechaFin").asString,
                    jsonObject.asJsonObject.get("responsable").asString
                )
                activity?.runOnUiThread {
                    setProjectData()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setTasksBtn(){
        binding.projectIndividualTask.setOnClickListener{
            menu.replaceFragment(taskFragment)
        }
    }

    private fun setHistoryBtn(){
        binding.projectIndividualHistory.setOnClickListener{
            menu.replaceFragment(historyFragment)
        }
    }

    private fun setResourcesBtn(){
        binding.projectIndividualResources.setOnClickListener{
            menu.replaceFragment(resourcesFragment)
        }
    }

    private fun setMeetingsBtn(){
        binding.projectIndividualMeeting.setOnClickListener{
            menu.replaceFragment(meetingsFragment)
        }
    }

    private fun setBarChartBtn(){
        binding.projectBarChart.setOnClickListener{
            menu.replaceFragment(ProjectBarChartFragment())
        }
    }

    private fun setBurndownChartBtn(){
        binding.projectBurndownChart.setOnClickListener{
            menu.replaceFragment(ProjectBurndownChartFragment())
        }
    }

    private fun setAddBtn(){
        binding.projectIndividualAddPerson.setOnClickListener{
            menu.replaceFragment(ProjectAddCollaboratorsFragment())
        }
    }

    private fun setCollaboratorsBtn(){
        binding.projectIndividualCollaborators.setOnClickListener{
            val projectName = project.nombre
            val collaboratorsFragment = ProjectCollaboratorsFragment.newInstance(projectName)
            menu.replaceFragment(collaboratorsFragment)
        }
    }

    private fun setStatusSpinner(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectIndividualStatus.adapter = adapter
        binding.projectIndividualStatus.setSelection(statusItems.indexOf("Active"))
    }

    private fun setEditableTextViews(){
        binding.projectIndividualDescription.setOnClickListener {
            binding.projectIndividualDescription.visibility = View.GONE
            binding.projectIndividualDescriptionEt.visibility = View.VISIBLE
            binding.projectIndividualBtn.visibility = View.VISIBLE
        }
        binding.projectIndividualBudget.setOnClickListener {
            binding.projectIndividualBudget.visibility = View.GONE
            binding.projectIndividualBudgetEt.visibility = View.VISIBLE
            binding.projectIndividualBtn.visibility = View.VISIBLE
        }
        binding.projectIndividualCoordinator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.projectIndividualBtn.visibility = View.VISIBLE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.projectIndividualStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != statusItems.indexOf("Active")){
                    binding.projectIndividualBtn.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setProjectData(){
        binding.projectIndividualName.text = project.nombre
        binding.projectIndividualStatus.setSelection(statusItems.indexOf(project.estado))
        binding.projectIndividualDescription.text = project.descripcion
        binding.projectIndividualDescriptionEt.setText(project.descripcion)
        binding.projectIndividualBudget.text = project.presupuesto.toString()
        binding.projectIndividualBudgetEt.setText(project.presupuesto.toString())
        val date = project.fechaInicio.substring(0,10) + " to " + project.fechaFin.substring(0,10)
        binding.projectIndividualStartDate.text = date
        collaboratorsList.add(0, project.responsable)
        collaboratorsAdapter.notifyDataSetChanged()
    }

    private fun setBackBtn(){
        binding.projectIndividualBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun unSetEditableTextViews(){
        binding.projectIndividualDescription.visibility = View.VISIBLE
        binding.projectIndividualDescriptionEt.visibility = View.INVISIBLE
        binding.projectIndividualBudget.visibility = View.VISIBLE
        binding.projectIndividualBudgetEt.visibility = View.INVISIBLE
        binding.projectIndividualBtn.visibility = View.GONE
    }

    private fun setButton(){
        binding.projectIndividualBtn.setOnClickListener{
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
                unSetEditableTextViews()
                val pupOk = Dialog(requireContext())
                val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
                pupOk.setContentView(bindingPupOk.root)
                pupOk.setCancelable(true)
                pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
                pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
                val okBtn = bindingPupOk.pupOkBtn
                bindingPupOk.pupYesNoDescription.text = getString(R.string.popup_ok_project_individual)
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
}