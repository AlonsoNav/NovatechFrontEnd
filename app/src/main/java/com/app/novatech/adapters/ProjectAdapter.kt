package com.app.novatech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.novatech.R
import com.app.novatech.model.Collaborator
import com.app.novatech.model.ProjectForList
import java.util.Date

class ProjectAdapter (private val projectsList: ArrayList<ProjectForList>)
    : RecyclerView.Adapter<ProjectAdapter.MyViewHolder>(), Filterable{
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
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
        val currentItem = filteredList[position]
        holder.name.text = currentItem.name
        holder.responsible.text = currentItem.responsible
        holder.status.text = currentItem.status
        val totalDays = currentItem.endDate.time - currentItem.startDate.time
        val elapsedDays = Date().time - currentItem.startDate.time
        holder.progressBar.progress = (elapsedDays.toDouble() / totalDays.toDouble() * 100).toInt()
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
}