package com.example.projectmanager.ui.dashboard.tasks.detail.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projectmanager.R
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponseItem
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentTaskDetailsEditBinding
import com.example.projectmanager.ui.dashboard.projects.manage.details.ProjectManageFragmentArgs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskEditFragment : Fragment() {

    val LOG_TAG = "TaskEditFragment"

    private var _binding: FragmentTaskDetailsEditBinding? = null
    lateinit var taskEditFragmentViewModel: TaskEditViewModel
    private val args: TaskEditFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        binding.taskDetailsEditCompletionDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.taskDetailsEditCompletionDate.text = sdf.format(calendar.time)
            }

            DatePickerDialog(
                requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.taskDetailsEditDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.taskDetailsEditDueDate.text = sdf.format(calendar.time)
            }

            DatePickerDialog(
                requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        Log.d(LOG_TAG, "TaskId ale penera: ${args.taskId}")
        //taskEditFragmentViewModel.getTaskDetails(args.taskId)
        observeResponses()
    }

    private fun tryEditTask() {
        //taskId: Int, taskDescriptionShort: String, taskHeader: String, taskDescription: String, responsiblePersonId: Int, priority: Int,
        //        status: Int, dueDate: String, completionDate: String

        val taskDescriptionShort = binding.taskDetailsEditDescriptionShort.text.toString()
        val taskHeader = binding.taskDetailsEditDescriptionHeader.text.toString()
        val taskDescription = binding.taskDetailsEditDescription.text.toString()
        val responsiblePersonId = binding.taskDetailsEditResponsiblePerson.text?.toString()?.let {
            if (it.isBlank()) null else it.toInt()
        }
        val priority = binding.taskDetailsEditPriority.text.toString().toInt()
        val status = binding.taskDetailsEditStatus.text.toString().toInt()
        val dueDate = binding.taskDetailsEditDueDate.text.toString()
        val completionDate = binding.taskDetailsEditCompletionDate.text?.toString()?.let {
            it.ifBlank { null }
        }


        taskEditFragmentViewModel.updateTask(
            args.taskId,
            taskDescriptionShort,
            taskHeader,
            taskDescription,
            responsiblePersonId,
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
        // Handle the selected user
        binding.taskDetailsEditResponsiblePerson.text = user.email
    }

    private fun observeResponses() {
//        val sharedViewModel: SharedViewModel by activityViewModels()
//        sharedViewModel.sharedData.observe(viewLifecycleOwner) { data ->
//            Log.d(LOG_TAG, "User2: $data")
//            binding.taskDetailsEditResponsiblePerson.text = data
//        }

        taskEditFragmentViewModel.taskDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()

                    response.data?.let {
                        binding.taskDetailsEditDescriptionShort.setText(it.taskDescriptionShort)
                        binding.taskDetailsEditDescriptionHeader.setText(it.taskHeader)
                        binding.taskDetailsEditDescription.setText(it.taskDescription)
                        binding.taskDetailsEditResponsiblePerson.text = if (it.taskResponsiblePersonId == null) "" else it.taskResponsiblePersonId.toString()
                        binding.taskDetailsEditPriority.setText(it.taskPriority.toString())
                        binding.taskDetailsEditStatus.setText(it.taskStatus.toString())
                        binding.taskDetailsEditDueDate.text = it.dueDate.toString()
                        if(it.completionDate == null) {
                            binding.taskDetailsEditCompletionDate.visibility = View.GONE
                        } else
                            binding.taskDetailsEditCompletionDate.text = it.completionDate.toString()
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

        taskEditFragmentViewModel.taskEditResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    Toast.makeText(activity, "Successfully edited", Toast.LENGTH_SHORT).show()
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

        taskEditFragmentViewModel.taskDeleteResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    Toast.makeText(activity, "Successfully deleted", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.navigation_project_tasks, true)
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