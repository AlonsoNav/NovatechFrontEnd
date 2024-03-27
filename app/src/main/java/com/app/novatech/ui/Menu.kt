package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.app.novatech.R
import com.app.novatech.databinding.ActivityMenuBinding
import com.app.novatech.databinding.PopupYesnoBinding

class Menu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val projectFragment = ProjectFragment()
    private val projectIndividualFragment = ProjectIndividualFragment()
    private val forumFragment = ForumFragment()
    private val collaboratorsFragment = CollaboratorsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setting the default fragment
        replaceFragment(collaboratorsFragment)
        binding.bottomNavigationView.selectedItemId = R.id.menu_collaborators

        binding.bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.menu_project -> {
                    replaceFragment(projectIndividualFragment)
                }
                R.id.menu_forum -> {
                    replaceFragment(forumFragment)
                }
                R.id.menu_collaborators -> {
                    replaceFragment(collaboratorsFragment)
                }
                R.id.menu_logout -> {
                    pup()
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .commit()
    }

    private fun pup(){
        val pupYesno= Dialog(this)
        val bindingPup = PopupYesnoBinding.inflate(layoutInflater)
        pupYesno.setContentView(bindingPup.root)
        pupYesno.setCancelable(true)
        pupYesno.window?.setBackgroundDrawableResource(android.R.color.transparent)
        pupYesno.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
        val yesBtn = bindingPup.pupYesBtn
        val noBtn = bindingPup.pupNoBtn
        bindingPup.pupYesNoDescription.text = getString(R.string.popup_logout)
        yesBtn.setOnClickListener{
            finish()
        }
        noBtn.setOnClickListener {
            pupYesno.dismiss()
        }
        pupYesno.show()
    }
}