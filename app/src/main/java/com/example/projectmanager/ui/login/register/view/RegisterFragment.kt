package com.example.projectmanager.ui.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.ActivityLoginRegisterBinding
import com.example.projectmanager.ui.login.login.LoginFragment

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
            // Replace the login fragment with the register fragment
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
    private fun tryRegister(){
        val email = _binding.activityLoginRegisterEmail.text.trim().toString()
        val password = _binding.activityLoginRegisterPassword.text.trim().toString()
        val firstName = _binding.activityLoginRegisterName.text.trim().toString()
        val lastName = _binding.activityLoginRegisterSurname.text.trim().toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginActivityViewModel.register(email, password, firstName, lastName)
        } else {
            Toast.makeText(requireContext(), "Please fill out all the fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeLoginResponse() {
        loginActivityViewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        Toast.makeText(requireContext(), "Registering successful", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, LoginFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(requireContext(), "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    //showProgressBar()
                }
            }
        }
    }
}