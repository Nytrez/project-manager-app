package com.example.projectmanager.ui.login.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmanager.R
import com.example.projectmanager.databinding.ActivityLoginMainBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_login_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, LoginFragment())
            .commit()
    }

    fun showLoadingIndicator() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    fun hideLoadingIndicator() {
        binding.progressIndicator.visibility = View.GONE
    }
}