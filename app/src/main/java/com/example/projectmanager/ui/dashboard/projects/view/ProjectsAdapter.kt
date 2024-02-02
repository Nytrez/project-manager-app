package com.example.projectmanager.ui.dashboard.projects.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.data.model.projects.ProjectResponseItem
import com.google.android.material.card.MaterialCardView

class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ProjectResponseItem>() {
        override fun areItemsTheSame(oldItem: ProjectResponseItem, newItem: ProjectResponseItem): Boolean {
            return oldItem.projectId == newItem.projectId
        }

        override fun areContentsTheSame(oldItem: ProjectResponseItem, newItem: ProjectResponseItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false)
        return ProjectsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val currProject = differ.currentList[position]
        holder.itemView.apply {
            findViewById<MaterialCardView>(R.id.rvProjectItemCardView).setOnClickListener {
                onItemClickListener?.let { it(currProject) }
            }

            holder.itemView.findViewById<TextView>(R.id.rvProjectItemName).text = currProject.projectName
            holder.itemView.findViewById<TextView>(R.id.rvProjectItemDescription).text = currProject.projectDescription

            setOnClickListener {
                onItemClickListener?.let { it(currProject) }
            }
        }
    }

    private var onItemClickListener: ((ProjectResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ProjectResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ProjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}