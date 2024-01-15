package com.example.projectmanager.data.util

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseWrapper<T>(
    @field:SerializedName("reason")
    val reason: String,
    val body: T? = null
) : Serializable