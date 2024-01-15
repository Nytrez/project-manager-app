package com.example.projectmanager.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmanager.R
import com.example.projectmanager.ui.login.login.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_main)

        // Add the login fragment to the 'fragment_container' FrameLayout
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, LoginFragment())
            .commit()
    }
}