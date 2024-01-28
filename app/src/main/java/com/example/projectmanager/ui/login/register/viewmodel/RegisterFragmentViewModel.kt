package com.example.projectmanager.ui.login.register.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.login.RegisterRequest
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.handleApiResponse
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
                handleApiResponse(response, registerResponse)
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> registerResponse.postValue(Resource.Error("Network Failure"))
                    else -> registerResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

}