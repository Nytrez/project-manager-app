package com.example.projectmanager.ui.dashboard.tasks.state.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentTasksInProgressBinding
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksAdapter
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksViewModel
import com.google.android.material.snackbar.Snackbar


class TasksInProgressFragment : Fragment() {

    val LOG_TAG = "TasksInProgressFragment"

    private var _binding: FragmentTasksInProgressBinding? = null
    private lateinit var tasksAdapter: TasksAdapter
    lateinit var tasksViewModel: TasksViewModel

    private val args: TasksInProgressFragmentArgs by navArgs()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksInProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        setUpRecyclerView()
        tasksViewModel.getAllProjectTasks(args.projectId, 1)


        tasksAdapter.setOnItemClickListener {
            Log.d(LOG_TAG, "ProjectId: ${it.taskId}")
            val bundle = Bundle().apply {
                putInt("taskId", it.taskId)
                putInt("projectId", it.taskProjectId)
            }
            findNavController().navigate(
                R.id.action_navigation_to_taskDetailsFragment,
                bundle
            )
        }

        observeProjects()
    }

    private fun observeProjects() {
        tasksViewModel.allProjectTasks.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        tasksAdapter.differ.submitList(it)
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

    }

    private fun setUpRecyclerView() {
        tasksAdapter = TasksAdapter()
        binding.tasksRvInProgress.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}