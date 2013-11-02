package is.activites.showActivities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        _db = new DbShowHandler(this);

        _listView = (ListView) findViewById(R.id.overviewShows);

        _adapter = new ActivityAdapter(this, _db.GetCursorUpcoming(), 0);
        _listView.setAdapter(_adapter);

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
