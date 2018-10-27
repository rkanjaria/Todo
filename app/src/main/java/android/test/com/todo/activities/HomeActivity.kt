package android.test.com.todo.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.test.com.todo.R
import android.test.com.todo.adapters.TasksAdapter
import android.test.com.todo.getDbInsatance
import android.test.com.todo.utils.IS_COMPLETED
import android.view.View
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = "Tasks todo"
        getAllCompletedTasks()

        addTaskFab.setOnClickListener {
            val addTaskIntent = Intent(this, AddTasksActivity::class.java)
            startActivity(addTaskIntent)
        }
    }

    fun getAllCompletedTasks() {
        showProgressBar()
        getDbInsatance().whereEqualTo(IS_COMPLETED, true).get().addOnCompleteListener {
            if (it.isComplete) {
                hideProgressBar()
                setTaskRecyclerView(it.getResult()?.documents)
            }
        }
    }

    private fun setTaskRecyclerView(taskList: List<DocumentSnapshot>?) {
        if (taskList != null) {
            taskRecyclerview.setHasFixedSize(true)
            taskRecyclerview.layoutManager = LinearLayoutManager(this)
            taskRecyclerview.adapter = TasksAdapter(taskList)
        }
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}
