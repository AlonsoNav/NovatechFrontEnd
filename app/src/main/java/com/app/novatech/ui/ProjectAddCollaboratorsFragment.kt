package com.app.novatech.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.ProjectCollaboratorsAdapter
import com.app.novatech.databinding.FragmentProjectCollaboratorsBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.util.ProjectsGetController
import org.json.JSONArray
import org.json.JSONObject

class ProjectCollaboratorsFragment : Fragment() {
    private var _binding: FragmentProjectCollaboratorsBinding? = null
    private val binding get() = _binding!!
    private var projectName: String? = null
    private val collaboratorsList = ArrayList<Collaborator>()

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
        _binding = FragmentProjectCollaboratorsBinding.inflate(inflater, container, false)


        val menuActivity = activity as? Menu


        if (menuActivity != null) {
            binding.projectCollaboratorsRv.layoutManager = LinearLayoutManager(requireContext())


            binding.projectCollaboratorsRv.adapter = ProjectCollaboratorsAdapter(
                menuActivity,
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

        return binding.root
    }


    private fun loadCollaborators(projectName: String) {
        Log.d("ProjectCollabFragment", "Loading collaborators for project: $projectName")
        ProjectsGetController.getColabsAttempt(projectName) { response ->
            Log.d("ProjectCollabFragment", "Response received")
            if (!response.isSuccessful) {
                return@getColabsAttempt
            }
            val responseBody = response.body?.string()
            val jsonArray = JSONArray(responseBody)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i) as JSONObject
                val name = jsonObject.getString("nombre")
                val email = jsonObject.getString("correo")
                collaboratorsList.add(Collaborator(name, email, jsonObject.getString("_id")))
            }
            activity?.runOnUiThread {
                setRecyclerView()
            }
        }
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
            ProjectCollaboratorsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROJECT_NAME, projectName)
                }
            }
    }
}
