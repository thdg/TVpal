package is.parsers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import is.utilities.PictureTask;

/**
 * This class parses xml files.  It uses the Sax Parser and extends DefaultHandler.
 * It parses a single episode data from the TheTvDB database and downloads a episode picture as a BitMap
 * Created by Arnar on 12.10.2013.
 * @see org.xml.sax.helpers.DefaultHandler
 */

public class TvDbPictureParser extends DefaultHandler {

    private String baseURL;
    private Bitmap episodePicture;
    private StringBuilder sb;

    public TvDbPictureParser(String baseUrl)
    {
        this.baseURL = baseUrl;
    }

    public Bitmap GetEpisodePicture()
    {
        parseDocument();

        return this.episodePicture;
    }

    private void parseDocument()
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try
        {
            SAXParser parser = factory.newSAXParser();
            parser.parse(baseURL, this);
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException
    {
        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if(element.equalsIgnoreCase("filename"))
        {
            try
            {
                String pictureUrl = String.format("http://thetvdb.com/banners/%s", sb.toString());
                PictureTask bit = new PictureTask();
                byte[] pictureByteStream = bit.getBitmapFromURL(pictureUrl);
                episodePicture = BitmapFactory.decodeByteArray(pictureByteStream, 0, pictureByteStream.length);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }
        }

        sb = null;
    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException
    {
        if(sb != null)
        {
            for (int k= i; k<i+j; k++) {
                sb.append(ac[k]);
            }
        }
    }
}
