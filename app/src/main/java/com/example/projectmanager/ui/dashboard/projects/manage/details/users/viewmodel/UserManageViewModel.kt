package com.example.projectmanager.ui.dashboard.projects.manage.details.users.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsChangeProjectRequest
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponse
import com.example.projectmanager.data.model.projects.manage.user.UserProjectAddRequest
import com.example.projectmanager.data.model.projects.manage.user.UserProjectRemoveRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import com.example.projectmanager.ui.util.handleApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserManageViewModel : ViewModel() {

    val LOG_TAG = "UserManageProjectsViewModel"

    val projectUsers : MutableLiveData<Resource<UserDetailsProjectResponse>> = MutableLiveData()
    val userChangeResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val userDeleteResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val userAddResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()


    fun getProjectUsers(projectId: Int) {
        viewModelScope.launch {
            try {
                projectUsers.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.getProjectUsers(SessionManager.fetchAuthToken()!!, projectId)
                handleApiResponse(response, projectUsers)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> projectUsers.postValue(Resource.Error("Network Failure"))
                    else -> projectUsers.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }


    fun updateUser(
        projectId: Int, role: String, permission: Int, userId: Int
    ) {
        viewModelScope.launch {
            try {
                userChangeResponse.postValue(Resource.Loading())

                val projectRequest = UserDetailsChangeProjectRequest(
                    projectId = projectId,
                    userId = userId,
                    role = role,
                    permissions = permission
                )

                val response = RetrofitBuilder.api.changeProjectUserDetails(SessionManager.fetchAuthToken()!!, projectRequest)
                handleApiResponse(response, userChangeResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> userChangeResponse.postValue(Resource.Error("Network Failure"))
                    else -> userChangeResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    fun deleteUser(projectId: Int, userId: Int) {
        viewModelScope.launch {
            try {
                userDeleteResponse.postValue(Resource.Loading())

                val response = RetrofitBuilder.api.removeUserFromProject(SessionManager.fetchAuthToken()!!, projectId, userId)
                handleApiResponse(response, userDeleteResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> userDeleteResponse.postValue(Resource.Error("Network Failure"))
                    else -> userDeleteResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    fun addUser(projectId: Int, userEmail: String, userRole: String, userPermission: Int) {
        viewModelScope.launch {
            try {
                userAddResponse.postValue(Resource.Loading())
                val userAddRequest = UserProjectAddRequest(
                    projectId = projectId,
                    email = userEmail,
                    role = userRole,
                    permissions = userPermission
                )

                val response = RetrofitBuilder.api.addUserToProject(SessionManager.fetchAuthToken()!!, userAddRequest)
                handleApiResponse(response, userAddResponse)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> userAddResponse.postValue(Resource.Error("Network Failure"))
                    else -> userAddResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }

    }
}