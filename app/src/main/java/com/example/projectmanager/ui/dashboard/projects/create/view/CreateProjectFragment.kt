package com.example.projectmanager.ui.dashboard.projects.create.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentAddProjectBinding
import com.example.projectmanager.ui.dashboard.projects.create.viewmodel.CreateProjectViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import isValidDateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class CreateProjectFragment : Fragment() {

    val LOG_TAG = "CreateProjectFragment"

    private var _binding: FragmentAddProjectBinding? = null
    lateinit var createProjectViewModel: CreateProjectViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProjectBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createProjectViewModel = ViewModelProvider(this)[CreateProjectViewModel::class.java]


        binding.btnAddProject.setOnClickListener {
            tryCreateProject()
            observeProjectCreateResponse()
        }

        binding.addProjectStartDateBox.setStartIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select completion date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, datePicker.toString())

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.addProjectStartDate.setText(dateFormatter.format(selection))
            }
        }

        binding.addProjectEndDateBox.setStartIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select completion date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, datePicker.toString())

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.addProjectEndDate.setText(dateFormatter.format(selection))
            }
        }
    }

    private fun tryCreateProject() {
        val projectName = binding.addProjectName.text?.trim().toString()
        val projectDescription = binding.addProjectDescription.text?.trim().toString()
        val projectEstimatedEndDateString = binding.addProjectEndDate.text?.trim().toString()
        val projectStartDateString = binding.addProjectStartDate.text?.trim().toString()

        if (!isValidDateFormat(projectStartDateString)) {
            binding.addProjectStartDate.error = "Invalid date format (different from yyyy-MM-dd)"
            return
        }

        if (!isValidDateFormat(projectEstimatedEndDateString)) {
            binding.addProjectEndDate.error = "Invalid date format (different from yyyy-MM-dd)"
            return
        }


        if (projectName.isNotEmpty() && projectDescription.isNotEmpty() && projectStartDateString.isNotEmpty() && projectEstimatedEndDateString.isNotEmpty()) {
            createProjectViewModel.createProject(projectName, projectDescription, projectStartDateString, projectEstimatedEndDateString)
        } else {
            Snackbar.make(requireView(), "Please fill out all fields.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        //dashboardViewModel.getAllProjects()
    }

    private fun observeProjectCreateResponse() {
        createProjectViewModel.projectCreateResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    Snackbar.make(binding.root, "Project created successfully.", Snackbar.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    Log.d(LOG_TAG, "Error: ${response.message}")
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.message?.let { message ->
                        Snackbar.make(binding.root, "An error occurred: $message", Snackbar.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    (activity as? ProjectsActivity)?.showLoadingIndicator()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}