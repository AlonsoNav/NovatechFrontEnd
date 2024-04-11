package com.app.novatech.adapters

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.novatech.model.Tasks
import com.app.novatech.ui.ProjectTaskListFragment

class TaskListAdapter (private val tasks: ArrayList<Tasks>, fragmentManager: FragmentManager,
                       lifecycle: Lifecycle,
                       private val name: String,
                       private val responsible: String,
                       private val user: String,
                       private val isAdmin: Boolean) :
    FragmentStateAdapter (fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val taskListFragment = ProjectTaskListFragment()
        val args = Bundle().apply {
            putString("name", name)
            putString("responsible", responsible)
            putString("user", user)
            putBoolean("isAdmin", isAdmin)
        }
        when (position){
            0 -> args.putParcelableArrayList("taskList", tasks.filter { it.status == "Todo" } as ArrayList<out Parcelable>)
            1 -> args.putParcelableArrayList("taskList", tasks.filter { it.status == "Doing" } as ArrayList<out Parcelable>)
            2 -> args.putParcelableArrayList("taskList", tasks.filter { it.status == "Done" } as ArrayList<out Parcelable>)
        }
        taskListFragment.arguments = args
        return taskListFragment
    }
}