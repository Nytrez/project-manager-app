package com.example.projectmanager.ui.dashboard.projects.manage.details.view

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
import androidx.navigation.fragment.navArgs
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentManageProjectsItemBinding
import com.example.projectmanager.ui.dashboard.projects.manage.details.viewmodel.ProjectManageViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
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
            Toast.makeText(activity, "Project removed", Toast.LENGTH_SHORT).show()
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

        binding.projectManageProjectStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.projectManageProjectStartDate.text = sdf.format(calendar.time)
            }

            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.projectManageProjectEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.projectManageProjectEndDate.text = sdf.format(calendar.time)
            }

            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }


        binding.projectManageSaveChangesBtn.setOnClickListener {

            val newName = binding.projectManageProjectName.text.trim().toString()
            val newDescription = binding.projectManageProjectDescription.text.trim().toString()
            val newStartDate = binding.projectManageProjectStartDate.text.trim().toString()
            val newEndDate = binding.projectManageProjectEndDate.text.trim().toString()

            projectManageViewModel.changeProjectDetails(
                args.projectId,
                newName,
                newDescription,
                newStartDate,
                newEndDate,
                "Inactive"
            )

            Toast.makeText(activity, "Changes saved", Toast.LENGTH_SHORT).show()
        }
        projectManageViewModel.getProjectDetails(args.projectId)
        observeResponses()
    }



    private fun observeResponses() {
        projectManageViewModel.projectDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        binding.projectManageProjectName.setText(it.projectName)
                        binding.projectManageProjectDescription.setText(it.projectDescription)
                        binding.projectManageProjectStartDate.text = it.projectStartDate
                        binding.projectManageProjectEndDate.text = it.projectEstimatedEndDate
                    }
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



        projectManageViewModel.projectChangeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        //TODO RETURN

                        Toast.makeText(activity, "something happened", Toast.LENGTH_SHORT).show()
                        //findNavController().popBackStack()
                    }
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