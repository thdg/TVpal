package is.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import is.rules.ConnectionListener;
import is.rules.Helpers;
import is.tvpal.R;

public class MainActivity extends Activity
{
    private ConnectionListener _connectivityListener;
    private String lol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();
    }

    private void Initialize()
    {
        _connectivityListener = new ConnectionListener();
    }

    public void getSchedulesRuv_clickEvent(View view)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if (networkAvailable)
            {
                Intent intent = new Intent(this, DisplayRuvActivity.class);
                startActivity(intent);
            }
            else
            {
                Helpers.showNetworkAlertDialog(this);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getSchedulesStod2_clickEvent(View view)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if (networkAvailable)
            {
                Intent intent = new Intent(this, DisplayStod2Activity.class);
                startActivity(intent);
            }
            else
            {
                Helpers.showNetworkAlertDialog(this);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getSchedulesSkjarinn_clickEvent(View view)
    {
        try
        {
            boolean networkAvailable = _connectivityListener.isNetworkAvailable(this);

            if (networkAvailable)
            {
                Intent intent = new Intent(this, DisplaySkjarinnActivity.class);
                startActivity(intent);
            }
            else
            {
                Helpers.showNetworkAlertDialog(this);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
