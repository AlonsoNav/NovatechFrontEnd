package com.app.novatech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.databinding.FragmentCollaboratorsListBinding
import com.app.novatech.databinding.FragmentRegisterCollaboratorBinding
import com.app.novatech.model.Colaborador
import com.app.novatech.util.CollaboratorController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ColaboradorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvName)
    val tvEmail: TextView = view.findViewById(R.id.tvEmail)
    val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
}

class ColaboradorAdapter(private val colaboradores: List<Colaborador>) : RecyclerView.Adapter<ColaboradorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColaboradorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_item_collaborator, parent, false)
        return ColaboradorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColaboradorViewHolder, position: Int) {
        val colaborador = colaboradores[position]
        holder.tvName.text = colaborador.nombre
        holder.tvEmail.text = colaborador.email
        holder.btnDelete.setOnClickListener {
            //eliminar colaborador
        }
    }

    override fun getItemCount() = colaboradores.size
}

class CollaboratorListFragment : Fragment() {
    private var _binding: FragmentCollaboratorsListBinding? = null
    private val binding get() = _binding!!
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCollaboratorsListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cargarColaboradores()

        binding.btnCreate.setOnClickListener {

            val registerFragment = CollaboratorRegisterFragment()


            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_register_collaborator, registerFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun cargarColaboradores() {
        CoroutineScope(Dispatchers.Main).launch {
            val colaboradores = CollaboratorController.obtenerColaboradores()
            binding.recyclerViewCollaborators.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewCollaborators.adapter = ColaboradorAdapter(colaboradores)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel() // Cancela las Coroutines cuando el Fragment se destruye
        _binding = null
    }
}