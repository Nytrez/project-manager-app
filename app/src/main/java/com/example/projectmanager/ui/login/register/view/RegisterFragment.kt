package com.example.projectmanager.ui.login.register.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.ActivityLoginRegisterBinding
import com.example.projectmanager.ui.login.register.viewmodel.RegisterFragmentViewModel
import com.example.projectmanager.ui.login.view.LoginActivity
import com.example.projectmanager.ui.login.view.LoginFragment
import com.google.android.material.snackbar.Snackbar

class RegisterFragment : Fragment() {

    private lateinit var _binding: ActivityLoginRegisterBinding
    private lateinit var loginActivityViewModel: RegisterFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ActivityLoginRegisterBinding.inflate(inflater, container, false)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.buttonBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }

        loginActivityViewModel = ViewModelProvider(this)[RegisterFragmentViewModel::class.java]

        _binding.buttonRegisterRequest.setOnClickListener {
            tryRegister()
            observeLoginResponse()
        }
    }

    private fun tryRegister() {
        val email = _binding.activityLoginRegisterEmail.text?.trim().toString()
        val password = _binding.activityLoginRegisterPassword.text?.trim().toString()
        val firstName = _binding.activityLoginRegisterName.text?.trim().toString()
        val lastName = _binding.activityLoginRegisterSurname.text?.trim().toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(requireView(), "Invalid email", Snackbar.LENGTH_LONG).show()
                return
            }
            loginActivityViewModel.register(email, password, firstName, lastName)
        } else {
            Snackbar.make(requireView(), "Please fill all the fields", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun observeLoginResponse() {
        loginActivityViewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? LoginActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        Snackbar.make(requireView(), "Registering successful", Snackbar.LENGTH_LONG).show()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LoginFragment())
                            .addToBackStack(null)
                            .commit()
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