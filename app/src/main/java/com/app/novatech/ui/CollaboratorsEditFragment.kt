package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.app.novatech.R
import com.app.novatech.adapters.CollaboratorAdapter
import com.app.novatech.databinding.FragmentCollaboratorsBinding
import com.app.novatech.databinding.FragmentCollaboratorsEditBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.model.Department
import com.app.novatech.model.Project
import com.app.novatech.model.User
import com.app.novatech.util.CollaboratorController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class CollaboratorsEditFragment : Fragment() {

    private var _binding: FragmentCollaboratorsEditBinding? = null
    private val binding get() = _binding!!
    private val client = OkHttpClient()
    private val scope = CoroutineScope(Dispatchers.Main)
    private var collaborator: Collaborator? = null
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            collaborator = it.getSerializable(ARG_USER) as? Collaborator
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCollaboratorsEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDepartmentsAndSetSpinnerSelection()
        fetchProjectsAndSetSpinnerSelection()
        setUserData()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }

    private fun setUserData() {
        collaborator?.let { collab ->
            scope.launch {
                val response = withContext(Dispatchers.IO) {
                    try {
                        val futureResponse = CompletableDeferred<okhttp3.Response>()
                        CollaboratorController.getCollaboratorByCedula(collab.id) { response ->
                            futureResponse.complete(response)
                        }
                        futureResponse.await()
                    } catch (e: Exception) {
                        null
                    }
                }

                if (response?.isSuccessful == true) {
                    user = parseResponseToUser(response)
                    updateUIWithUserData(user)
                } else {
                }
            }
        }
    }


    private fun parseResponseToUser(response: okhttp3.Response): User? {
        return try {
            val responseBody = response.body?.string()
            Gson().fromJson(responseBody, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    private fun updateUIWithUserData(user: User?) {
        user?.let {
            binding.profileName.text = it.nombre
            binding.profileId.text = it.cedula
        }
    }


    private fun fetchProjectsAndSetSpinnerSelection() {
        scope.launch {
            val projects = withContext(Dispatchers.IO) {
                val request = Request.Builder().url("https://backend-ap.fly.dev/api/proyectos").build()
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            Gson().fromJson<List<Project>>(responseBody, object : TypeToken<List<Project>>() {}.type)
                        } else emptyList()
                    }
                } catch (e: Exception) {
                    emptyList<Project>()
                }
            }
            val adapterProjects = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, projects.map { it.nombre }).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.profileProjectSpinner.adapter = adapterProjects

            user?.proyecto?.let { userProject ->
                val projectPosition = projects.indexOfFirst { project -> project.nombre == userProject.nombre }
                if (projectPosition >= 0) {
                    binding.profileProjectSpinner.setSelection(projectPosition)
                }
            }
        }
    }


    private fun fetchDepartmentsAndSetSpinnerSelection() {
        scope.launch {
            val departments = withContext(Dispatchers.IO) {
                val request = Request.Builder().url("https://backend-ap.fly.dev/api/departamentos").build()
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()
                            Gson().fromJson<List<Department>>(responseBody, object : TypeToken<List<Department>>() {}.type)
                        } else emptyList()
                    }
                } catch (e: Exception) {
                    emptyList<Department>()
                }
            }

            val adapterDepartments = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departments.map { it.nombre }).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            binding.profileDepartmentSpinner.adapter = adapterDepartments

            user?.let { user ->
                val departmentPosition = departments.indexOfFirst { it.nombre == user.departamento }
                if (departmentPosition >= 0) {
                    binding.profileDepartmentSpinner.setSelection(departmentPosition)
                }
            }
        }
    }


    private fun setButton(){
        binding.profileBtn.setOnClickListener{
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
                val pupOk = Dialog(requireContext())
                val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
                pupOk.setContentView(bindingPupOk.root)
                pupOk.setCancelable(true)
                pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
                pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
                val okBtn = bindingPupOk.pupOkBtn
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


    companion object {
        private const val ARG_USER = "user"

        fun newInstance(collaborator: Collaborator): CollaboratorsEditFragment {
            val fragment = CollaboratorsEditFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, collaborator)
            fragment.arguments = args
            return fragment
        }
    }
}