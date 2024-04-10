package com.app.novatech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.model.Tasks
import com.app.novatech.ui.Menu
import com.app.novatech.ui.ProjectEditTaskFragment

class TaskAdapter (private val menu: Menu, private val tasksList : ArrayList<Tasks>) : RecyclerView.Adapter<TaskAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.item_task_name)
        val description : TextView = itemView.findViewById(R.id.item_task_description)
        val storyPoints : TextView = itemView.findViewById(R.id.item_task_story_points_text)
        val responsible : TextView = itemView.findViewById(R.id.item_task_responsible)
        val edit : ImageView = itemView.findViewById(R.id.item_task_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.element_item_task, parent, false)
        return TaskAdapter.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = tasksList[position]
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description
        holder.responsible.text = currentItem.responsible
        holder.storyPoints.text = currentItem.storyPoints.toString()
        holder.edit.setOnClickListener {
            menu.replaceFragment(ProjectEditTaskFragment())
        }
    }
}