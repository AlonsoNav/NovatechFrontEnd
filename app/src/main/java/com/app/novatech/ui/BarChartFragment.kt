package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.app.novatech.R
import com.app.novatech.databinding.FragmentBarChartBinding
import com.app.novatech.model.Tasks
import com.app.novatech.model.User
import com.app.novatech.util.ProjectsListController
import com.app.novatech.util.TasksGetController
import com.google.gson.JsonParser
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class BarChartFragment : Fragment() {
    private var _binding: FragmentBarChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private val projectsList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private val taskList = ArrayList<Tasks>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarChartBinding.inflate(inflater, container, false)
        user = (arguments?.getSerializable("user") as? User)!!
        setProjectAdapter()
        getProjectsList()
        setBackBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProjectsList(){
        if(user.admin){
            try{
                ProjectsListController.getProjectsListAttempt {
                    val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                    for(jsonObject in jsonArray){
                        projectsList.add(jsonObject.asJsonObject.get("nombre").asString)
                    }
                    activity?.runOnUiThread {
                        adapter.notifyDataSetChanged()
                        setBarChart()
                        setSpinner()
                    }
                }
            }catch(_: Exception){

            }
        }else if(user.proyecto != null){
            projectsList.add(user.proyecto!!.nombre)
            adapter.notifyDataSetChanged()
            setBarChart()
        }
    }

    private fun setProjectAdapter(){
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, projectsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.barChartProject.adapter = adapter
    }

    private fun setBackBtn(){
        binding.barChartBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setSpinner(){
        binding.barChartProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setBarChart()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun setBarChart(){
        taskList.clear()
        try{
            TasksGetController.getTasksAttempt(binding.barChartProject.selectedItem.toString()) {response ->
                val jsonArray = JsonParser().parse(response.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    taskList.add(
                        Tasks(
                            jsonObject.asJsonObject.get("nombre").asString,
                            jsonObject.asJsonObject.get("descripcion").asString,
                            jsonObject.asJsonObject.get("storyPoints").asInt,
                            jsonObject.asJsonObject.get("responsable").asString,
                            jsonObject.asJsonObject.get("estado").asString
                        )
                    )
                }
                activity?.runOnUiThread {
                    val barChart = binding.barChart
                    val entries = ArrayList<BarEntry>()
                    entries.add(BarEntry(0f, taskList.filter { it.status == "Todo" }.size.toFloat()))
                    entries.add(BarEntry(1f, taskList.filter { it.status == "Doing" }.size.toFloat()))
                    entries.add(BarEntry(2f, taskList.filter { it.status == "Done" }.size.toFloat()))
                    val barDataSet = BarDataSet(entries, "Tasks status")

                    val colors = ArrayList<Int>()
                    colors.add(requireActivity().getColor(R.color.btn_color))
                    barDataSet.colors = colors

                    val data = BarData(barDataSet)
                    barChart.data = data

                    val xAxis = barChart.xAxis
                    xAxis.valueFormatter = CustomXAxisValueFormatter()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.granularity = 1f
                    xAxis.labelCount = 3

                    val yAxisLeft = barChart.axisLeft
                    yAxisLeft.axisMinimum = 0f

                    val yAxisRight = barChart.axisRight
                    yAxisRight.isEnabled = false

                    barChart.setFitBars(true)
                    barChart.description.isEnabled = false
                    barChart.legend.isEnabled = false
                    barChart.setDrawValueAboveBar(true)
                    barChart.invalidate()
                }
            }
        }catch(_: Exception){

        }
    }

    inner class CustomXAxisValueFormatter : com.github.mikephil.charting.formatter.ValueFormatter() {
        private val labels = arrayOf("To Do", "Doing", "Done")
        override fun getFormattedValue(value: Float): String {
            return if (value >= 0 && value < labels.size) {
                labels[value.toInt()]
            } else {
                ""
            }
        }
    }
}