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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CollaboratorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollaboratorsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCollaboratorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollaboratorsBinding.inflate(inflater, container, false)
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
        val user = arguments?.getSerializable("user") as? User
        user?.let {
            binding.profileName.text = it.nombre
            binding.profileId.text = it.cedula
            binding.profileDepartment.text = it.departamento
            binding.profileEmail.text = it.correo
            binding.profileEmailEt.hint = it.correo
            binding.profileTelephone.text = it.telefono
            binding.profileTelephoneEt.hint = it.telefono
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
                unSetEditableTextViews()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CollaboratorsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CollaboratorsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}