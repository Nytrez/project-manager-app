package com.example.projectmanager.ui.dashboard.tasks.kanban.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectmanager.ui.dashboard.tasks.state.view.TasksDoneFragment
import com.example.projectmanager.ui.dashboard.tasks.state.view.TasksInProgressFragment
import com.example.projectmanager.ui.dashboard.tasks.state.view.TasksToDoFragment

class ViewPagerAdapter(fragment: Fragment, private val projectId: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle().apply {
            putInt("projectId", projectId)
        }
        return when (position) {
            0 -> TasksToDoFragment().apply { arguments = bundle }
            1 -> TasksInProgressFragment().apply { arguments = bundle }
            2 -> TasksDoneFragment().apply { arguments = bundle }
            else -> Fragment()
        }
    }
}