package android.test.com.todo.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.test.com.todo.R

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

    fun getReminderChannelNotification(title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, reminderChannelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
    }
}