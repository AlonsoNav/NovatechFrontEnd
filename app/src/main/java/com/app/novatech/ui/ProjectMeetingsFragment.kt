package com.app.novatech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.novatech.adapters.LogAdapter
import com.app.novatech.databinding.FragmentMeetingListBinding
import com.app.novatech.databinding.FragmentProjectLogsBinding
import com.app.novatech.model.Logs

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectMeetingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectMeetingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var logList: ArrayList<Logs>
    private var _binding: FragmentMeetingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMeetingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectLogsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectMeetingsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}