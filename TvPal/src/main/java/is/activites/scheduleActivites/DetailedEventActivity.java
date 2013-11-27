package is.activites.scheduleActivites;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.widget.Toast;
import is.activites.reminderActivities.ScheduleClient;
import is.activites.baseActivities.BaseActivity;
import is.contracts.datacontracts.EventData;
import is.tvpal.R;

/**
 * Created by Þorsteinn
 *
 * This class handles the activity to show detailed event information
 * when an event has been selected.
 */
public class DetailedEventActivity extends BaseActivity {

    private EventData event;
    private ScheduleClient scheduleClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();

        event = (EventData) intent.getSerializableExtra(ScheduleFragment.EXTRA_EVENT);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(event.getTitle());

        TextView eventDescription = (TextView) findViewById(R.id.title);
        eventDescription.setText(event.getTitle());

        TextView eventCategory = (TextView) findViewById(R.id.event_description);
        eventCategory.setText(event.getDescription());

        if (!event.getStartTime().equals("")) {
            TextView eventStart = (TextView) findViewById(R.id.event_starting);
            eventStart.setText(String.format("%s: %s", getResources().getString(R.string.starting_time), event.getStartTime()));
        }

        if (!event.getDuration().equals("")) {
            TextView eventDuration = (TextView) findViewById(R.id.event_duration);
            eventDuration.setText(String.format("%s: %s", getResources().getString(R.string.duration), event.getDuration()));
        }
        View view = findViewById(R.id.getReminder);
    }

    /**
     * This is the onClick called from the XML to set a new notification
     */
    public void ReminderClick(View view){

        // Create a new calendar set to the date and time of the show
        if (!event.getStartTime().equals("") && !event.getEventDate().equals("")) {
            int year = Integer.parseInt(event.getEventDate().substring(0, 4));
            int month = Integer.parseInt(event.getEventDate().substring(5, 7));
            int day = Integer.parseInt(event.getEventDate().substring(8));
            int hour = Integer.parseInt(event.getStartTime().substring(0,2));
            int minute = Integer.parseInt(event.getStartTime().substring(3));

            String[] showInfo = { event.getTitle(), event.getStartTime()};

                Calendar alarmDate = Calendar.getInstance();
                alarmDate.set(Calendar.YEAR, year);
                // first month is zero so do -1
                alarmDate.set(Calendar.MONTH, month-1);
                alarmDate.set(Calendar.DATE, day);
                alarmDate.set(Calendar.HOUR_OF_DAY, hour);
                alarmDate.set(Calendar.MINUTE, minute);
                //alarmDate.add(Calendar.MINUTE, -15);
                alarmDate.set(Calendar.SECOND, 0);


                Calendar now = Calendar.getInstance();
                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                if(alarmDate.after(now)) {

                    scheduleClient.setAlarmForNotification(alarmDate, showInfo);
                    // Notify the user what they just did
                    Toast.makeText(this, "Áminning sett þann: "+ day +"/"+ (month) +"/"+ year + " klukkan: " + hour + ":" +
                            minute , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Dagskrárliður hefur nú þegar verið sýndur" , Toast.LENGTH_SHORT).show();
                }

        }

    }

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}
