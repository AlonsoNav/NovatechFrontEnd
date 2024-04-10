package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ResourceAdapter
import com.app.novatech.databinding.FragmentProjectResourcesBinding
import com.app.novatech.model.Resource
import com.app.novatech.util.ResourcesGetController
import com.google.gson.JsonParser

class ProjectResourcesFragment : Fragment() {
    private val resourceList = ArrayList<Resource>()
    private lateinit var adapter: ResourceAdapter
    private var _binding: FragmentProjectResourcesBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu : Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectResourcesBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        if(resourceList.isNotEmpty())
            resourceList.clear()
        getProjectResources()
        setAddBtn()
        setRecyclerView()
        setBackBtn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProjectResources(){
        try{
            ResourcesGetController.getResourcesAttempt(arguments?.getString("name")!!) {
                val jsonArray = JsonParser().parse(it.body?.string()).asJsonArray
                for(jsonObject in jsonArray){
                    resourceList.add(Resource(
                        jsonObject.asJsonObject.get("nombre").asString,
                        jsonObject.asJsonObject.get("descripcion").asString,
                        jsonObject.asJsonObject.get("tipo").asString,
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
        adapter = ResourceAdapter(requireContext(), layoutInflater, resourceList,
            arguments?.getBoolean("isAdmin")!! or
                    (arguments?.getString("user")!! == arguments?.getString("responsible")!!),
            arguments?.getString("name")!!,
            requireActivity())
        binding.projectResourcesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectResourcesRv.setHasFixedSize(true)
        binding.projectResourcesRv.adapter = adapter
    }

    private fun setAddBtn(){
        if(!arguments?.getBoolean("isAdmin")!! and (arguments?.getString("user")!! != arguments?.getString(
                "responsible"
            )!!)
        ){
            binding.projectResourcesAdd.visibility = View.GONE
        }
        binding.projectResourcesAdd.setOnClickListener {
            val resourceAddFragment = ProjectAddResourceFragment()
            val bundle = Bundle().apply {
                putString("name", arguments?.getString("name")!!)
            }
            resourceAddFragment.arguments = bundle
            menu.replaceFragment(resourceAddFragment)        }
    }

    private fun setBackBtn(){
        binding.projectResourcesBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}