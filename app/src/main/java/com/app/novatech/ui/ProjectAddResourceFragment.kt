package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.novatech.databinding.FragmentProjectAddResourceBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.util.ResourcesAddController
import com.google.gson.JsonParser

class ProjectAddResourceFragment : Fragment() {
    private var _binding: FragmentProjectAddResourceBinding? = null
    private val binding get() = _binding!!
    private val typeItems = arrayOf("Financial", "Human", "Material")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectAddResourceBinding.inflate(inflater, container, false)
        setTypeSpinner()
        setBackBtn()
        setBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTypeSpinner(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, typeItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectAddResourceType.adapter = adapter
    }

    private fun setBackBtn(){
        binding.projectAddResourceBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setBtn(){
        binding.projectAddResourceBtn.setOnClickListener {
            val pupOk = Dialog(requireContext())
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = com.app.novatech.R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                ResourcesAddController.resourcesAddAttempt(arguments?.getString("name")!!,
                    binding.projectAddResourceName.text.toString(),
                    binding.projectAddResourceDescription.text.toString(),
                    binding.projectAddResourceType.selectedItem.toString()){
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
}