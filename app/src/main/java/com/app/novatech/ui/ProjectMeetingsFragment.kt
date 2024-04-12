package com.app.novatech.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.MeetingAdapter
import com.app.novatech.databinding.FragmentProjectMeetingsBinding
import com.app.novatech.model.Meeting
import com.app.novatech.util.MeetingsGetController
import com.app.novatech.util.ResourcesGetController
import com.google.gson.JsonParser

class ProjectMeetingsFragment : Fragment() {
    private var _binding: FragmentProjectMeetingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu
    private var isAdmin = false
    private lateinit var adapter : MeetingAdapter
    private val meetingsList = ArrayList<Meeting>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectMeetingsBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        isAdmin = arguments?.getBoolean("isAdmin")!! or (arguments?.getString("user")!! == arguments?.getString("responsible")!!)
        if(meetingsList.isNotEmpty())
            meetingsList.clear()
        setRecyclerView()
        getMeetingsList()
        setAddBtn()
        setBackBtn()
        return binding.root
    }

    private fun getMeetingsList(){
        try{
            MeetingsGetController.getMeetingsAttempt(arguments?.getString("name")!!) {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                Log.d("json", jsonArray.toString())
                /*for(jsonObject in jsonArray){
                    meetingsList.add(
                        Meeting(
                            jsonObject.asJsonObject.get("temaReunion").asString,
                            jsonObject.asJsonObject.get("descripcion").asString,
                            jsonObject.asJsonObject.get("fecha").asString,
                            jsonObject.asJsonObject.get("medioReunion").asString,
                        )
                    )
                }*/
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }catch(_: Exception){

        }
    }

    private fun setBackBtn(){
        binding.projectMeetingsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setRecyclerView(){
        adapter = MeetingAdapter(requireContext(), layoutInflater, meetingsList, arguments?.getString("name")!!, isAdmin, activity)
        binding.projectMeetingsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectMeetingsRv.setHasFixedSize(true)
        binding.projectMeetingsRv.adapter = adapter
    }

    private fun setAddBtn(){
        if(!isAdmin)
            binding.projectMeetingsAdd.visibility = View.GONE
        binding.projectMeetingsAdd.setOnClickListener {
            val meetingsAddFragment = ProjectAddMeetingsFragment()
            val bundle = Bundle().apply {
                putString("name", arguments?.getString("name")!!)
            }
            meetingsAddFragment.arguments = bundle
            menu.replaceFragment(meetingsAddFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}