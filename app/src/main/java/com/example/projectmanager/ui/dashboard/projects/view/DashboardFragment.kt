package com.example.projectmanager.ui.dashboard.projects

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentProjectsBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private lateinit var projectsAdapter: ProjectsAdapter
    lateinit var dashboardViewModel: DashboardViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
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
            Log.d("DashboardFragment", "ProjectId: ${it.projectId}")
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
                    //hideProgressBar()
                    response.data?.let {
                        projectsAdapter.differ.submitList(it)
                    }
                }

                is Resource.Error -> {
                    Log.d("DashboardFragment", "Error: ${response.message}")
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