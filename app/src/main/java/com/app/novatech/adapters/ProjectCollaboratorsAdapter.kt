package com.app.novatech.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.ui.Menu

class ProjectCollaboratorsAdapter(
    private val menu: Menu,
    private val activity: FragmentActivity?,
    private val context: Context,
    private var collaborators: List<Collaborator>
) : RecyclerView.Adapter<ProjectCollaboratorsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.element_item_project_collaborator, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collaborator = collaborators[position]
        holder.bind(collaborator)
        setAnimation(holder.itemView)
        holder.itemView.findViewById<ImageView>(R.id.item_collaborator_delete).setOnClickListener {
            showDeleteConfirmationDialog(collaborator, position)
        }
    }

    override fun getItemCount(): Int = collaborators.size

    private fun setAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)
        view.startAnimation(animation)
    }

    private fun showDeleteConfirmationDialog(collaborator: Collaborator, position: Int) {

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.item_collaborator_name)
        private val emailTextView: TextView = view.findViewById(R.id.item_collaborator_email)

        fun bind(collaborator: Collaborator) = with(itemView) {
            nameTextView.text = collaborator.name
            emailTextView.text = collaborator.email
            // Bind other views if necessary
        }
    }
}
