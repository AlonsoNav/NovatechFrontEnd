package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.TaskAdapter
import com.app.novatech.databinding.FragmentProjectTaskListBinding
import com.app.novatech.model.Tasks

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectTaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectTaskListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var filterTasksList: ArrayList<Tasks>
    private var _binding: FragmentProjectTaskListBinding? = null
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
        _binding = FragmentProjectTaskListBinding.inflate(inflater, container, false)
        arguments?.let{
            filterTasksList = it.getParcelableArrayList("taskList")!!
        }
        menu = requireActivity() as Menu
        setRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView(){
        binding.projectTaskRv.layoutManager = LinearLayoutManager(requireContext())
        binding.projectTaskRv.setHasFixedSize(true)
        binding.projectTaskRv.adapter = TaskAdapter(menu, filterTasksList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectTaskListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectTaskListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}