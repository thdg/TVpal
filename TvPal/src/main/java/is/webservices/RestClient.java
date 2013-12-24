package is.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Arnar on 15.11.2013.
 */
public class RestClient
{
    public String Get(String myurl) throws Exception
    {
        InputStream is = null;

        try
        {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String responseString = convertStreamToString(is);
            return responseString;
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    private String convertStreamToString(InputStream stream) throws Exception
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }

        return sb.toString();
    }
}
