package is.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Arnar on 5.12.2013.
 */
public class ExternalIntents
{
    public static final String IMDB_URL = "http://www.imdb.com/title/";

    public static void StartIMDBIntent(Context context, String imdbId)
    {
        //First try to open the IMDB app if it is installed, else open with browser
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("imdb:///title/" + imdbId));
            context.startActivity(intent);
        }
        catch (Exception ex) {
            try{
                StartUriIntent(context, IMDB_URL + imdbId);
            }
            catch (Exception e) {
                Toast.makeText(context, "Couldn't open IMDB", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void StartUriIntent(Context context, String uri)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(uri));
        context.startActivity(i);
    }
}
