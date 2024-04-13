package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.app.novatech.R
import com.app.novatech.databinding.FragmentCollaboratorsEditBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.User
import com.app.novatech.util.CollaboratorsEditAdminController
import com.app.novatech.util.CollaboratorsGetController
import com.app.novatech.util.ProjectsListController
import com.google.gson.Gson
import com.google.gson.JsonParser

class CollaboratorsEditFragment : Fragment() {
    private var _binding: FragmentCollaboratorsEditBinding? = null
    private val binding get() = _binding!!
    private val departments = ArrayList<String>()
    private val projectsList = ArrayList<String>()
    private lateinit var projectAdapter: ArrayAdapter<String>
    private lateinit var user : User
    private var project: String? = null
    private var department: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCollaboratorsEditBinding.inflate(inflater, container, false)
        setProjectSpinner()
        getProjectsList()
        setBackBtn()
        setDepartmentSpinner()
        setButton()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setBackBtn(){
        binding.collaboratorsEditBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
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
                getUser()
            }
        }catch (_: Exception){

        }
    }

    private fun getUser(){
        try{
            CollaboratorsGetController.getCollaboratorsAttempt(arguments?.getString("id")!!){ response ->
                val jsonObjectForUser = JsonParser().parse(response.body?.string()).asJsonObject
                if(response.isSuccessful) {
                    user = Gson().fromJson(
                        jsonObjectForUser.toString(),
                        User::class.java
                    )
                }
                requireActivity().runOnUiThread {
                    setUserData()
                    setEditableTextViews()
                }
            }
        }catch (_: Exception){

        }
    }

    private fun setUserData(){
        binding.collaboratorsEditName.text = user.nombre
        binding.collaboratorsEditId.text = user.cedula
        binding.collaboratorsEditEmail.text = user.correo
        binding.collaboratorsEditEmailEt.setText(user.correo)
        binding.collaboratorsEditPhone.text = user.telefono
        binding.collaboratorsEditPhoneEt.setText(user.telefono)
        binding.collaboratorsEditDepartment.setSelection(departments.indexOf(user.departamento))
        if(user.proyecto == null){
            binding.collaboratorsEditProject.setSelection(projectsList.indexOf("Free"))
        }else{
            binding.collaboratorsEditProject.setSelection(projectsList.indexOf(user.proyecto!!.nombre))
        }
    }

    private fun setProjectSpinner(){
        projectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, projectsList)
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.collaboratorsEditProject.adapter = projectAdapter
    }

    private fun setDepartmentSpinner(){
        departments.add(getString(R.string.department_it))
        departments.add(getString(R.string.department_admin))
        departments.add(getString(R.string.department_accountability))
        departments.sort()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departments)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.collaboratorsEditDepartment.adapter = adapter
    }

    private fun setEditableTextViews(){
        binding.collaboratorsEditPhone.setOnClickListener {
            binding.collaboratorsEditPhone.visibility = View.GONE
            binding.collaboratorsEditPhoneEt.visibility = View.VISIBLE
            binding.collaboratorsEditBtn.visibility = View.VISIBLE
        }
        binding.collaboratorsEditEmail.setOnClickListener {
            binding.collaboratorsEditEmail.visibility = View.GONE
            binding.collaboratorsEditEmailEt.visibility = View.VISIBLE
            binding.collaboratorsEditBtn.visibility = View.VISIBLE
        }
        binding.collaboratorsEditPassword.setOnClickListener {
            binding.collaboratorsEditPassword.visibility = View.GONE
            binding.collaboratorsEditPasswordEt.visibility = View.VISIBLE
            binding.collaboratorsEditBtn.visibility = View.VISIBLE
        }
        binding.collaboratorsEditProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val projectPos = if(user.proyecto == null) "Free" else user.proyecto!!.nombre
                if(position != projectsList.indexOf(projectPos)){
                    binding.collaboratorsEditBtn.visibility = View.VISIBLE
                    project = binding.collaboratorsEditProject.selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        binding.collaboratorsEditDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != departments.indexOf(user.departamento)){
                    binding.collaboratorsEditBtn.visibility = View.VISIBLE
                    department = binding.collaboratorsEditDepartment.selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun unSetEditableTextViews(){
        binding.collaboratorsEditPhone.visibility = View.VISIBLE
        binding.collaboratorsEditPhoneEt.visibility = View.GONE
        binding.collaboratorsEditEmail.visibility = View.VISIBLE
        binding.collaboratorsEditEmailEt.visibility = View.GONE
        binding.collaboratorsEditPassword.visibility = View.VISIBLE
        binding.collaboratorsEditPasswordEt.visibility = View.GONE
        binding.collaboratorsEditBtn.visibility = View.GONE
    }

    private fun setButton(){
        binding.collaboratorsEditBtn.setOnClickListener{
            val pupYesno= Dialog(requireContext())
            val bindingPup = PopupYesnoBinding.inflate(layoutInflater)
            pupYesno.setContentView(bindingPup.root)
            pupYesno.setCancelable(true)
            pupYesno.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupYesno.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val yesBtn = bindingPup.pupYesBtn
            val noBtn = bindingPup.pupNoBtn
            bindingPup.pupYesNoDescription.text = getString(R.string.popup_yesno_profile)
            yesBtn.setOnClickListener{
                val email = if(binding.collaboratorsEditEmailEt.visibility == View.VISIBLE)
                    binding.collaboratorsEditEmailEt.text.toString() else null
                val phone = if(binding.collaboratorsEditPhoneEt.visibility == View.VISIBLE)
                    binding.collaboratorsEditPhoneEt.text.toString() else null
                val password = if(binding.collaboratorsEditPasswordEt.visibility == View.VISIBLE)
                    binding.collaboratorsEditPasswordEt.text.toString() else null
                unSetEditableTextViews()
                val pupOk = Dialog(requireContext())
                val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
                pupOk.setContentView(bindingPupOk.root)
                pupOk.setCancelable(true)
                pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
                pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
                val okBtn = bindingPupOk.pupOkBtn
                try{
                    CollaboratorsEditAdminController.editAdminCollaboratorsAttempt(user.cedula,
                        email,
                        department,
                        phone,
                        password,
                        project){
                        val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                        if(it.isSuccessful)
                            getUser()
                        requireActivity().runOnUiThread {
                            bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                        }
                    }
                }catch(e : Exception){
                    bindingPupOk.pupYesNoDescription.text = getString(R.string.failed_server)
                }
                bindingPupOk.pupYesNoDescription.text = getString(R.string.popup_ok_profile)
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