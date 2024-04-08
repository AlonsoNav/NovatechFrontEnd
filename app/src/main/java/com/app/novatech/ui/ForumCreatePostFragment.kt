package com.app.novatech.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.novatech.R
import com.app.novatech.databinding.FragmentForumPostBinding
import com.app.novatech.databinding.FragmentProjectAddBinding
import com.app.novatech.databinding.FragmentProjectCollaboratorsBinding

class ForumCreatePostFragment : Fragment() {
    private var _binding: FragmentForumPostBinding?= null
    private val binding get() = _binding!!
    private lateinit var menu : Menu


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumPostBinding.inflate(inflater, container, false)
        setBackBtn()
        return binding.root
    }

    private fun setBackBtn(){
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}