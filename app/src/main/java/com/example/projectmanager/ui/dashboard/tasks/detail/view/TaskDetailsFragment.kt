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
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentTaskDetailsBinding
import com.example.projectmanager.ui.dashboard.tasks.detail.viewmodel.TaskDetailsViewModel

class TaskDetailsFragment : Fragment() {

    val LOG_TAG = "TaskDetailsFragment"

    private var _binding: FragmentTaskDetailsBinding? = null
    private lateinit var commentsAdapter: CommentsAdapter
    lateinit var taskDetailsViewModel: TaskDetailsViewModel

    private val args: TaskDetailsFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
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
            val commentText = binding.taskDetailCommentInput.text.trim().toString()
            if (commentText.isNotEmpty()) {
                taskDetailsViewModel.postComment(args.taskId, commentText)
                observeCommentPostResponse()
            } else {
                Toast.makeText(requireContext(), "Please fill out the comment.", Toast.LENGTH_SHORT).show()
            }
        }

        //commentsAdapter.setOnItemHoldListener
        // TODO usuwanie komentarzy

        taskDetailsViewModel.getAllTaskDetails(args.taskId)
        taskDetailsViewModel.getAllComments(args.taskId)

        observeProjects()
    }

    private fun observeCommentPostResponse() {
        taskDetailsViewModel.commentPostResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        Toast.makeText(activity, "Comment posted successfully.", Toast.LENGTH_SHORT).show()
                        taskDetailsViewModel.getAllComments(args.taskId)
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
    }


    private fun observeProjects() {
        taskDetailsViewModel.commentsDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        commentsAdapter.differ.submitList(it)
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

        taskDetailsViewModel.taskDetailsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        binding.taskDetailsHeader.text = it.taskHeader
                        binding.taskDetailsDescriptionShort.text = it.taskDescriptionShort
                        binding.taskDetailsDescription.text = it.taskDescription
                        binding.taskDetailPriority.text = it.taskPriority.toString()
                        binding.taskDetailCompletionDate.text = it.completionDate.toString()
                        binding.taskDetailResponsiblePerson.text = it.taskResponsiblePersonId.toString()
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

    }

    private fun setUpRecyclerView(){
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