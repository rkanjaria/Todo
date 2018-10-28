package android.test.com.todo.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.test.com.todo.R
import android.test.com.todo.adapters.TasksAdapter
import android.test.com.todo.getDbInsatance
import android.test.com.todo.helper.ACTIVITY_RESULT_CODE
import android.test.com.todo.helper.ID
import android.test.com.todo.helper.IS_COMPLETED
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), TasksAdapter.TaskAdapterListener {

    private var isCompleted = false

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar as Toolbar)
        isCompleted = intent.getBooleanExtra(IS_COMPLETED, false)
        supportActionBar?.title = if (isCompleted) "Completed todo's" else "Your todo's"
        if (isCompleted) supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getAllCompletedTasks()

        addTaskFab.setOnClickListener {
            val addTaskIntent = Intent(this, AddTasksActivity::class.java)
            startActivityForResult(addTaskIntent, ACTIVITY_RESULT_CODE)
        }

        addTaskFab.visibility = if (isCompleted) View.GONE else View.VISIBLE
    }

    fun getAllCompletedTasks() {
        showProgressBar()
        getDbInsatance().whereEqualTo(IS_COMPLETED, isCompleted).get().addOnCompleteListener {
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
            taskRecyclerview.adapter = TasksAdapter(taskList, this)
            taskRecyclerview.visibility = View.VISIBLE
        }
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun startTaskDetailsActivity(documentId: String) {
        val detailsIntent = Intent(this, TaskDetailsActivity::class.java)
        detailsIntent.putExtra(ID, documentId)
        startActivityForResult(detailsIntent, ACTIVITY_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            taskRecyclerview.visibility = View.GONE
            getAllCompletedTasks()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        if (isCompleted) menu?.findItem(R.id.action_completed_task)?.setVisible(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_completed_task -> {
                val completedTaskIntent = Intent(this, HomeActivity::class.java)
                completedTaskIntent.putExtra(IS_COMPLETED, true)
                startActivity(completedTaskIntent)
            }
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }
}
