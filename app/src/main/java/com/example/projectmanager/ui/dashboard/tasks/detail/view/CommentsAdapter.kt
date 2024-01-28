package com.example.projectmanager.ui.dashboard.tasks.detail.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmanager.R
import com.example.projectmanager.data.model.tasks.comments.TaskCommentsResponseItem

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>(){

    val LOG_TAG = "CommentsAdapter"

    private val differCallback = object : DiffUtil.ItemCallback<TaskCommentsResponseItem>(){
        override fun areItemsTheSame(oldItem: TaskCommentsResponseItem, newItem: TaskCommentsResponseItem): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: TaskCommentsResponseItem, newItem: TaskCommentsResponseItem): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent,false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val currComment = differ.currentList[position]
        holder.itemView.apply {
            Log.d(LOG_TAG, "onBindViewHolder: $currComment")
            holder.itemView.findViewById<TextView>(R.id.commentItemUserName).text = currComment.taskCommentUserId.toString()
            holder.itemView.findViewById<TextView>(R.id.commentItemDate).text = currComment.taskCommentDate.toString()
            holder.itemView.findViewById<TextView>(R.id.commentItemComment).text = currComment.taskCommentText

//            Glide.with(this)
//                .load(currProject.thumbnail)
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(holder.itemView.findViewById(R.id.ivGameImage))
            setOnClickListener {
                onItemClickListener?.let { it(currComment) }
            }
        }
    }

    private var onItemClickListener: ((TaskCommentsResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (TaskCommentsResponseItem) -> Unit) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class CommentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}
}
