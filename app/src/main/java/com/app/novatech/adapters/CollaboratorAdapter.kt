package com.app.novatech.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.app.novatech.util.CollaboratorsDeleteController
import com.google.gson.JsonParser

class CollaboratorAdapter(private val activity: FragmentActivity?,
                          private val context: Context,
                          private val layoutInflater: LayoutInflater,
                          private val collaboratorsList: ArrayList<Collaborator>)
    : RecyclerView.Adapter<CollaboratorAdapter.MyViewHolder>(), Filterable {

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.item_collaborator_name)
        val email : TextView = itemView.findViewById(R.id.item_collaborator_email)
        val delete : ImageView = itemView.findViewById(R.id.item_collaborator_delete)
        val edit : ImageView = itemView.findViewById(R.id.item_collaborator_edit)
    }

    private var filteredList: List<Collaborator> = collaboratorsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_collaborator, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.name.text = currentItem.name
        holder.email.text = currentItem.email
        holder.delete.setOnClickListener {
            setDeleteBtn(currentItem, position)
        }
    }

    private fun setDeleteBtn(currentItem : Collaborator, position: Int){
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
                CollaboratorsDeleteController.deleteCollaboratorsAttempt(currentItem.id) {
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
            pupYesno.dismiss()
            pupOk.show()
        }
        noBtn.setOnClickListener {
            pupYesno.dismiss()
        }
        pupYesno.show()
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