package com.example.projectmanager.ui.dashboard.projects.create.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentAddProjectBinding
import com.example.projectmanager.ui.dashboard.projects.create.viewmodel.CreateProjectViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateProjectFragment : Fragment() {

    val LOG_TAG = "CreateProjectFragment"

    private var _binding: FragmentAddProjectBinding? = null
    lateinit var createProjectViewModel: CreateProjectViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
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

        binding.addProjectStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.addProjectStartDate.setText(sdf.format(calendar.time))
            }

            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.addProjectEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.addProjectEndDate.setText(sdf.format(calendar.time))
            }

            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun tryCreateProject() {
        val projectName = binding.addProjectName.text.trim().toString()
        val projectDescription = binding.addProjectDescription.text.trim().toString()
        val projectEstimatedEndDateString = binding.addProjectEndDate.text.trim().toString()
        val projectStartDateString = binding.addProjectStartDate.text.trim().toString()
        //val projectStartDate = binding.addProjectStartDate.text.trim().toString()

//        val myFormat = "yyyy-MM-dd" // format daty, który używasz // TODO Daty?
//        val sdf = SimpleDateFormat(myFormat, Locale.US)


        if (projectName.isNotEmpty() && projectDescription.isNotEmpty() && projectStartDateString.isNotEmpty() && projectEstimatedEndDateString.isNotEmpty()) {
//            val projectStartDate: Date = sdf.parse(projectStartDateString)!!
//            val projectEstimatedEndDate: Date = sdf.parse(projectEstimatedEndDateString)!!
//
//            // Format the date to string before logging
//            val formattedStartDate = sdf.format(projectStartDate)
//            val formattedEndDate = sdf.format(projectEstimatedEndDate)
//
//            Log.d(LOG_TAG, "tryCreateProject start: $formattedStartDate")
//            Log.d(LOG_TAG, "tryCreateProject end: $formattedEndDate")
            createProjectViewModel.createProject(projectName, projectDescription, projectStartDateString, projectEstimatedEndDateString)
        } else {
            Toast.makeText(requireContext(), "Please fill out all the fields.", Toast.LENGTH_SHORT).show()
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
                    //hideProgressBar()
                    Toast.makeText(activity, "Successfully created", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    Log.d(LOG_TAG, "Error: ${response.message}")
                    //hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    //showProgressBar()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}