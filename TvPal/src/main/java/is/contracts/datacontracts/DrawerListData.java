package is.contracts.datacontracts;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import is.handlers.adapters.DrawerListAdapter;
import is.tvpal.R;

/**
 * Created by Þorsteinn on 14.10.2013.
 */
public class DrawerListData implements IDrawerItem
{
    private final String name;
    private final int iconId;

    public DrawerListData(String name, int iconId)
    {
        this.name = name;
        this.iconId = iconId;
    }

    @Override
    public int getViewType() {
        return DrawerListAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.navigation_drawer, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        ImageView icon = (ImageView) view.findViewById(R.id.drawer_list_icon);
        TextView title = (TextView) view.findViewById(R.id.drawer_list_title);
        icon.setImageResource(iconId);
        title.setText(name);

        return view;
    }
    @Override
    public String toString()
    {
        return String.format("%s", this.name);
    }
}
