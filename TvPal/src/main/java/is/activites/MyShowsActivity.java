package is.activites;

import android.app.ListActivity;
import android.os.Bundle;
import java.util.List;
import is.datacontracts.ShowDataContract;
import is.handlers.CustomMyShowsAdapter;
import is.handlers.DataBaseHandler;
import is.tvpal.R;

public class MyShowsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        List<ShowDataContract> myShows = GetMyShows();

        setListAdapter(new CustomMyShowsAdapter(this, R.layout.listview_item_my_shows, myShows));
    }

    private List<ShowDataContract> GetMyShows()
    {
        DataBaseHandler db = new DataBaseHandler(this);

        return db.GetAllSeries();
    }
}
