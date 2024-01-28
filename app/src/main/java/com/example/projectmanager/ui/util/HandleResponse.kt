package com.example.projectmanager.ui.util

import androidx.lifecycle.MutableLiveData
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.data.util.ResponseWrapper
import retrofit2.Response

fun <T> handleApiResponse(
    response: Response<ResponseWrapper<T>>,
    liveData: MutableLiveData<Resource<T>>
) {
    if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
            responseBody.body?.let {
                liveData.postValue(Resource.Success(it))
            } ?: liveData.postValue(Resource.Error(responseBody.reason))
        } else {
            liveData.postValue(Resource.Error("Response body is null"))
        }
    } else {
        liveData.postValue(Resource.Error(response.message() ?: "Unknown error"))
    }
}
