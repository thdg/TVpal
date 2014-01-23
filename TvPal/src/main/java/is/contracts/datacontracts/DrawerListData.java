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
    private final boolean seperatorInvisible;

    public DrawerListData(String name, int iconId, boolean seperatorInvisible)
    {
        this.name = name;
        this.iconId = iconId;
        this.seperatorInvisible =  seperatorInvisible;
    }

    @Override
    public int getViewType()
    {
        return DrawerListAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView)
    {
        View view;
        if (convertView == null)
        {
            view = inflater.inflate(R.layout.navigation_drawer, null);
        }
        else
        {
            view = convertView;
        }

        ((ImageView) view.findViewById(R.id.drawer_list_icon)).setImageResource(iconId);
        ((TextView) view.findViewById(R.id.drawer_list_title)).setText(name);

        if (seperatorInvisible)
            view.findViewById(R.id.lineSeperator).setVisibility(View.INVISIBLE);
        else
            view.findViewById(R.id.lineSeperator).setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public String toString()
    {
        return String.format("%s", this.name);
    }
}
