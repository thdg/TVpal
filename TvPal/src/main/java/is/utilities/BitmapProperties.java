package is.utilities;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Arnar on 19.10.2013.
 */
public class BitmapProperties
{
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
}
