package com.app.novatech.ui

import CollaboratorRegistrationController
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.novatech.databinding.FragmentRegisterCollaboratorBinding
import com.app.novatech.model.Department
import com.app.novatech.model.Proyecto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class CollaboratorRegisterFragment : Fragment() {
    private var _binding: FragmentRegisterCollaboratorBinding? = null
    private val binding get() = _binding!!
    private val TAG = "RegCollabFragment"
    private val scope = CoroutineScope(Dispatchers.Main)
    private val client = OkHttpClient()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView")
        _binding = FragmentRegisterCollaboratorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        setButton()
        fetchProyectos()
        fetchDepartamentos()
    }

    fun fetchProyectos() {
        scope.launch {
            val proyectos = withContext(Dispatchers.IO) {
                val request = Request.Builder().url("https://backend-ap.fly.dev/api/proyectos").build()
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            listOf(Proyecto("Proyecto Ejemplo", "Activo", 10000.0, "Descripción", "2023-01-01", "Juan Pérez"))
                        } else {
                            emptyList<Proyecto>()
                        }
                    }
                } catch (e: Exception) {
                    emptyList<Proyecto>()
                }
            }

            val adapterProyectos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, proyectos.map { it.nombre })
            adapterProyectos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerProject.adapter = adapterProyectos
        }
    }

    fun fetchDepartamentos() {
        scope.launch {
            val departamentos = withContext(Dispatchers.IO) {
                val request = Request.Builder().url("https://backend-ap.fly.dev/api/departamentos").build()
                try {
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            listOf(Department("Departamento Ejemplo"))
                        } else {
                            emptyList<Department>()
                        }
                    }
                } catch (e: Exception) {
                    emptyList<Department>()
                }
            }

            val adapterDepartamentos = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departamentos.map { it.nombre })
            adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDepartment.adapter = adapterDepartamentos
        }
    }

    private fun setButton() {
        binding.btnAddMember.setOnClickListener {
            Log.d(TAG, "Attempting to add member")
            val nombre = binding.etName.text.toString()
            val cedula = binding.etId.text.toString()
            val correo = binding.etEmail.text.toString()
            val telefono = binding.etPhone.text.toString()
            val departamento = binding.etDepartment.text.toString()
            val contrasena = binding.etPassword.text.toString()

            if (nombre.isEmpty() || cedula.isEmpty() || correo.isEmpty() || telefono.isEmpty() || departamento.isEmpty() || contrasena.isEmpty()) {
                Log.d(TAG, "Validation failed: Empty fields")
                showPopupError("Todos los campos son requeridos")
            } else {
                CollaboratorRegistrationController.registrarColaborador(
                    nombre, cedula, correo, telefono, departamento, "",
                    contrasena, requireContext()
                ) { response, responseBody ->
                    activity?.runOnUiThread {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Member added successfully")
                            showPopupOk("Colaborador registrado exitosamente")
                        } else {
                            val errorMessage = responseBody ?: "Error desconocido"
                            Log.e(TAG, "Failed to add member: $errorMessage")
                            showPopupError("Error registrando colaborador: $errorMessage")
                        }
                    }
                }
            }
        }
    }

    private fun showPopupError(message: String) {
        binding.root.post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun showPopupOk(message: String) {
        binding.root.post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
        Log.d(TAG, "onDestroyView")
    }
}
