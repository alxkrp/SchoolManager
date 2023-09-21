package ru.ak.schoolmanager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.text.Format
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class AlarmBroadcast : BroadcastReceiver() {
    //При получении сигнала о срабатывании уведомления
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context, intent: Intent) {
        //Получаем соответствующий сервис
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        // Создаем уведомление
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "schoolmanager.wackelock")

        //Блокируем поток
        wl.acquire()

        //Здесь можно делать обработку.
        val extras = intent.extras
        val msgStr = StringBuilder()
        if (extras != null && extras.getBoolean(ONE_TIME, false)) {
            //проверяем параметр ONE_TIME, если это одиночный будильник,
            //выводим соответствующее сообщение.
            msgStr.append("Одноразовый будильник: ")
        }

        //Добавляем дату и время срабатывания
        val formatter: Format = SimpleDateFormat("hh:mm:ss a")
        msgStr.append(formatter.format(Date()))

        //Создаем события, устанавливаем флаги (сброс уведомления, уведомление стирается при нажатии)
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val int1 = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Создаем уведомление, которое появится при срабатывании даты
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context)
            .setContentTitle("Напоминание")
            .setContentText("Текст вашего напоминания")
//            .setSmallIcon(R.drawable.ic_launcher)
            .setContentIntent(int1)
            .setAutoCancel(true) // Важный параметр указывает, что нужно очистить уведомление при нажатии
        mBuilder.build().flags = mBuilder.build().flags or Notification.FLAG_AUTO_CANCEL
        nm.notify(1, mBuilder.build())

        //Устанавливаем звук уведомления
        val nt = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, nt)
        r.play()

        //Разблокируем поток.
        wl.release()
    }

    fun setAlarm(context: Context, inputString: String, id: Int) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcast::class.java)
        intent.putExtra(ONE_TIME, false) //Параметр интента
        val pi = PendingIntent.getBroadcast(context, id, intent, FLAG_IMMUTABLE)
        //Интервал срабатывания 5 секунд
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val anothersdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.setTimeZone(TimeZone.getDefault())

        //Здесь делаем так, чтоб уведомление не появлялось, если время срабатывания уже прошло, а именно переносим на следующий день
        var date = Date()
        val data: String = anothersdf.format(date) + " " + inputString
        val current: Date = date
        try {
            date = sdf.parse(data)
        } catch (e: ParseException) {
        }
        val c: Calendar = Calendar.getInstance()
        c.setTime(date)
        if (date.compareTo(current) < 0) {
            c.add(Calendar.DATE, 1)
        }
        date = c.getTime()
        am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTime(), AlarmManager.INTERVAL_DAY, pi)
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, AlarmBroadcast::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender) //Отменяем будильник связанный с интентом данного класс
    }

    fun setOneTimeTimer(context: Context, inputString: String, id: Int) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcast::class.java)
        intent.putExtra(ONE_TIME, true)
        val pi = PendingIntent.getBroadcast(context, id, intent, FLAG_IMMUTABLE)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val anothersdf = SimpleDateFormat("yyyy-MM-dd")
        sdf.setTimeZone(TimeZone.getDefault())
        var date = Date()
        val data: String = anothersdf.format(date) + " " + inputString
        try {
            date = sdf.parse(data)
        } catch (e: ParseException) {
        }
        am[AlarmManager.RTC_WAKEUP, date.getTime()] = pi
    }

    companion object {
        const val ONE_TIME = "onetime"
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun alert(context: Context) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

//        context.getSystemService(Context.VIBRATOR_SERVICE).vibrate(4000)
        vibratorManager.defaultVibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show()
        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        // setting default ringtone

        // setting default ringtone
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)

        // play ringtone

        // play ringtone
        ringtone.play()
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context: Context, time: LocalDateTime) {
        val alarmIntent = Intent(context, AlarmBroadcast::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
//        val intent = Intent(context, AlarmBroadcast::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                //time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                SystemClock.elapsedRealtime() + 60 * 1000,
                alarmIntent
            );
        }

    }
}