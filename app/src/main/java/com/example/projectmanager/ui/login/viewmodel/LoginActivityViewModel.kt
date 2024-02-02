package com.example.projectmanager.ui.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.model.login.LoginRequest
import com.example.projectmanager.data.model.login.LoginResponseItem
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.util.handleApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class LoginActivityViewModel : ViewModel() {

    val loginResponse: MutableLiveData<Resource<LoginResponseItem>> = MutableLiveData()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loginResponse.postValue(Resource.Loading())
                val loginRequest = LoginRequest(email, password)
                val response = RetrofitBuilder.api.login(loginRequest)
                handleApiResponse(response, loginResponse)
            } catch (t: Throwable) {
                when (t) {
                    is IOException -> loginResponse.postValue(Resource.Error("Network Failure"))
                    else -> loginResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }
}