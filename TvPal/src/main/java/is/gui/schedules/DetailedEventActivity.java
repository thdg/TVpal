package is.gui.schedules;

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
import is.gui.reminders.ScheduleClient;
import is.gui.base.BaseActivity;
import is.contracts.datacontracts.EventData;
import is.tvpal.R;

public class DetailedEventActivity extends BaseActivity
{
    private EventData mEvent;
    private ScheduleClient mScheduleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_event);

        mScheduleClient = new ScheduleClient(this);
        mScheduleClient.doBindService();

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();

        mEvent = (EventData) intent.getSerializableExtra(ScheduleFragment.EXTRA_EVENT);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(mEvent.getTitle());

        TextView eventDescription = (TextView) findViewById(R.id.title);
        eventDescription.setText(mEvent.getTitle());

        TextView eventCategory = (TextView) findViewById(R.id.event_description);
        eventCategory.setText(mEvent.getDescription());

        if (!mEvent.getStartTime().equals(""))
        {
            TextView eventStart = (TextView) findViewById(R.id.event_starting);
            eventStart.setText(String.format("%s: %s", getResources().getString(R.string.starting_time), mEvent.getStartTime()));
        }

        if (!mEvent.getDuration().equals(""))
        {
            TextView eventDuration = (TextView) findViewById(R.id.event_duration);
            eventDuration.setText(String.format("%s: %s", getResources().getString(R.string.duration), mEvent.getDuration()));
        }
    }

    public void ReminderClick(View view)
    {
        if (!mEvent.getStartTime().isEmpty() && !mEvent.getEventDate().isEmpty())
        {
            String year = mEvent.getEventDate().substring(2,4);
            String month = mEvent.getEventDate().substring(5,7);
            String day = mEvent.getEventDate().substring(8);
            int hour = Integer.parseInt(mEvent.getStartTime().substring(0,2));
            int minute = Integer.parseInt(mEvent.getStartTime().substring(3));

            Date notificationDate = FormatNotificationDate(String.format("%s/%s/%s", month, day, year));

            if (notificationDate != null)
            {
                String[] showInfo = { mEvent.getTitle(), mEvent.getStartTime()};

                Calendar alarmDate = Calendar.getInstance();
                alarmDate.setTime(notificationDate);
                alarmDate.set(Calendar.HOUR_OF_DAY, hour);
                alarmDate.set(Calendar.MINUTE, minute-15);

                Calendar dateNow = Calendar.getInstance();
                dateNow.setTime(new Date());

                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                if(alarmDate.before(dateNow))
                {
                    Toast.makeText(this, "Dagskrárliður hefur nú þegar verið sýndur", Toast.LENGTH_SHORT).show();
                }
                else if (alarmDate.after(dateNow))
                {
                    mScheduleClient.setAlarmForNotification(alarmDate, showInfo);
                    // Notify the user what they just did
                    Toast.makeText(this, "Áminning sett: " + formatDate(alarmDate.getTime()), Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(this, "Villa kom upp við skráningu", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onStop()
    {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(mScheduleClient != null)
            mScheduleClient.doUnbindService();
        super.onStop();
    }

    private String formatDate(Date date)
    {
        return new SimpleDateFormat("yyyy-MM-dd kk:mm").format(date);
    }

    private Date FormatNotificationDate(String date)
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
            return dateFormat.parse(date);
        }
        catch(ParseException e)
        {
            Toast.makeText(this, "Ekki gekk að vista áminningu", Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}
