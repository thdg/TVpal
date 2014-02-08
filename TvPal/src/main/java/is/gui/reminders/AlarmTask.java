package is.gui.reminders;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Svavar on 26.11.2013.
 */
public class AlarmTask implements Runnable{

    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
    // Reminder for specific event
    private final String[] showInfo;

    public AlarmTask(Context context, Calendar date, String[] showInfo)
    {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.showInfo = showInfo;
    }

    @Override
    public void run()
    {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra("REMINDER", showInfo);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent("is.activites.scheduleActivites.INTENT_NOTIFY"),
                PendingIntent.FLAG_NO_CREATE) != null);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        if(!alarmUp) am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
    }
}
