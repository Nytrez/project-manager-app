package com.example.projectmanager.ui.dashboard.tasks.detail.edit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.ui.dashboard.projects.manage.users.UserManageAdapter

class UserSelectDialogFragment : DialogFragment() {

    private lateinit var usersAdapter: UserManageAdapter
    lateinit var taskEditSelectUserViewModel: TaskEditSelectUserViewModel

    companion object {
        fun newInstance(projectId: Int, taskId : Int): UserSelectDialogFragment {
            val args = Bundle()
            args.putInt("projectId", projectId)
            args.putInt("taskId", taskId)
            val fragment = UserSelectDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_task_select_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        // Replace this with your actual data
        taskEditSelectUserViewModel = ViewModelProvider(this)[TaskEditSelectUserViewModel::class.java]
        setUpRecyclerView()
        Log.d("Petara", "ProjectId: ${arguments?.getInt("projectId")}")
        arguments?.getInt("projectId")?.let { taskEditSelectUserViewModel.getProjectUsers(it) }
        observeResponses()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun observeResponses() {
        taskEditSelectUserViewModel.projectUsers.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        usersAdapter.differ.submitList(it)
                    }
                }

                is Resource.Error -> {
                    Log.d("Dialog", "Error: ${response.message}")
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

    private fun setUpRecyclerView() {
        usersAdapter = UserManageAdapter()
        usersAdapter.setOnItemClickListener { user ->
            // Pass the selected user back to the parent fragment
            (parentFragment as? TaskEditFragment)?.onUserSelected(user)
            dismiss()
        }

        val recyclerView: RecyclerView? = view?.findViewById(R.id.taskDetailsEditRvUserSelect)
        recyclerView?.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}