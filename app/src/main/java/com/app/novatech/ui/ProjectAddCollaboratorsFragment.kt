package com.app.novatech.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ProjectCollaboratorsAdapter
import com.app.novatech.databinding.FragmentProjectAddCollaboratorsBinding
import com.app.novatech.model.Collaborator

class ProjectAddCollaboratorsFragment : Fragment() {
    private var _binding: FragmentProjectAddCollaboratorsBinding? = null
    private val binding get() = _binding!!
    private var projectName: String? = null
    private val collaboratorsList = ArrayList<Collaborator>()
    private lateinit var menu : Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectName = it.getString(ARG_PROJECT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectAddCollaboratorsBinding.inflate(inflater, container, false)

        menu = requireActivity() as Menu


        if (menu != null) {
            binding.projectCollaboratorsRv.layoutManager = LinearLayoutManager(requireContext())


            binding.projectCollaboratorsRv.adapter = ProjectCollaboratorsAdapter(
                menu,
                activity,
                requireContext(),
                emptyList()
            )
        } else {
            Log.e("ProjectCollabFragment", "Activity cannot be cast to Menu.")
        }

        projectName?.let {
            loadCollaborators(it)
        }

        setBackBtn()

        return binding.root
    }

    private fun setBackBtn(){
        binding.projectAddCollaboratorsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    private fun loadCollaborators(projectName: String) {
    }

    private fun setRecyclerView() {
        val menu = requireActivity() as Menu
        val adapter = ProjectCollaboratorsAdapter(menu, activity, requireContext(), collaboratorsList)
        binding.projectCollaboratorsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PROJECT_NAME = "projectName"

        @JvmStatic
        fun newInstance(projectName: String) =
            ProjectAddCollaboratorsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROJECT_NAME, projectName)
                }
            }
    }
}
