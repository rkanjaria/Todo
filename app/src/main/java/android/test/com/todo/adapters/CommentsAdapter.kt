package android.test.com.todo.adapters

import android.support.v7.widget.RecyclerView
import android.test.com.todo.R
import android.test.com.todo.inflate
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.comment_recycler_layout.view.*

class CommentsAdapter(val commentList: MutableList<String>) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommentsViewHolder {
        return CommentsViewHolder(parent.inflate(R.layout.comment_recycler_layout))
    }

    override fun getItemCount() = commentList.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bindTasks(commentList[position])
    }

    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        fun bindTasks(comment: String) {
            view.comment.text = comment
        }
    }
}
