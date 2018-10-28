package android.test.com.todo.adapters

import android.support.v7.widget.RecyclerView
import android.test.com.todo.R
import android.test.com.todo.helper.TASK_NAME
import android.test.com.todo.inflate
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.task_recycler_layout.view.*

class TasksAdapter(val taskList: List<DocumentSnapshot>, val mListener: TaskAdapterListener) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TaskViewHolder {
        return TaskViewHolder(parent.inflate(R.layout.task_recycler_layout))
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTasks(taskList[position])
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView
        fun bindTasks(documentSnapshot: DocumentSnapshot) {
            view.taskName.text = documentSnapshot.getString(TASK_NAME)
            view.setOnClickListener {
                mListener.startTaskDetailsActivity(documentSnapshot.id)
            }
        }
    }

    interface TaskAdapterListener {
        fun startTaskDetailsActivity(documentId: String)
    }
}