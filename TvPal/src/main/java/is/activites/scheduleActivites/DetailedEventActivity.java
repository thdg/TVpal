package is.activites.scheduleActivites;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.TextView;

import is.datacontracts.EventData;
import is.tvpal.R;

/**
 * Created by Þorsteinn
 *
 * This class handles the activity to show detailed event information
 * when an event has been selected.
 */
public class DetailedEventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        Intent intent = getIntent();

        EventData event = (EventData) intent.getSerializableExtra(ScheduleFragment.EXTRA_EVENT);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
