package com.example.projectmanager.ui.util

import androidx.lifecycle.MutableLiveData
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.data.util.ResponseWrapper
import retrofit2.Response

inline fun <reified T, R> handleApiResponse(
    response: Response<ResponseWrapper<T>>,
    liveData: MutableLiveData<Resource<R>>,
    noinline transform: (T) -> R = { it as R }
) {
    if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
            // Special handling for Unit type
            if (T::class == Unit::class) {
                @Suppress("UNCHECKED_CAST")
                liveData.postValue(Resource.Success(Unit as R))
            } else {
                responseBody.body?.let {
                    liveData.postValue(Resource.Success(transform(it)))
                } ?: liveData.postValue(Resource.Error(responseBody.reason))
            }
        } else {
            liveData.postValue(Resource.Error("Response body is null"))
        }
    } else {
        liveData.postValue(Resource.Error(response.message() ?: "Unknown error"))
    }
}

