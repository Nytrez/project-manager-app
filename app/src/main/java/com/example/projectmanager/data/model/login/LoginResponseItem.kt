package com.example.projectmanager.data.model.login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponseItem (
    @field:SerializedName("token")
    var token: String,
) : Serializable
