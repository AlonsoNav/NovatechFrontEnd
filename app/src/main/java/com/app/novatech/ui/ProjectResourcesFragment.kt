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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectResourcesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectResourcesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var resourceList: ArrayList<Resource>
    private var _binding: FragmentProjectResourcesBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentProjectResourcesBinding.inflate(inflater, container, false)
        menu = requireActivity() as Menu
        setAddBtn()
        setRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView(){
        resourceList = arrayListOf(
            Resource("Title1", "Description1", "Material"),
            Resource("Title2", "Description2", "Human"))
        binding.projectResourcesRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectResourcesRv.setHasFixedSize(true)
        binding.projectResourcesRv.adapter = ResourceAdapter(requireContext(), layoutInflater, resourceList)
    }

    private fun setAddBtn(){
        binding.projectResourcesAdd.setOnClickListener {
            menu.replaceFragment(ProjectAddResourceFragment())
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectResourceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectResourcesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}