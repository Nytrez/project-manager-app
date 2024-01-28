package com.example.projectmanager.ui.dashboard.projects.manage.details.users.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.data.model.projects.manage.user.UserDetailsProjectResponseItem

class UserManageAdapter : RecyclerView.Adapter<UserManageAdapter.UserViewHolder>(){

    private val differCallback = object : DiffUtil.ItemCallback<UserDetailsProjectResponseItem>(){
        override fun areItemsTheSame(oldItem: UserDetailsProjectResponseItem, newItem: UserDetailsProjectResponseItem): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: UserDetailsProjectResponseItem, newItem: UserDetailsProjectResponseItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_details_item, parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = differ.currentList[position]
        //log.d("TasksAdapter", "onBindViewHolder: $task")
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.projectManageUserDetailsEmail).text = user.email
            holder.itemView.findViewById<TextView>(R.id.projectManageUserDetailsName).text = user.name
            holder.itemView.findViewById<TextView>(R.id.projectManageUserDetailsSurname).text = user.surname
            holder.itemView.findViewById<TextView>(R.id.projectManageUserDetailsPermissions).text = user.permissions.toString()
            holder.itemView.findViewById<TextView>(R.id.projectManageUserDetailsRole).text = user.role

            //holder.itemView.findViewById<TextView>(R.id.rvProjectItemEstimatedEndDate).text = task.projectEstimatedEndDate.toString()

//            Glide.with(this)
//                .load(currProject.thumbnail)
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(holder.itemView.findViewById(R.id.ivGameImage))
            setOnClickListener {
                onItemClickListener?.let { it(user) }
            }
        }
    }

    private var onItemClickListener: ((UserDetailsProjectResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (UserDetailsProjectResponseItem) -> Unit) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
}