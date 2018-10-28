package android.test.com.todo.activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.test.com.todo.*
import android.test.com.todo.adapters.CommentsAdapter
import android.test.com.todo.fragments.TimePickerFragment
import android.test.com.todo.helper.IS_COMPLETED
import android.test.com.todo.helper.ReminderReceiver
import android.view.MenuItem
import android.view.View
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_add_tasks.*
import java.util.*

class AddTasksActivity : AppCompatActivity() {

    val commentList = mutableListOf<String>()
    val commentsAdapter = CommentsAdapter(commentList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = "Add todo's"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpCommentRecyclerView()
        addCommentButton.setOnClickListener { addCommentToList() }
        saveTaskButton.setOnClickListener { addTask() }
    }

    private fun setUpCommentRecyclerView() {
        commentsRecyclerView.setHasFixedSize(true)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.adapter = commentsAdapter
    }

    private fun addCommentToList() {
        if (taskCommentEditText.text != null && taskCommentEditText.text.isNotEmpty()) {
            commentList.add(taskCommentEditText.text.toString())
            commentsAdapter.notifyDataSetChanged()
            commentsRecyclerView.visibility = View.VISIBLE
            taskCommentEditText.setText("")
        } else showMessage("Please enter your comment")
    }

    private fun addTask() {
        if (taskNameEditText.text != null && taskNameEditText.text.toString().trim().isNotEmpty()) {
            val taskDoc = HashMap<String, Any>()
            taskDoc.put("taskName", taskNameEditText.text.toString())
            taskDoc.put("isCompleted", false)
            taskDoc.put("comments", commentList)

            getDbInsatance().add(taskDoc)
                    .addOnSuccessListener {
                        showMessage("Task saved")
                        //remindButton.visibility = View.VISIBLE
                    }
                    .addOnFailureListener { showMessage("Failed to save Task") }

            setResult(Activity.RESULT_OK)
            finish()
        } else showMessage("Please enter task name")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }
}
