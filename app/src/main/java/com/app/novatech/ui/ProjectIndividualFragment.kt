package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectIndividualFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectIndividualFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentProjectIndividualBinding? = null
    private val binding get() = _binding!!
    private val historyFragment = ProjectLogsFragment()
    private val resourcesFragment = ProjectResourcesFragment()
    private val taskFragment = ProjectTaskFragment()
    private val statusItems = arrayOf("Active", "Inactive", "Completed")
    private lateinit var menu : Menu

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
    ): View? {
        _binding = FragmentProjectIndividualBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        setStatusSpinner()
        setEditableTextViews()
        setButton()
        setHistoryBtn()
        setResourcesBtn()
        setTasksBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectIndividualFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectIndividualFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}