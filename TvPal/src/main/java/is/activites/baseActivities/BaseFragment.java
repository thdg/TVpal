package is.activites.baseActivities;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Arnar on 5.12.2013.
 */
public class BaseFragment extends Fragment
{
    protected IContext activityCxt;

    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach(activity);
        try
        {
            activityCxt = (IContext) activity;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
