package com.example.projectmanager.ui.dashboard.projects.manage.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentManageProjectsBinding
import com.example.projectmanager.ui.dashboard.projects.manage.viewmodel.ManageProjectsViewModel
import com.example.projectmanager.ui.dashboard.projects.view.ProjectsAdapter
import com.google.android.material.snackbar.Snackbar

class ManageProjectsFragment : Fragment() {

    val LOG_TAG = "ManageProjectsFragment"

    private var _binding: FragmentManageProjectsBinding? = null
    private lateinit var projectsAdapter: ProjectsAdapter
    lateinit var dashboardViewModel: ManageProjectsViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageProjectsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = ViewModelProvider(this)[ManageProjectsViewModel::class.java]

        setUpRecyclerView()

        projectsAdapter.setOnItemClickListener {
            Log.d(LOG_TAG, "ProjectId: ${it.projectId}")
            val bundle = Bundle().apply {
                putInt("projectId", it.projectId)
            }
            findNavController().navigate(
                R.id.action_navigation_dashboard_to_projectManageProjectFragment,
                bundle
            )
        }

        observeProjects()
    }

    private fun observeProjects() {
        dashboardViewModel.allProjectsOwner.observe(viewLifecycleOwner) { response ->
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
        projectsAdapter = ProjectsAdapter()
        binding.projectManageRv.apply {
            adapter = projectsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}