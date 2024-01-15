package com.example.projectmanager.ui.login.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.api.RetrofitBuilder
import com.example.projectmanager.data.util.ResponseWrapper
import com.example.projectmanager.data.model.login.LoginRequest
import com.example.projectmanager.data.model.login.LoginResponseItem
import com.example.projectmanager.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class LoginActivityViewModel : ViewModel() {

    val loginResponse: MutableLiveData<Resource<LoginResponseItem>> = MutableLiveData()

//    init {
//        getAllProjects()
//    }


    fun login(email:String, password:String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loginResponse.postValue(Resource.Loading())
                val loginRequest = LoginRequest(email, password)
                val response = RetrofitBuilder.api.login(loginRequest)
                handleLoginResponse(response)
            } catch (t:Throwable){
                when(t) {
                    is IOException -> loginResponse.postValue(Resource.Error("Network Failure"))
                    else -> loginResponse.postValue(Resource.Error(t.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun handleLoginResponse(response: Response<ResponseWrapper<LoginResponseItem>>) {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                if(responseBody.body != null) {
                    loginResponse.postValue(Resource.Success(responseBody.body))
                } else {
                    loginResponse.postValue(Resource.Error(responseBody.reason))
                }
            } else {
                loginResponse.postValue(Resource.Error("Response body is null"))
            }
        } else {
            loginResponse.postValue(Resource.Error(response.message()))
        }
    }
}