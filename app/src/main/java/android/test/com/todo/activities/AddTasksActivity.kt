package android.test.com.todo.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.test.com.todo.R

class AddTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)
    }
}
