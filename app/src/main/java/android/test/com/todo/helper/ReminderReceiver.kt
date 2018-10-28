package android.test.com.todo.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val notificationHelper = NotificationHelper(context)
            val notificationBuilder = notificationHelper.getReminderChannelNotification(intent.getStringExtra(TITLE), intent.getStringExtra(MESSAGE))
            notificationHelper.getNoificationManager().notify(1, notificationBuilder.build())
        }
    }
}