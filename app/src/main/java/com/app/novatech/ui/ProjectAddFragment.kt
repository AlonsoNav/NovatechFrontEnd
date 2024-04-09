package com.app.novatech.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.novatech.databinding.FragmentProjectAddBinding
import com.app.novatech.util.CollaboratorsGetFreeController
import com.google.gson.JsonParser
import java.util.Calendar

class ProjectAddFragment : Fragment() {
    private var _binding: FragmentProjectAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePickerDialogStartDate : DatePickerDialog
    private lateinit var datePickerDialogEndDate : DatePickerDialog
    private lateinit var collaboratorsAdapter: ArrayAdapter<String>
    private val collaboratorsList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectAddBinding.inflate(inflater, container, false)
        getCollaboratorsList()
        setResponsibleSpinner()
        setDatePickers()
        setBackBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun setResponsibleSpinner(){
        collaboratorsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, collaboratorsList)
        collaboratorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectsAddResponsible.adapter = collaboratorsAdapter
    }

    private fun setDatePickers(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var startDateSelected = false
        datePickerDialogStartDate = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.projectsAddStartDate.setText(selectedDate)
            calendar.set(selectedYear, selectedMonth, selectedDay)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            binding.projectsAddEndDate.setText("")
            datePickerDialogEndDate = DatePickerDialog(requireContext(), { _, selectedYear2, selectedMonth2, selectedDay2 ->
                val selectedDate2 = "$selectedDay2/${selectedMonth2 + 1}/$selectedYear2"
                binding.projectsAddEndDate.setText(selectedDate2)
            }, selectedYear, selectedMonth, selectedDay + 1)
            datePickerDialogEndDate.datePicker.minDate = calendar.timeInMillis
            startDateSelected = true
        }, year, month, day)
        datePickerDialogStartDate.datePicker.minDate = calendar.timeInMillis
        binding.projectsAddStartDate.setOnClickListener {
            datePickerDialogStartDate.show()
        }
        binding.projectsAddEndDate.setOnClickListener{
            if(startDateSelected)
                datePickerDialogEndDate.show()
        }
    }

    private fun setBackBtn(){
        binding.projectsAddBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}