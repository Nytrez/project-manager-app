package com.example.projectmanager.data.model.tasks.comments

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TaskCommentsResponseItem(@field:SerializedName("commentId")
                                           val commentId: Int,
                                    @field:SerializedName("taskId")
                                           val taskCommentTaskId: String,
                                    @field:SerializedName("userId")
                                           val taskCommentUserId: Int,
                                    @field:SerializedName("commentDate")
                                           val taskCommentDate: Date,
                                    @field:SerializedName("comment")
                                           val taskCommentText: String,
)