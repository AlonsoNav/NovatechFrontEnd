package com.app.novatech.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.app.novatech.R
import com.app.novatech.util.Database
import com.google.gson.JsonObject
import okhttp3.Response

class CreateProject : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etResources: EditText
    private lateinit var etBudget: EditText
    private lateinit var etDescription: EditText
    private lateinit var spinnerPersonInCharge: Spinner
    private lateinit var spinnerInitialDate: Spinner
    private lateinit var btnAddProject: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_create_project)

        etName = findViewById(R.id.etName)
        etResources = findViewById(R.id.etResources)
        etBudget = findViewById(R.id.etBudget)
        etDescription = findViewById(R.id.etDescription)
        spinnerPersonInCharge = findViewById(R.id.spinnerPersonInCharge)
        spinnerInitialDate = findViewById(R.id.spinnerInitialDate)
        btnAddProject = findViewById(R.id.btnAddProject)

        btnAddProject.setOnClickListener {
            addProject()
        }
    }

    private fun addProject() {
        val name = etName.text.toString()
        val resources = etResources.text.toString()
        val budget = etBudget.text.toString()
        val description = etDescription.text.toString()
        val personInCharge = spinnerPersonInCharge.selectedItem.toString()
        val initialDate = spinnerInitialDate.selectedItem.toString()

        val json = JsonObject().apply {
            addProperty("name", name)
            addProperty("resources", resources)
            addProperty("budget", budget)
            addProperty("description", description)
            addProperty("personInCharge", personInCharge)
            addProperty("initialDate", initialDate)
        }.toString()

        Database().postRequestToApi(json, "projects") { response ->
            handleResponse(response)
        }
    }

    private fun handleResponse(response: Response) {
        if (response.isSuccessful) {
            runOnUiThread {
                Toast.makeText(this, "Project added successfully", Toast.LENGTH_LONG).show()
            }
        } else {
            runOnUiThread {
                Toast.makeText(this, "Failed to add project", Toast.LENGTH_LONG).show()
            }
        }
    }
}