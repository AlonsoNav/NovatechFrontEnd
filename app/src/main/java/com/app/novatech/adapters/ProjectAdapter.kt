package com.app.novatech.adapters

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.ProjectForList
import com.app.novatech.ui.Menu
import com.app.novatech.ui.ProjectIndividualFragment
import com.app.novatech.util.ProjectsDeleteController
import com.google.gson.JsonParser
import java.util.Date

class ProjectAdapter (private val menu: Menu,
                      private val activity: FragmentActivity?,
                      private val context: Context,
                      private val layoutInflater: LayoutInflater,
                      private val projectsList: ArrayList<ProjectForList>,
                      private val isAdmin: Boolean,
                      private val user: String)
    : RecyclerView.Adapter<ProjectAdapter.MyViewHolder>(), Filterable{
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val layout : ConstraintLayout = itemView.findViewById(R.id.item_project)
        val name : TextView = itemView.findViewById(R.id.item_project_name)
        val responsible : TextView = itemView.findViewById(R.id.item_project_responsible)
        val delete : ImageView = itemView.findViewById(R.id.item_project_delete)
        val status : TextView = itemView.findViewById(R.id.item_project_status)
        val progressBar : ProgressBar = itemView.findViewById(R.id.item_project_progress_bar)
    }

    private var filteredList: List<ProjectForList> = projectsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_project, parent, false)
        return ProjectAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(!isAdmin)
            holder.delete.visibility = View.GONE
        val currentItem = filteredList[position]
        holder.name.text = currentItem.name
        holder.responsible.text = currentItem.responsible
        holder.status.text = currentItem.status
        val totalDays = currentItem.endDate.time - currentItem.startDate.time
        val elapsedDays = Date().time - currentItem.startDate.time
        holder.progressBar.progress = (elapsedDays.toDouble() / totalDays.toDouble() * 100).toInt()
        holder.delete.setOnClickListener {
            setDeleteBtn(currentItem, position)
        }
        holder.layout.setOnClickListener {
            val projectIndividualFragment = ProjectIndividualFragment()
            val bundle = Bundle().apply {
                putString("name", currentItem.name)
                putBoolean("isAdmin", isAdmin)
                putString("user", user)
            }
            projectIndividualFragment.arguments = bundle
            menu.replaceFragment(projectIndividualFragment)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val query = constraint.toString()
                filteredList = if(query.isEmpty()){
                    projectsList
                }else{
                    projectsList.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = results?.values as List<ProjectForList>
                notifyDataSetChanged()
            }

        }
    }

    private fun setDeleteBtn(currentItem : ProjectForList, position: Int){
        val pupYesno= Dialog(context)
        val bindingPup = PopupYesnoBinding.inflate(layoutInflater)
        pupYesno.setContentView(bindingPup.root)
        pupYesno.setCancelable(true)
        pupYesno.window?.setBackgroundDrawableResource(android.R.color.transparent)
        pupYesno.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
        val yesBtn = bindingPup.pupYesBtn
        val noBtn = bindingPup.pupNoBtn
        bindingPup.pupYesNoDescription.text = context.getString(R.string.popup_yesno_project_resources_delete, currentItem.name)
        yesBtn.setOnClickListener{
            val pupOk = Dialog(context)
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                ProjectsDeleteController.deleteProjectsAttempt(currentItem.name) {
                    val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                    if(it.isSuccessful){
                        projectsList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                    activity?.runOnUiThread {
                        bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                    }
                }
            }catch (e: Exception){
                bindingPupOk.pupYesNoDescription.text = context.getString(R.string.failed_server)
            }
            okBtn.setOnClickListener{
                pupOk.dismiss()
            }
            pupYesno.dismiss()
            pupOk.show()
        }
        noBtn.setOnClickListener {
            pupYesno.dismiss()
        }
        pupYesno.show()
    }
}