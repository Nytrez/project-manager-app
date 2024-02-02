package com.example.projectmanager.ui.dashboard.tasks.kanban.view

//import com.example.projectmanager.ui.dashboard.ProjectTasksFragmentArgs
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentProjectTasksBinding
import com.example.projectmanager.ui.dashboard.tasks.kanban.viewmodel.ProjectTasksFragmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class ProjectTasksFragment : Fragment() {
    private var _binding: FragmentProjectTasksBinding? = null
    private lateinit var projectsTasksViewModel: ProjectTasksFragmentViewModel
    private val args: ProjectTasksFragmentArgs by navArgs()

    val LOG_TAG = "ProjectTasksFragment"

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        projectsTasksViewModel = ViewModelProvider(this)[ProjectTasksFragmentViewModel::class.java]

        binding.viewPager.adapter = ViewPagerAdapter(this, args.projectId)

        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener { view ->
            showInputDialog()
        }

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "To Do"
                1 -> "In Progress"
                2 -> "Done"
                else -> null
            }
        }.attach()

        return root
    }

    private fun showInputDialog() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Enter task details")

        val inputLayout = LayoutInflater.from(context).inflate(R.layout.dialog_create_task, null)
        builder.setView(inputLayout)

        val headerInput = inputLayout.findViewById<EditText>(R.id.dialogCreateTaskHeader)
        val descriptionInput = inputLayout.findViewById<EditText>(R.id.dialogCreateTaskDescription)
        val descriptionShortInput = inputLayout.findViewById<EditText>(R.id.dialogCreateTaskDescriptionShort)
        val priorityInput = inputLayout.findViewById<Slider>(R.id.dialogCreateTaskPriority)

        builder.setPositiveButton("Create") { _, _ ->
            val headerText = headerInput.text.toString()
            val descriptionText = descriptionInput.text.toString()
            val descriptionShortText = descriptionShortInput.text.toString()
            val priorityText = priorityInput.value.toInt()

            if (headerText.isEmpty() || descriptionText.isEmpty() || descriptionShortText.isEmpty()) {
                Snackbar.make(requireView(), "Please fill all the fields", Snackbar.LENGTH_LONG).show()
                return@setPositiveButton
            }

            Log.d(LOG_TAG, "showInputDialog: $headerText, $descriptionText, $descriptionShortText, $priorityText")

            projectsTasksViewModel.addTaskToProject(args.projectId, headerText, descriptionText, descriptionShortText, priorityText)

            observeTaskCreateResponse()

        }
        builder.setNegativeButton("Cancel", null)

        builder.show()
    }

    private fun observeTaskCreateResponse() {
        projectsTasksViewModel.taskCreateResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        //projectsAdapter.differ.submitList(it) // TODO
                    }
                }

                is Resource.Error -> {
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