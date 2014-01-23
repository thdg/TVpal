package is.contracts.datacontracts;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by thorsteinn on 11/2/13.
 */
public interface IDrawerItem
{
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
