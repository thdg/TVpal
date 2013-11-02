package is.datacontracts;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by thorsteinn on 11/2/13.
 */
public interface Item {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
