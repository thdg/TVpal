package is.gui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Arnar on 5.12.2013.
 */
public class BaseFragment extends Fragment
{
    protected IContext activity;

    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach(activity);
        try
        {
            this.activity = (IContext) activity;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
