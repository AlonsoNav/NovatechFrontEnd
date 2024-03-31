package com.app.novatech.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.app.novatech.model.Message
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.google.android.material.bottomnavigation.BottomNavigationView


// TODO: Create "CreateForum"
class Forum : AppCompatActivity() {

    private lateinit var recyclerViewMessages: RecyclerView
    private lateinit var spinnerForum: Spinner
    private lateinit var bottomNavigationView: BottomNavigationView
    private val messages = listOf(
        Message("Lorem ipsum dolor sit amet...", "02/19/2024 18:05", "Name Lastname"),
        // More sample messages
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_forum)

        recyclerViewMessages = findViewById(R.id.recyclerViewPosts)
        spinnerForum = findViewById(R.id.spinnerForum)

        //recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        //recyclerViewMessages.adapter = MessagesAdapter(messages)

        setupSpinner()
        //setupBottomNavigationView()
    }

    private fun setupSpinner() {
        // Setup spinner for forum selection (general or project-specific)
    }

    /*private fun setupBottomNavigationView() {
        bottomNavigationView.menu.getItem(1).isChecked = true
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    // Handle home action
                    true
                }
                R.id.navigation_dashboard -> {
                    // Handle dashboard action
                    true
                }
                R.id.navigation_notifications -> {
                    // Handle notifications action
                    true
                }
                else -> false
            }
        }

        // Customize colors
        bottomNavigationView.itemIconTintList = ColorStateList.valueOf(Color.parseColor("#YourColorHere"))
        bottomNavigationView.itemTextColor = ColorStateList.valueOf(Color.parseColor("#YourColorHere"))
    }*/
}
