package is.activites;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MyShowsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Initialize();
    }

    private void Initialize()
    {
        TextView tv = new TextView(this);

        tv.setText("Arnar");

        setContentView(tv);
    }
}
