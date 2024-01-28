package com.example.projectmanager.ui.dashboard.tasks.state.view

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentTasksToDoBinding
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksAdapter
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksViewModel
import com.example.projectmanager.ui.dashboard.tasks.state.viewmodel.TasksViewModelFactory

class TasksToDoFragment : Fragment() {

    val LOG_TAG = "TasksToDoFragment"

    private var _binding: FragmentTasksToDoBinding? = null
    private lateinit var tasksAdapter: TasksAdapter
    lateinit var tasksViewModel: TasksViewModel

    private val args: TasksToDoFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksToDoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TasksToDo", "ProjectId: ${args.projectId}")
        val factory = TasksViewModelFactory(args.projectId, 0)
        tasksViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]

        setUpRecyclerView()

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
                    //hideProgressBar()
                    response.data?.let {
                        tasksAdapter.differ.submitList(it)
                    }
                }

                is Resource.Error -> {
                    Log.d("TasksToDo", "Error: ${response.message}")
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

    private fun setUpRecyclerView(){
        tasksAdapter = TasksAdapter()
        binding.tasksRvToDo.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}