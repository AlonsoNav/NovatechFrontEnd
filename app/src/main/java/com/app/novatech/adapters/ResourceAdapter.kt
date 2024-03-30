package com.app.novatech.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Resource

class ResourceAdapter (private val context: Context, private val layoutInflater: LayoutInflater,
                       private val resourceList : ArrayList<Resource>) : RecyclerView.Adapter<ResourceAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.item_resource_title)
        val description : TextView = itemView.findViewById(R.id.item_resource_description)
        val type : TextView = itemView.findViewById(R.id.item_resource_type)
        val delete : ImageView = itemView.findViewById(R.id.item_resource_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_resource, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return resourceList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = resourceList[position]
        holder.title.text = currentItem.name
        holder.description.text = currentItem.description
        holder.type.text = currentItem.type
        holder.delete.setOnClickListener {
            setDeleteBtn(currentItem, position)
        }
    }

    private fun setDeleteBtn(currentItem : Resource, position: Int){
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
            resourceList.removeAt(position)
            notifyItemRemoved(position)
            val pupOk = Dialog(context)
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            bindingPupOk.pupYesNoDescription.text = context.getString(R.string.popup_ok_project_resources_delete, currentItem.name)
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