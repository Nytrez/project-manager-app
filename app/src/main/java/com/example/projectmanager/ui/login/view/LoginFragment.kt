package com.example.projectmanager.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.ActivityLoginLoginBinding
import com.example.projectmanager.ui.login.register.view.RegisterFragment
import com.example.projectmanager.ui.login.viewmodel.LoginActivityViewModel
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private lateinit var _binding: ActivityLoginLoginBinding
    private lateinit var loginActivityViewModel: LoginActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityLoginLoginBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.buttonRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        loginActivityViewModel = ViewModelProvider(this)[LoginActivityViewModel::class.java]

        _binding.buttonLogin.setOnClickListener {
            tryLogin()
            observeLoginResponse()
        }
    }

    private fun tryLogin() {
        val email = _binding.activityLoginEnterEmail.text?.trim().toString()
        val password = _binding.activityLoginEnterPassword.text?.trim().toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // TODO Turn on after testing
//            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                Snackbar.make(requireView(), "Invalid email", Snackbar.LENGTH_LONG).show()
//                return
//            }
            loginActivityViewModel.login(email, password)
        } else {
            Snackbar.make(requireView(), "Please fill all the fields", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun observeLoginResponse() {
        loginActivityViewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? LoginActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        com.example.projectmanager.ui.util.SessionManager.saveAuthToken(it.token)
                        Snackbar.make(requireView(), "Login successful", Snackbar.LENGTH_LONG).show()
                        val intent = Intent(requireActivity(), ProjectsActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }

                is Resource.Error -> {
                    (activity as? LoginActivity)?.hideLoadingIndicator()
                    response.message?.let { message ->
                        Snackbar.make(requireView(), "An error occurred: $message", Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    (activity as? LoginActivity)?.showLoadingIndicator()
                }
            }
        }
    }
}