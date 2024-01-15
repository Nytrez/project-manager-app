package com.example.projectmanager.ui.dashboard.tasks.detail.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val sharedData = MutableLiveData<String>()
}