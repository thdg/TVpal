package is.activites.showActivities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;
import is.handlers.adapters.ActivityAdapter;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

public class UpcomingShowsActivity extends Activity
{
    private ListView _listView;
    private DbShowHandler _db;
    private ActivityAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_activity);

        Initialize();
    }

    private void Initialize()
    {
        _db = new DbShowHandler(this);
        TextView tv = (TextView) findViewById(R.id.overviewTitle);
        tv.setText("Upcoming shows");

        _listView = (ListView) findViewById(R.id.overviewShows);

        _adapter = new ActivityAdapter(this, _db.GetCursorUpcoming(), 0);
        _listView.setAdapter(_adapter);
    }
}
