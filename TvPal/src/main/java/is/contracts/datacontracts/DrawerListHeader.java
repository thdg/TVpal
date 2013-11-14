package is.contracts.datacontracts;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import is.handlers.adapters.DrawerListAdapter;
import is.tvpal.R;

/**
 * Created by thorsteinn on 11/2/13.
 */
public class DrawerListHeader implements IDrawerItem
{
    private final String name;

    public DrawerListHeader(String name)
    {
        this.name = name;
    }

    public String getName() { return this.name; }

    @Override
    public int getViewType() {
        return DrawerListAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.navigation_drawer_header, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(name);

        return view;
    }
    @Override
    public String toString()
    {
        return String.format("%s", this.name);
    }
}
