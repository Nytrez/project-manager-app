package com.example.projectmanager.ui.dashboard.projects.manage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.projects.ProjectsResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
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
                handleProjectsResponse(response)
            } catch (t:Throwable){
                when(t) {
                    is IOException -> allProjectsOwner.postValue(Resource.Error("Network Failure"))
                    else -> allProjectsOwner.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleProjectsResponse(response: Response<ResponseWrapper<ProjectsResponse>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if(responseBody.body != null) {
                    allProjectsOwner.postValue(Resource.Success(responseBody.body))
                } else {
                    allProjectsOwner.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                allProjectsOwner.postValue(Resource.Error("Response body is null"))
            }
        } else {
            allProjectsOwner.postValue(Resource.Error(response.message()))
        }
    }
}