package com.app.novatech.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.model.Colaborador
import com.app.novatech.ui.ColaboradorViewHolder

class CollaboratorListAdapter(private val colaboradores: List<Colaborador>) : RecyclerView.Adapter<ColaboradorViewHolder>() {

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