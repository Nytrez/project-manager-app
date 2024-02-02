package com.example.projectmanager.ui.dashboard.tasks.detail.view

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
import com.example.projectmanager.ProjectsActivity
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentTaskDetailsBinding
import com.example.projectmanager.ui.dashboard.tasks.detail.viewmodel.TaskDetailsViewModel
import com.google.android.material.snackbar.Snackbar

class TaskDetailsFragment : Fragment() {

    val LOG_TAG = "TaskDetailsFragment"

    private var _binding: FragmentTaskDetailsBinding? = null
    private lateinit var commentsAdapter: CommentsAdapter
    lateinit var taskDetailsViewModel: TaskDetailsViewModel

    private val args: TaskDetailsFragmentArgs by navArgs()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailsViewModel = ViewModelProvider(this)[TaskDetailsViewModel::class.java]

        setUpRecyclerView()

        binding.taskDetailEditButton.setOnClickListener {
            Log.d(LOG_TAG, "ProjectId: ${args.taskId}")
            val bundle = Bundle().apply {
                putInt("taskId", args.taskId)
                putInt("projectId", args.projectId)
            }
            findNavController().navigate(
                R.id.action_navigation_to_editTaskFragment,
                bundle
            )
        }


        binding.taskDetailPostCommentButton.setOnClickListener {
            val commentText = binding.taskDetailCommentInput.text?.trim().toString()
            if (commentText.isNotEmpty()) {
                taskDetailsViewModel.postComment(args.taskId, commentText)
                observeCommentPostResponse()
            } else {
                Snackbar.make(view, "Comment cannot be empty.", Snackbar.LENGTH_SHORT).show()
            }
        }

        // TODO usuwanie komentarzy

        taskDetailsViewModel.getAllTaskDetails(args.taskId)
        taskDetailsViewModel.getAllComments(args.taskId)

        observeProjects()
    }

    private fun observeCommentPostResponse() {
        taskDetailsViewModel.commentPostResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        Snackbar.make(binding.root, "Comment posted successfully.", Snackbar.LENGTH_SHORT).show()
                        taskDetailsViewModel.getAllComments(args.taskId)
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


    private fun observeProjects() {
        taskDetailsViewModel.commentsDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        commentsAdapter.differ.submitList(it)
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

        taskDetailsViewModel.taskDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    (activity as? ProjectsActivity)?.hideLoadingIndicator()
                    response.data?.let {
                        binding.taskDetailsHeader.text = it.taskHeader
                        binding.taskDetailsDescriptionShort.text = it.taskDescriptionShort
                        binding.taskDetailsDescription.text = it.taskDescription
                        binding.taskDetailPriority.text = it.taskPriority.toString()
                        if(it.completionDate == null){
                            binding.taskDetailCompletionDate.visibility = View.GONE
                        } else {
                            binding.taskDetailCompletionDate.text = it.completionDate.toString()
                        }
                        binding.taskDetailResponsiblePerson.text = it.taskResponsiblePersonEmail ?: "Not assigned"
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
        commentsAdapter = CommentsAdapter()
        binding.taskDetailRvComments.apply {
            adapter = commentsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}