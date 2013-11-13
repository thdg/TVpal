package is.activites.showActivities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import is.handlers.adapters.ActivityAdapter;
import is.handlers.database.DbShowHandler;
import is.tvpal.R;

/**
 * Displays upcoming shows
 * @author Arnar
 */

public class UpcomingShowsActivity extends Activity implements AdapterView.OnItemClickListener
{
    public static final String EXTRA_SERIESID = "is.activites.showActivities.SERIESID";
    public static final String EXTRA_SEASONNR = "is.activites.showActivities.SEASONNR";
    public static final String EXTRA_SELECTED = "is.activites.showActivities.SELECTED";

    private ListView _listView;
    private DbShowHandler _db;
    private ActivityAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_recent);

        Initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void Initialize()
    {
        _db = new DbShowHandler(this);

        _listView = (ListView) findViewById(R.id.overviewShows);
        _listView.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
    {
        Cursor episode = (Cursor) _adapter.getItem(position);

        Intent intent = new Intent(this, EpisodeActivity.class);
        intent.putExtra(EXTRA_SERIESID, episode.getString(3));
        intent.putExtra(EXTRA_SEASONNR, episode.getString(4));
        intent.putExtra(EXTRA_SELECTED, Integer.parseInt(episode.getString(5))-1);  //Fuck ugly hack to set correct episode

        startActivity(intent);
    }
}
