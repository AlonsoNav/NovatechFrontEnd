package com.app.novatech.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.app.novatech.R
import com.app.novatech.databinding.FragmentProjectAddMeetingsBinding
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.util.MeetingsAddController
import com.google.gson.JsonParser
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProjectAddMeetingsFragment : Fragment() {
    private var _binding: FragmentProjectAddMeetingsBinding? = null
    private val binding get() = _binding!!
    private val mediumsItems = arrayOf("Discord", "Office", "Zoom")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectAddMeetingsBinding.inflate(inflater, container, false)
        setMediumSpinner()
        setDatePickers()
        setBtn()
        setBackBtn()
        return binding.root
    }

    private fun setMediumSpinner(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mediumsItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectsAddMeetingMedium.adapter = adapter
    }

    private fun setBackBtn(){
        binding.projectsAddMeetingBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setDatePickers(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialogStartDate = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val selectedDateTime = Calendar.getInstance()
                selectedDateTime.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
                val dateTimeString = dateFormat.format(selectedDateTime.time)
                binding.projectsAddMeetingDate.setText(dateTimeString)
            }, hour, minute, true)
            timePickerDialog.show()
        }, year, month, day)
        datePickerDialogStartDate.datePicker.minDate = calendar.timeInMillis
        binding.projectsAddMeetingDate.setOnClickListener {
            datePickerDialogStartDate.show()
        }
    }

    private fun setBtn(){
        binding.projectsAddMeetingBtn.setOnClickListener {
            val pupOk = Dialog(requireContext())
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                MeetingsAddController.addMeetingsAttempt(arguments?.getString("name")!!,
                    convertDate(binding.projectsAddMeetingDate.text.toString()),
                    binding.projectsAddMeetingName.text.toString(),
                    binding.projectsAddMeetingMedium.selectedItem.toString(),
                    binding.projectsAddMeetingDescription.text.toString()){
                    val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                    activity?.runOnUiThread {
                        bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                    }
                }
            }catch(e : Exception){
                bindingPupOk.pupYesNoDescription.text = getString(R.string.failed_server)
            }
            okBtn.setOnClickListener{
                pupOk.dismiss()
            }
            pupOk.show()
        }
    }

    private fun convertDate(date : String) : String{
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = formatter.parse(date)
        return parser.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}