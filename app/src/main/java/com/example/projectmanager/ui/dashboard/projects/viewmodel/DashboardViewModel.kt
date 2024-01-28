package com.example.projectmanager.ui.dashboard.projects.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
//import com.example.projectmanager.data.api.buildService
import com.example.projectmanager.data.model.projects.ProjectsResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class DashboardViewModel : ViewModel() {

    val allProjects: MutableLiveData<Resource<ProjectsResponse>> = MutableLiveData()

    fun getAllProjects() {
        viewModelScope.launch {
            try {
                allProjects.postValue(Resource.Loading())
                val response = RetrofitBuilder.api.getAllProjects(SessionManager.fetchAuthToken()!!)
                handleApiResponse(response, allProjects)
            } catch (t:Throwable){
                when(t) {
                    is IOException -> allProjects.postValue(Resource.Error("Network Failure"))
                    else -> allProjects.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }
}