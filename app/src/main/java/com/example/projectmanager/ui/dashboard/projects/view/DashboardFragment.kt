package com.example.projectmanager.ui.dashboard.projects.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentProjectsBinding
import com.example.projectmanager.ui.dashboard.projects.viewmodel.DashboardViewModel
import com.google.android.material.snackbar.Snackbar

class DashboardFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private lateinit var projectsAdapter: ProjectsAdapter
    lateinit var dashboardViewModel: DashboardViewModel

    val LOG_TAG = "DashboardFragment"
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        setUpRecyclerView()

        binding.projectsAddProjectButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_createProjectFragment)
        }

        binding.projectsManageProjectsButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_projectManageFragment)
        }

        projectsAdapter.setOnItemClickListener {
            Log.d(LOG_TAG, "ProjectId: ${it.projectId}")
            val bundle = Bundle().apply {
                putInt("projectId", it.projectId)
            }
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_projectTasksFragment,
                bundle
            )
        }
        observeProjects()
    }


    override fun onStart() {
        super.onStart()
        dashboardViewModel.getAllProjects()
    }

    private fun observeProjects() {
        dashboardViewModel.allProjects.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        projectsAdapter.differ.submitList(it)
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

    }

    private fun setUpRecyclerView() {
        projectsAdapter = ProjectsAdapter()
        binding.rvProjects.apply {
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}