package com.example.projectmanager.ui.dashboard.tasks.detail.edit.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponse
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.ui.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class TaskEditSelectUserViewModel : ViewModel() {

    val LOG_TAG = "UserManageProjectsViewModel"

    val projectUsers: MutableLiveData<Resource<UserDetailsProjectResponse>> = MutableLiveData()


    fun getProjectUsers(projectId: Int) {
        viewModelScope.launch {
            try {
                projectUsers.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                //val project = ProjectRequest(projectId)
                val response = RetrofitBuilder.api.getProjectUsers(SessionManager.fetchAuthToken()!!, projectId)
                handleProjectUsersResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> projectUsers.postValue(Resource.Error("Network Failure"))
                    else -> projectUsers.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    // RESPONSE HANDLERS

    private fun handleProjectUsersResponse(response: Response<ResponseWrapper<UserDetailsProjectResponse>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if (responseBody.body != null) {
                    projectUsers.postValue(Resource.Success(responseBody.body))
                } else {
                    projectUsers.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                projectUsers.postValue(Resource.Error("Response body is null"))
            }
        } else {
            projectUsers.postValue(Resource.Error(response.message()))
        }
    }
}