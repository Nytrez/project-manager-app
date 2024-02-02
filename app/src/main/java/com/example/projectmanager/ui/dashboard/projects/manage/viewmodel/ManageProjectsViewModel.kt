package com.example.projectmanager.ui.dashboard.projects.manage.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.projects.ProjectsResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import java.io.IOException

class ManageProjectsViewModel : ViewModel() {

    val allProjectsOwner: MutableLiveData<Resource<ProjectsResponse>> = MutableLiveData()

    init {
        getAllProjects()
    }

    private fun getAllProjects() {
        viewModelScope.launch {
            try {
                allProjectsOwner.postValue(Resource.Loading())
                val response = RetrofitBuilder.api.getAllProjectsOwner(SessionManager.fetchAuthToken()!!)
                handleApiResponse(response, allProjectsOwner)
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> allProjectsOwner.postValue(Resource.Error("Network Failure"))
                    else -> allProjectsOwner.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}