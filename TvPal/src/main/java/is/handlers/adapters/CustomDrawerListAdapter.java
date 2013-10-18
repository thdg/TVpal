package is.handlers.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.datacontracts.DrawerListData;
import is.tvpal.R;

/**
 * Created by Þorsteinn on 14.10.2013.
 */
public class CustomDrawerListAdapter extends BaseAdapter
{
    private Context context;
    private int layoutResourceId;
    private List<DrawerListData> options;

    /**
     @param context This is the current context of the application activity
     @param layoutResourceId The id of the xml layout
     @param options List of Service
     */
    public CustomDrawerListAdapter(Context context, int layoutResourceId, List<DrawerListData> options)
    {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.options = options;
    }

    static class DrawerListItem
    {
        ImageView imgIcon;
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        DrawerListItem item;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            item = new DrawerListItem();
            item.imgIcon = (ImageView)row.findViewById(R.id.drawer_list_icon);
            item.name = (TextView) row.findViewById(R.id.drawer_list_title);

            row.setTag(item);
        }
        else
        {
            item = (DrawerListItem)row.getTag();
        }

        DrawerListData dataContract = options.get(position);

        item.name.setText(dataContract.getName());
        if (dataContract.getIcon()!=0) // leave default
            item.imgIcon.setImageResource(dataContract.getIcon());

        return row;
    }

    @Override
    public int getCount()
    {
        return (options == null) ? 0 : options.size();
    }

    @Override
    public DrawerListData getItem(int position)
    {
        return options.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return options.indexOf(getItem(position));
    }
}
