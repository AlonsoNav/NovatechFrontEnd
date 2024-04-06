package com.app.novatech.ui

import android.app.Dialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.app.novatech.R
import com.app.novatech.databinding.ActivityMenuBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.User

class Menu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val forumFragment = ForumFragment()

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

        val user = intent.getSerializableExtra("user") as? User

        // Setting the default fragment
        if (user != null) {
            if(user.admin){
                replaceFragment(CollaboratorsAdminFragment())
            }else {
                val collaboratorsFragment = CollaboratorsFragment()
                val bundle = Bundle().apply {
                    putSerializable("user", user)
                }
                collaboratorsFragment.arguments = bundle
                replaceFragment(collaboratorsFragment)
            }
        }
        binding.bottomNavigationView.selectedItemId = R.id.menu_collaborators

        binding.bottomNavigationView.setOnItemSelectedListener{item ->
            when (item.itemId){
                R.id.menu_project -> {
                    replaceFragment(ProjectFragment())
                }
                R.id.menu_forum -> {
                    replaceFragment(forumFragment)
                }
                R.id.menu_collaborators -> {
                    if (user != null) {
                        if(user.admin){
                            replaceFragment(CollaboratorsAdminFragment())
                        }else {
                            val collaboratorsFragment = CollaboratorsFragment()
                            val bundle = Bundle().apply {
                                putSerializable("user", user)
                            }
                            collaboratorsFragment.arguments = bundle
                            replaceFragment(collaboratorsFragment)
                        }
                    }
                }
                R.id.menu_logout -> {
                    pup()
                }
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameLayout.id, fragment)
            .addToBackStack(null)
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