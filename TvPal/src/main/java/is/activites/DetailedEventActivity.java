package is.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import is.tvpal.R;

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

        TextView eventStart = (TextView) findViewById(R.id.event_starting);
        eventStart.setText(start);

        TextView eventDuration = (TextView) findViewById(R.id.event_duration);
        eventDuration.setText(duration);

    }

}
