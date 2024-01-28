package com.example.projectmanager.ui.dashboard.tasks.detail.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.example.projectmanager.data.model.tasks.comments.TaskCommentRequest
import com.example.projectmanager.data.model.tasks.comments.TaskCommentsResponse
import com.example.projectmanager.data.model.tasks.manage.TaskDetailsRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.SessionManager
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class TaskDetailsViewModel() : ViewModel() {

    val LOG_TAG = "TaskDetailsViewModel"

    val taskDetails: MutableLiveData<Resource<TasksResponseItem>> = MutableLiveData()
    val comments: MutableLiveData<Resource<TaskCommentsResponse>> = MutableLiveData()
    val commentPost: MutableLiveData<Resource<Unit>> = MutableLiveData()


    fun getAllTaskDetails(taskId : Int) {
        viewModelScope.launch {
            try {
                taskDetails.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                //val task = TaskDetailsRequest(taskId)
                val response = RetrofitBuilder.api.getTaskDetails(SessionManager.fetchAuthToken()!!, taskId)
                handleTaskDetailsResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting tasks: $t")
                when (t) {
                    is IOException -> taskDetails.postValue(Resource.Error("Network Failure"))
                    else -> taskDetails.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }



    fun getAllComments(taskId : Int){
        viewModelScope.launch {
            try {
                comments.postValue(Resource.Loading())
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.getTaskAllComments(SessionManager.fetchAuthToken()!!, taskId)
                handleCommentsResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> comments.postValue(Resource.Error("Network Failure"))
                    else -> comments.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleCommentsResponse(response: Response<ResponseWrapper<TaskCommentsResponse>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if(responseBody.body != null) {
                    comments.postValue(Resource.Success(responseBody.body))
                } else {
                    comments.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                comments.postValue(Resource.Error("Response body is null"))
            }
        } else {
            comments.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleTaskDetailsResponse(response: Response<ResponseWrapper<TasksResponseItem>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if (responseBody.body != null) {
                    taskDetails.postValue(Resource.Success(responseBody.body))
                } else {
                    taskDetails.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                taskDetails.postValue(Resource.Error("Response body is null"))
            }
        } else {
            taskDetails.postValue(Resource.Error(response.message()))
        }
    }

    private fun handleCommentPostResponse(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                    commentPost.postValue(Resource.Success(Unit))
            } else {
                commentPost.postValue(Resource.Error("Response body is null"))
            }
        } else {
            commentPost.postValue(Resource.Error(response.message()))
        }
    }

    fun postComment(taskId: Int, commentText: String) {
        viewModelScope.launch {
            try {
                commentPost.postValue(Resource.Loading())
                val request = TaskCommentRequest(taskId, commentText)
                //Log.d("TasksViewModel", "getAllProjectTasks: $userToken")
                val response = RetrofitBuilder.api.addCommentToTask(SessionManager.fetchAuthToken()!!, request)
                handleCommentPostResponse(response)
            } catch (t: Throwable) {
                Log.d(LOG_TAG, "error whhile requesting comments: $t")
                when (t) {
                    is IOException -> commentPost.postValue(Resource.Error("Network Failure"))
                    else -> commentPost.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}
