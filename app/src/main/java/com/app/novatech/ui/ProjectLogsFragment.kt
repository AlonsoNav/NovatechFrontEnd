package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.LogAdapter
import com.app.novatech.databinding.FragmentProjectLogsBinding
import com.app.novatech.model.Logs
import com.app.novatech.model.Resource
import com.app.novatech.util.LogsGetController
import com.app.novatech.util.ResourcesGetController
import com.google.gson.JsonParser

class ProjectLogsFragment : Fragment() {
    private val logList = ArrayList<Logs>()
    private var _binding: FragmentProjectLogsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectLogsBinding.inflate(inflater, container, false)
        setRecyclerView()
        getProjectLogs()
        setBackBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProjectLogs(){
        try{
            LogsGetController.getLogsAttempt(arguments?.getString("name")!!) {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    logList.add(
                        Logs(
                        jsonObject.asJsonObject.get("nombre").asString,
                        jsonObject.asJsonObject.get("descripcion").asString,
                        jsonObject.asJsonObject.get("fecha").asString.substring(0, 10),
                        )
                    )
                }
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }catch(_: Exception){

        }
    }

    private fun setRecyclerView(){
        adapter = LogAdapter(logList)
        binding.projectLogRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectLogRv.setHasFixedSize(true)
        binding.projectLogRv.adapter = LogAdapter(logList)
    }

    private fun setBackBtn(){
        binding.projectLogBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}