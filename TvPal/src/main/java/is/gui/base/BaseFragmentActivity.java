package is.gui.base;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

/**
 * Created by Arnar on 21.11.2013.
 */
public class BaseFragmentActivity extends FragmentActivity implements IContext
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

    @Override
    public Context getContext()
    {
        return this;
    }
}
