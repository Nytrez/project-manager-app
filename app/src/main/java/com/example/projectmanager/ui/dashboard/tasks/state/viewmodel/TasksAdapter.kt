package com.example.projectmanager.ui.dashboard.tasks.state.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.data.model.tasks.TasksResponseItem
import com.google.android.material.card.MaterialCardView

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TasksViewHolder>(){

    private val differCallback = object : DiffUtil.ItemCallback<TasksResponseItem>(){
        override fun areItemsTheSame(oldItem: TasksResponseItem, newItem: TasksResponseItem): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(oldItem: TasksResponseItem, newItem: TasksResponseItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent,false)
        return TasksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task = differ.currentList[position]
        //log.d("TasksAdapter", "onBindViewHolder: $task")
        holder.itemView.apply {

            findViewById<MaterialCardView>(R.id.rvTaskItemCardView).setOnClickListener {
                onItemClickListener?.let { it(task) }
            }

            holder.itemView.findViewById<TextView>(R.id.taskDetailHeader).text = task.taskHeader
            holder.itemView.findViewById<TextView>(R.id.taskDetailDescriptionShort).text = task.taskDescriptionShort
            holder.itemView.findViewById<TextView>(R.id.taskDetailPriority).text = task.taskPriority.toString()
            holder.itemView.findViewById<TextView>(R.id.taskDetailCompletionDate).text = task.completionDate.toString()

            //holder.itemView.findViewById<TextView>(R.id.rvProjectItemEstimatedEndDate).text = task.projectEstimatedEndDate.toString()

//            Glide.with(this)
//                .load(currProject.thumbnail)
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(holder.itemView.findViewById(R.id.ivGameImage))
            setOnClickListener {
                onItemClickListener?.let { it(task) }
            }
        }
    }

    private var onItemClickListener: ((TasksResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (TasksResponseItem) -> Unit) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class TasksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
}