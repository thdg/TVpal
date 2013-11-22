package is.activites.showActivities;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import is.tvpal.R;

/**
 * An activity to search for episodes and add them to "MyShows"
 * @author Arnar
 * @see is.activites.showActivities.MyShowsActivity
 */

public class SearchMovieFragment extends Fragment
{
    private EditText _editSearch;
    private Context context;

    public SearchMovieFragment(Context context)
    {
        this.context = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        _editSearch = (EditText) getView().findViewById(R.id.traktSearchMovie);

        InitializeEditTextSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_search_movie, container, false);
    }

    private void InitializeEditTextSearch()
    {
        _editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    performSearch();

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //Close the keyboard

                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch()
    {
    }
}