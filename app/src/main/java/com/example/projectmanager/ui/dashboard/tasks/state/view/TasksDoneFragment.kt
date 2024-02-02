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
import com.example.projectmanager.databinding.FragmentTasksDoneBinding
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksAdapter
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksViewModel
import com.google.android.material.snackbar.Snackbar


class TasksDoneFragment : Fragment() {

    val LOG_TAG = "TasksDoneFragment"

    private var _binding: FragmentTasksDoneBinding? = null
    private lateinit var tasksAdapter: TasksAdapter
    lateinit var tasksViewModel: TasksViewModel

    private val args: TasksInProgressFragmentArgs by navArgs()
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksDoneBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksViewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        setUpRecyclerView()
        tasksViewModel.getAllProjectTasks(args.projectId, 2)


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
                        Snackbar.make(binding.root, "Error: $message", Snackbar.LENGTH_LONG).show()
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
        binding.tasksRvDone.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}