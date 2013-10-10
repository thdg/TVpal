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

        setTitle(title);

        TextView eventDescription = (TextView) findViewById(R.id.title);
        eventDescription.setText(title);

        TextView eventCategory = (TextView) findViewById(R.id.eventDescription);
        eventCategory.setText(description);
    }

}
