package is.activites.baseActivities;

import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * Created by Arnar on 21.11.2013.
 */
public class BaseFragmentActivity extends FragmentActivity
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
