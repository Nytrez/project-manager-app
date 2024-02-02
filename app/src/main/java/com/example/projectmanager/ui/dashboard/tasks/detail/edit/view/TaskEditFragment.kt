package com.example.projectmanager.ui.dashboard.tasks.detail.edit.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponseItem
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentTaskDetailsEditBinding
import com.example.projectmanager.ui.dashboard.tasks.detail.edit.viewmodel.SharedViewModel
import com.example.projectmanager.ui.dashboard.tasks.detail.edit.viewmodel.TaskEditViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class TaskEditFragment : Fragment() {

    val LOG_TAG = "TaskEditFragment"

    private var _binding: FragmentTaskDetailsEditBinding? = null
    lateinit var taskEditFragmentViewModel: TaskEditViewModel
    private val args: TaskEditFragmentArgs by navArgs()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskEditFragmentViewModel = ViewModelProvider(this)[TaskEditViewModel::class.java]

        binding.buttontaskDetailsRemove.setOnClickListener {
            taskEditFragmentViewModel.deleteTask(args.taskId)
        }

        binding.buttontaskDetailsEdit.setOnClickListener {
            tryEditTask()
        }

        binding.taskDetailsEditResponsiblePerson.setOnClickListener {
            val dialog = UserSelectDialogFragment.newInstance(args.projectId, args.taskId)
            dialog.show(childFragmentManager, "UserSelectDialogFragment")
        }

        binding.taskDetailsEditCompletionDateBox.setStartIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select completion date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, datePicker.toString())

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.taskDetailsEditCompletionDate.setText(dateFormatter.format(selection))
            }
        }

        binding.taskDetailsEditDueDateBox.setStartIconOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select completion date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.show(childFragmentManager, datePicker.toString())

            datePicker.addOnPositiveButtonClickListener { selection ->
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.taskDetailsEditDueDate.setText(dateFormatter.format(selection))
            }
        }

        Log.d(LOG_TAG, "TaskId: ${args.taskId}")
        observeResponses()
    }

    private fun tryEditTask() {
        val taskDescriptionShort = binding.taskDetailsEditDescriptionShort.text.toString()
        val taskHeader = binding.taskDetailsEditDescriptionHeader.text.toString()
        val taskDescription = binding.taskDetailsEditDescription.text.toString()
        val responsiblePersonEmail = binding.taskDetailsEditResponsiblePerson.text?.toString()
        val priority = binding.taskDetailsEditPriority.value.toInt()
        val status = binding.taskDetailsEditStatus.value.toInt()
        val dueDate = binding.taskDetailsEditDueDate.text.toString()
        val completionDate = binding.taskDetailsEditCompletionDate.text?.toString()?.let {
            it.ifBlank { null }
        }


        taskEditFragmentViewModel.updateTask(
            args.taskId,
            taskDescriptionShort,
            taskHeader,
            taskDescription,
            responsiblePersonEmail,
            priority,
            status,
            dueDate,
            completionDate
        )

    }

    override fun onStart() {
        super.onStart()
        val sharedViewModel: SharedViewModel by activityViewModels()
        if (sharedViewModel.sharedData.value.isNullOrEmpty()) {
            taskEditFragmentViewModel.getTaskDetails(args.taskId)
        }
    }

    fun onUserSelected(user: UserDetailsProjectResponseItem) {
        binding.taskDetailsEditResponsiblePerson.setText(user.email)
    }

    private fun observeResponses() {
        taskEditFragmentViewModel.taskDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()

                    response.data?.let {
                        binding.taskDetailsEditDescriptionShort.setText(it.taskDescriptionShort)
                        binding.taskDetailsEditDescriptionHeader.setText(it.taskHeader)
                        binding.taskDetailsEditDescription.setText(it.taskDescription)
                        binding.taskDetailsEditResponsiblePerson.setText(it.taskResponsiblePersonEmail ?: "")
                        binding.taskDetailsEditPriority.value = it.taskPriority.toFloat()
                        binding.taskDetailsEditStatus.value = it.taskStatus.toFloat()
                        binding.taskDetailsEditDueDate.setText(it.dueDate?.toString() ?: "")
                        binding.taskDetailsEditCompletionDate.setText(it.completionDate?.toString() ?: "")
                    }
                }

                is Resource.Error -> {
                    Log.d(LOG_TAG, "Error: ${response.message}")
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.message?.let { message ->
                       Snackbar.make(binding.root, "An error occurred: $message", Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    (activity as? ProjectsActivity)?.showLoadingIndicator()
                }
            }
        }

        taskEditFragmentViewModel.taskEditResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    Snackbar.make(binding.root, "Successfully updated", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    Log.d(LOG_TAG, "Error: ${response.message}")
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.message?.let { message ->
                        Snackbar.make(binding.root, "An error occurred: $message", Snackbar.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    (activity as? ProjectsActivity)?.showLoadingIndicator()
                }
            }
        }

        taskEditFragmentViewModel.taskDeleteResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    Snackbar.make(binding.root, "Successfully deleted", Snackbar.LENGTH_LONG).show()
                    findNavController().popBackStack(R.id.navigation_project_tasks, true)
                }

                is Resource.Error -> {
                    Log.d(LOG_TAG, "Error: ${response.message}")
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.message?.let { message ->
                        Snackbar.make(binding.root, "An error occurred: $message", Snackbar.LENGTH_LONG).show()
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