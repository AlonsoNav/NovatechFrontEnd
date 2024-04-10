package com.app.novatech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.model.Logs

class LogAdapter (private val logList : ArrayList<Logs>) : RecyclerView.Adapter<LogAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.item_log_title)
        val description : TextView = itemView.findViewById(R.id.item_log_description)
        val date : TextView = itemView.findViewById(R.id.item_log_date_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_log, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = logList[position]
        holder.title.text = currentItem.title
        holder.date.text = currentItem.date
        holder.description.text = currentItem.description
    }
}