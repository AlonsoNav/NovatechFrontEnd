package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.novatech.R
import com.app.novatech.databinding.FragmentCollaboratorsBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.User
import com.app.novatech.util.CollaboratorsEditController
import com.app.novatech.util.CollaboratorsGetController
import com.google.gson.Gson
import com.google.gson.JsonParser

class CollaboratorsFragment : Fragment() {
    private var _binding: FragmentCollaboratorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var user : User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollaboratorsBinding.inflate(inflater, container, false)
        user = (arguments?.getSerializable("user") as? User)!!
        setUserData()
        setEditableTextViews()
        setButton()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUserData(){
        user?.let {
            binding.profileName.text = it.nombre
            binding.profileId.text = it.cedula
            binding.profileDepartment.text = it.departamento
            binding.profileEmail.text = it.correo
            binding.profileEmailEt.setText(it.correo)
            binding.profileTelephone.text = it.telefono
            binding.profileTelephoneEt.setText(it.telefono)
            if(it.proyecto != null){
                binding.profileProject.text = it.proyecto.nombre
            }else{
                binding.profileProject.text = getString(R.string.free)
            }
        }
    }

    private fun setEditableTextViews(){
        binding.profileTelephone.setOnClickListener {
            binding.profileTelephone.visibility = View.GONE
            binding.profileTelephoneEt.visibility = View.VISIBLE
            binding.profileBtn.visibility = View.VISIBLE
        }
        binding.profileEmail.setOnClickListener {
            binding.profileEmail.visibility = View.GONE
            binding.profileEmailEt.visibility = View.VISIBLE
            binding.profileBtn.visibility = View.VISIBLE
        }
        binding.profilePassword.setOnClickListener {
            binding.profilePassword.visibility = View.GONE
            binding.profileCurrentPasswordEt.visibility = View.VISIBLE
            binding.profileNewPasswordEt.visibility = View.VISIBLE
            binding.profileBtn.visibility = View.VISIBLE
        }
    }

    private fun unSetEditableTextViews(){
        binding.profileTelephone.visibility = View.VISIBLE
        binding.profileTelephoneEt.visibility = View.GONE
        binding.profileEmail.visibility = View.VISIBLE
        binding.profileEmailEt.visibility = View.GONE
        binding.profilePassword.visibility = View.VISIBLE
        binding.profileCurrentPasswordEt.visibility = View.GONE
        binding.profileNewPasswordEt.visibility = View.GONE
        binding.profileBtn.visibility = View.GONE
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
                val email = if(binding.profileEmailEt.visibility == View.VISIBLE) binding.profileEmailEt.text.toString() else null
                val phone = if(binding.profileTelephoneEt.visibility == View.VISIBLE) binding.profileTelephoneEt.text.toString() else null
                val password : String?
                val newPassword :  String?
                if(binding.profileCurrentPasswordEt.visibility == View.VISIBLE) {
                    password = binding.profileCurrentPasswordEt.text.toString()
                    newPassword = binding.profileNewPasswordEt.text.toString()
                }else{
                    password = null
                    newPassword = null
                }
                unSetEditableTextViews()
                val pupOk = Dialog(requireContext())
                val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
                pupOk.setContentView(bindingPupOk.root)
                pupOk.setCancelable(true)
                pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
                pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
                val okBtn = bindingPupOk.pupOkBtn
                try{
                    CollaboratorsEditController.editCollaboratorsAttempt(user.cedula,
                        email,
                        null,
                        phone,
                        newPassword,
                        password){
                        val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                        if(it.isSuccessful){
                            CollaboratorsGetController.getCollaboratorsAttempt(user.cedula){response ->
                                val jsonObjectForUser = JsonParser().parse(response.body?.string()).asJsonObject
                                if(response.isSuccessful) {
                                    user = Gson().fromJson(
                                        jsonObjectForUser.toString(),
                                        User::class.java
                                    )
                                }
                                requireActivity().runOnUiThread {
                                    setUserData()
                                }
                            }
                        }
                        requireActivity().runOnUiThread {
                            bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                        }
                    }
                }catch(e : Exception){
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
}