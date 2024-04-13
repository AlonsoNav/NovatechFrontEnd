package com.app.novatech.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Collaborator
import com.app.novatech.ui.Menu
import com.app.novatech.util.CollaboratorsDeleteController
import com.app.novatech.util.ProjectCollaboratorsAddController
import com.google.gson.JsonParser

class ProjectAddCollaboratorsAdapter(private val activity: FragmentActivity?,
                                  private val context: Context,
                                  private val layoutInflater: LayoutInflater,
                                  private val collaboratorsList: ArrayList<Collaborator>,
                                  private val name: String)
    : RecyclerView.Adapter<ProjectAddCollaboratorsAdapter.MyViewHolder>(), Filterable {

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.item_project_add_collaborator_name)
        val email : TextView = itemView.findViewById(R.id.item_project_add_collaborator_email)
        val add : ImageView = itemView.findViewById(R.id.item_project_add_collaborator_add)
    }

    private var filteredList: List<Collaborator> = collaboratorsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_project_add_collaborator, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.name.text = currentItem.name
        holder.email.text = currentItem.email
        holder.add.setOnClickListener {
            setAddBtn(currentItem, position)
        }
    }

    private fun setAddBtn(currentItem : Collaborator, position: Int){
        val pupOk = Dialog(context)
        val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
        pupOk.setContentView(bindingPupOk.root)
        pupOk.setCancelable(true)
        pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
        pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
        val okBtn = bindingPupOk.pupOkBtn
        try{
            ProjectCollaboratorsAddController.addProjectCollaboratorsAttempt(name, currentItem.email) {
                val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                if(it.isSuccessful){
                    collaboratorsList.removeAt(position)
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
        pupOk.show()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val query = constraint.toString()
                filteredList = if(query.isEmpty()){
                    collaboratorsList
                }else{
                    collaboratorsList.filter {
                        it.name.contains(query, ignoreCase = true) or
                                it.email.contains(query, ignoreCase = true)
                    }
                }
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredList = results?.values as List<Collaborator>
                notifyDataSetChanged()
            }

        }
    }
}

