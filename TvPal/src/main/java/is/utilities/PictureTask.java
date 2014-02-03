package is.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

    public static Bitmap getBitmapFromUrl(String src)
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
            return null;
        }
    }

    //Use this method when bitmaps are really large in size
    public Bitmap getResizedBitmap(String posterUrl, int newHeight, int newWidth)
    {
        Bitmap image = getBitmapFromUrl(posterUrl);

        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        return Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
    }
}
