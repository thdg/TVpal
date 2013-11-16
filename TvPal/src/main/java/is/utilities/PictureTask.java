package is.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to download picture from a given uri
 * Created by Arnar on 19.10.2013.
 */
public class PictureTask
{
    public byte[] getByteStreamFromUrl(String src) throws IOException
    {
        URL url = new URL(src);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();

        return out.toByteArray();
    }

    public Bitmap getBitmapFromUrl(String src)
    {
        try
        {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        }
        catch (IOException e)
        {
            Log.e(getClass().getName(), e.getMessage());
            return null;
        }
    }
}
