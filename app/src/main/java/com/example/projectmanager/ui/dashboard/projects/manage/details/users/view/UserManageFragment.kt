package com.example.projectmanager.ui.dashboard.projects.manage.users

import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmanager.R
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponseItem
import com.example.projectmanager.data.util.Resource
import com.example.projectmanager.databinding.FragmentManageUsersBinding
import com.google.android.material.snackbar.Snackbar

class UserManageFragment : Fragment() {

    val LOG_TAG = "UserManageProjectsFragment"

    private var _binding: FragmentManageUsersBinding? = null
    lateinit var userManageViewModel: UserManageViewModel
    private lateinit var usersAdapter: UserManageAdapter
    private val args: UserManageFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageUsersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userManageViewModel = ViewModelProvider(this)[UserManageViewModel::class.java]

        setUpRecyclerView()

        usersAdapter.setOnItemClickListener { user ->
            showUserEditDialog(user)
        }

        binding.fabAddUser.setOnClickListener {
            showUserAddDialog()
        }

        userManageViewModel.getProjectUsers(args.projectId)
        observeResponses()
    }

    private fun showUserEditDialog(userDetails : UserDetailsProjectResponseItem) {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Update user")

        val inputLayout = LayoutInflater.from(context).inflate(R.layout.user_details_edit_popup, null)
        builder.setView(inputLayout)

        val userRoleInput = inputLayout.findViewById<EditText>(R.id.projectManageUserManagePopupRole)
        val userPermissionInput = inputLayout.findViewById<EditText>(R.id.projectManageUserManagePopupPermissions)

        builder.setPositiveButton("Update", null)
        builder.setNegativeButton("Cancel", null)
        builder.setNeutralButton("Remove from project", null)

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            setTextColor(Color.GREEN)
            setOnClickListener {
                val user = userRoleInput.text.toString()
                val userPermission = userPermissionInput.text.toString()

                Log.d(LOG_TAG, "showInputDialog: $user, $userPermission")

                userManageViewModel.updateUser(args.projectId, user, userPermission.toInt(), userDetails.userId)
                dialog.dismiss()
            }
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
            setTextColor(Color.RED)
            setOnClickListener {
                userManageViewModel.deleteUser(args.projectId, userDetails.userId)
                dialog.dismiss()
            }
        }
    }

    private fun showUserAddDialog() {
        val context = requireContext()
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add user")

        val inputLayout = LayoutInflater.from(context).inflate(R.layout.dialog_add_user, null)
        builder.setView(inputLayout)

        val userEmailInput = inputLayout.findViewById<EditText>(R.id.dialogAddUserEmail)
        val userRoleInput = inputLayout.findViewById<EditText>(R.id.dialogAddUserRole)
        val userPermissionInput = inputLayout.findViewById<EditText>(R.id.dialogAddUserPermissions)

        builder.setPositiveButton("Add", null)
        builder.setNegativeButton("Cancel", null)

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            setTextColor(Color.GREEN)
            setOnClickListener {
                val userEmail = userEmailInput.text.toString()
                val userRole = userRoleInput.text.toString()
                val userPermission = userPermissionInput.text.toString().toInt()

                Log.d(LOG_TAG, "adding user: $userEmail")

                userManageViewModel.addUser(args.projectId, userEmail, userRole, userPermission)
                dialog.dismiss()
            }
        }

    }

    private fun observeResponses() {
        userManageViewModel.projectUsers.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        usersAdapter.differ.submitList(it)
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

        userManageViewModel.userChangeResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        //TODO RETURN

                        Toast.makeText(activity, "its fine", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
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


        userManageViewModel.userDeleteResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        //TODO RETURN

                        view?.let { it1 -> Snackbar.make(it1, "User deleted", Snackbar.LENGTH_SHORT).show() }
                        findNavController().popBackStack()
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

        userManageViewModel.userAddResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    //hideProgressBar()
                    response.data?.let {
                        //TODO RETURN
                        view?.let { it1 -> Snackbar.make(it1, "User added", Snackbar.LENGTH_SHORT).show() }
                        findNavController().popBackStack()
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

    private fun setUpRecyclerView() {
        usersAdapter = UserManageAdapter()
        binding.projectManageRvUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}