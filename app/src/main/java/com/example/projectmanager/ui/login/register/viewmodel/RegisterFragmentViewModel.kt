package com.example.projectmanager.ui.login.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.login.RegisterRequest
import com.example.projectmanager.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RegisterFragmentViewModel : ViewModel() {

    val registerResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()

    fun register(email: String, password: String, name: String, surname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerResponse.postValue(Resource.Loading())
                val registerRequest = RegisterRequest(email, password, name, surname)
                val response = RetrofitBuilder.api.register(registerRequest)
                handleRegisterResponse(response)
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> registerResponse.postValue(Resource.Error("Network Failure"))
                    else -> registerResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleRegisterResponse(response: Response<ResponseWrapper<Unit>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
//                if (responseBody.body != null) {
//                    registerResponse.postValue(Resource.Success(responseBody.body))
//                } else {
                    registerResponse.postValue(Resource.Success(Unit))
                //}
            } else {
                registerResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            registerResponse.postValue(Resource.Error(response.message()))
        }
    }
}