package is.activites.baseActivities;

import android.app.ListActivity;
import android.view.MenuItem;

/**
 * Created by Arnar on 22.11.2013.
 */
public class BaseListActivity extends ListActivity
{
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
