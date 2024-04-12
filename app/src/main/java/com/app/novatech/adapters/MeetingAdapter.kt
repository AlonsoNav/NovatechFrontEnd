package com.app.novatech.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.databinding.PopupOkBinding
import com.app.novatech.databinding.PopupYesnoBinding
import com.app.novatech.model.Meeting
import com.app.novatech.util.MeetingsDeleteController
import com.google.gson.JsonParser

class MeetingAdapter (private val context: Context, private val layoutInflater: LayoutInflater, private val meetingsList : ArrayList<Meeting>, private val name: String,
                      private val isAdmin: Boolean, private val activity: FragmentActivity?) : RecyclerView.Adapter<MeetingAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.item_meeting_title)
        val description : TextView = itemView.findViewById(R.id.item_meeting_description)
        val date : TextView = itemView.findViewById(R.id.item_meeting_date_text)
        val medium : TextView = itemView.findViewById(R.id.item_meeting_medium)
        val delete : ImageView = itemView.findViewById(R.id.item_meeting_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_task, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return meetingsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = meetingsList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        holder.date.text = currentItem.date
        holder.medium.text = currentItem.medium
        if(!isAdmin){
            holder.delete.visibility = View.GONE
        }
        holder.delete.setOnClickListener {
            setDeleteBtn(currentItem, position)
        }
    }

    private fun setDeleteBtn(currentItem : Meeting, position: Int){
        val pupYesno= Dialog(context)
        val bindingPup = PopupYesnoBinding.inflate(layoutInflater)
        pupYesno.setContentView(bindingPup.root)
        pupYesno.setCancelable(true)
        pupYesno.window?.setBackgroundDrawableResource(android.R.color.transparent)
        pupYesno.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
        val yesBtn = bindingPup.pupYesBtn
        val noBtn = bindingPup.pupNoBtn
        bindingPup.pupYesNoDescription.text = context.getString(R.string.popup_yesno_project_resources_delete, currentItem.title)
        yesBtn.setOnClickListener{
            val pupOk = Dialog(context)
            val bindingPupOk = PopupOkBinding.inflate(layoutInflater)
            pupOk.setContentView(bindingPupOk.root)
            pupOk.setCancelable(true)
            pupOk.window?.setBackgroundDrawableResource(android.R.color.transparent)
            pupOk.window?.attributes?.windowAnimations = R.style.CustomDialogAnimation
            val okBtn = bindingPupOk.pupOkBtn
            try{
                MeetingsDeleteController.deleteMeetingsAttempt(name, currentItem.title) {
                    val jsonObject = JsonParser().parse(it.body?.string()).asJsonObject
                    if(it.isSuccessful){
                        meetingsList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                    activity?.runOnUiThread {
                        bindingPupOk.pupYesNoDescription.text = jsonObject.get("message").asString
                    }
                }
            }catch(e: Exception){
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