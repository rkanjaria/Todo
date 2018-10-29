package android.test.com.todo.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.test.com.todo.R
import android.test.com.todo.activities.TaskDetailsActivity

class NotificationHelper(context: Context) : ContextWrapper(context) {
    private val reminderChannelId = "reminder_channel"
    private val reminderChannel = "remainder_channel"
    private var notificationManager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val notificationReminderChannel = NotificationChannel(reminderChannelId, reminderChannel, NotificationManager.IMPORTANCE_HIGH)
        notificationReminderChannel.enableLights(true)
        notificationReminderChannel.enableVibration(true)
        notificationReminderChannel.lightColor = R.color.colorPrimary
        notificationReminderChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getNoificationManager().createNotificationChannel(notificationReminderChannel)
    }

    fun getNoificationManager(): NotificationManager {
        if (notificationManager == null) {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }
        return notificationManager!!
    }

    fun getReminderChannelNotification(intent: Intent): NotificationCompat.Builder {
        val taskDetailsIntent = Intent(this, TaskDetailsActivity::class.java)
        intent.putExtra(FROM_NOTIFICATION, true)
        taskDetailsIntent.putExtras(intent)
        taskDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 1, taskDetailsIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Builder(applicationContext, reminderChannelId)
                .setContentTitle(intent.getStringExtra(TITLE))
                .setContentText(intent.getStringExtra(MESSAGE))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_playlist_add_check)
    }
}