package is.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    //This method seems to be alot slower to download bitmaps than getBitmapFromURL2
    public byte[] getBitmapFromURL(String link) throws IOException
    {
        URL url = new URL(link);
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

    //Use this badboy method to download bitmaps
    public Bitmap getBitmapFromURL2(String src)
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
            e.printStackTrace();
            return null;
        }
    }
}
