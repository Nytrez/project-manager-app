package com.example.projectmanager.ui.dashboard.projects.manage.details.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentManageProjectsItemBinding
import com.example.projectmanager.ui.dashboard.projects.manage.details.viewmodel.ProjectManageViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class ProjectManageFragment : Fragment() {

    val LOG_TAG = "ManageProjectsFragmentItem"

    private var _binding: FragmentManageProjectsItemBinding? = null
    lateinit var projectManageViewModel: ProjectManageViewModel
    private val args: ProjectManageFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageProjectsItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        projectManageViewModel = ViewModelProvider(this)[ProjectManageViewModel::class.java]

        binding.projectManageRemoveProjectBtn.setOnClickListener {
            projectManageViewModel.removeProject(args.projectId)
            Snackbar.make(view, "Project removed", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.projectManageManagerUsersBtn.setOnClickListener {

            //Log.d("DashboardFragment", "ProjectId: ${it.projectId}")
            val bundle = Bundle().apply {
                putInt("projectId", args.projectId)
            }
            findNavController().navigate(
                R.id.action_navigation_to_manageUsersFragment,
                bundle
            )
        }

        binding.projectManageProjectStartDateBox.setStartIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select completion date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, datePicker.toString())

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.projectManageProjectStartDate.setText(dateFormatter.format(selection))
            }
        }

        binding.projectManageProjectEndDateBox.setStartIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select completion date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, datePicker.toString())

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.projectManageProjectEndDate.setText(dateFormatter.format(selection))
            }
        }

        binding.projectManageSaveChangesBtn.setOnClickListener {

            val newName = binding.projectManageProjectName.text?.trim().toString()
            val newDescription = binding.projectManageProjectDescription.text?.trim().toString()
            val newStartDate = binding.projectManageProjectStartDate.text?.trim().toString()
            val newEndDate = binding.projectManageProjectEndDate.text?.trim().toString()

            projectManageViewModel.changeProjectDetails(
                args.projectId,
                newName,
                newDescription,
                newStartDate,
                newEndDate,
                "Inactive"
            )

            Snackbar.make(view, "Project details changed", Snackbar.LENGTH_SHORT).show()
        }
        projectManageViewModel.getProjectDetails(args.projectId)
        observeResponses()
    }


    private fun observeResponses() {
        projectManageViewModel.projectDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        binding.projectManageProjectName.setText(it.projectName)
                        binding.projectManageProjectDescription.setText(it.projectDescription)
                        binding.projectManageProjectStartDate.setText(it.projectStartDate)
                        binding.projectManageProjectEndDate.setText(it.projectEstimatedEndDate)
                    }
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



        projectManageViewModel.projectChangeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        //TODO RETURN

                        Snackbar.make(binding.root, "Project details changed", Snackbar.LENGTH_SHORT).show()
                        //findNavController().popBackStack()
                    }
                }

                is Resource.Error -> {
                    Log.d(LOG_TAG, "Error: ${response.message}")
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.message?.let { message ->
                        Snackbar.make(requireView(), "An error occurred: $message", Snackbar.LENGTH_SHORT).show()
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