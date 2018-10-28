package android.test.com.todo.activities

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.test.com.todo.R
import android.test.com.todo.adapters.CommentsAdapter
import android.test.com.todo.fragments.TimePickerFragment
import android.test.com.todo.getDbInsatance
import android.test.com.todo.helper.*
import android.test.com.todo.showMessage
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TimePicker
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_task_details.*
import java.util.*

class TaskDetailsActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private var task: DocumentSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = "Todo Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mainLayout.visibility = View.GONE
        getTask()
        remindButton.setOnClickListener { setReminder() }
        markComplete.setOnClickListener { updateTask() }
    }

    private fun updateTask() {
        getDbInsatance().document(intent.getStringExtra(ID)).update(IS_COMPLETED, true)
                .addOnSuccessListener {
                    showMessage("Task marked completed")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .addOnFailureListener { showMessage("Failed to mark task as completed ") }
    }

    private fun getTask() {
        showProgressBar()
        getDbInsatance().document(intent.getStringExtra(ID)).get().addOnSuccessListener {
            if (it.exists()) {
                hideProgressBar()
                setTodoData(it)
            }
        }
    }

    private fun setTodoData(taskSnapshot: DocumentSnapshot?) {
        task = taskSnapshot
        taskDescription.text = taskSnapshot?.getString(TASK_NAME)
        val isCompleted = task?.getBoolean(IS_COMPLETED)!!
        statusDescription.text = if (isCompleted) "Your task is complete" else "Your task in pending"
        bottomCardView.visibility = if (isCompleted) View.GONE else View.VISIBLE
        commentsRecyclerView.setHasFixedSize(true)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.adapter = CommentsAdapter(task?.get(COMMENTS) as MutableList<String>)
        mainLayout.visibility = View.VISIBLE
    }

    private fun setReminder() {
        val timePicker = TimePickerFragment()
        timePicker.show(supportFragmentManager, "time picker")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        scheduleReminder(calendar)
    }

    private fun scheduleReminder(calendar: Calendar) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra(TITLE, "Did you complete your task?")
        intent.putExtra(MESSAGE, task?.getString(TASK_NAME))
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        if (calendar.before(Calendar.getInstance())) { // if we select the time which is already passed the notification will fire the next day at selected time
            calendar.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }
}
