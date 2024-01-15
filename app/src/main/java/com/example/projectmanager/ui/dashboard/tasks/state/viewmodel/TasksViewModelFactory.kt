package com.example.projectmanager.ui.dashboard.tasks.state.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TasksViewModelFactory(private val projectId: Int, private val taskStatus: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(projectId, taskStatus) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}