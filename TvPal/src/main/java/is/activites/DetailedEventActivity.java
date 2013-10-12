package is.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import is.tvpal.R;

/**
 * Created by Arnar
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

    private void Initialize()
    {
        Intent intent = getIntent();

        String title       = intent.getStringExtra(DisplayRuvActivity.EXTRA_TITLE);
        String description = intent.getStringExtra(DisplayRuvActivity.EXTRA_DESCRIPTION);
        String start       = intent.getStringExtra(DisplayRuvActivity.EXTRA_START);
        String duration    = intent.getStringExtra(DisplayRuvActivity.EXTRA_DURATION);

        setTitle(title);

        TextView eventDescription = (TextView) findViewById(R.id.title);
        eventDescription.setText(title);

        TextView eventCategory = (TextView) findViewById(R.id.event_description);
        eventCategory.setText(description);

        if (!start.equals("")) {
            TextView eventStart = (TextView) findViewById(R.id.event_starting);
            eventStart.setText(String.format("%s: %s", getResources().getString(R.string.starting_time), start));
        }

        if (!duration.equals("")) {
            TextView eventDuration = (TextView) findViewById(R.id.event_duration);
            eventDuration.setText(String.format("%s: %s", getResources().getString(R.string.duration), duration));
        }

    }

}
