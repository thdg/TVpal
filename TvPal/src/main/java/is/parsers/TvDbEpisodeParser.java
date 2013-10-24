package is.parsers;

import android.content.Context;
import android.graphics.Bitmap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import is.datacontracts.EpisodeData;
import is.handlers.database.DbShowHandler;
import is.rules.BitmapProperties;

/**
 * Created by Arnar on 12.10.2013.
 */
public class TvDbEpisodeParser extends DefaultHandler {

    private String baseURL;
    private List<EpisodeData> episodes;
    private EpisodeData episodeTmp;
    private StringBuilder sb;
    private Context context;
    private String seriesId;

    public TvDbEpisodeParser(String baseUrl, Context context, String seriesId)
    {
        this.baseURL = baseUrl;
        this.episodes = new ArrayList<EpisodeData>();
        episodeTmp = new EpisodeData();
        this.context = context;
        this.seriesId = seriesId;
    }

    public List<EpisodeData> GetEpisodes()
    {
        parseDocument();

        return this.episodes;
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
    public void startElement(String s, String s1, String element, Attributes attributes) throws SAXException
    {
        sb = new StringBuilder();

        if (element.equalsIgnoreCase("Episode"))
        {
            episodeTmp = new EpisodeData();
        }

        if(element.equalsIgnoreCase("FirstAired") || element.equalsIgnoreCase("Overview") || element.equalsIgnoreCase("EpisodeName")
           || element.equalsIgnoreCase("id") || element.equalsIgnoreCase("SeasonNumber") || element.equalsIgnoreCase("EpisodeNumber")
           || element.equalsIgnoreCase("seriesid") || element.equalsIgnoreCase("Rating") || element.equalsIgnoreCase("Director")
           || element.equalsIgnoreCase("poster"))
            sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if(element.equalsIgnoreCase("id"))
        {
            episodeTmp.setEpisodeId(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("SeasonNumber"))
        {
            episodeTmp.setSeasonNumber(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("EpisodeNumber"))
        {
            episodeTmp.setEpisodeNumber(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("FirstAired"))
        {
            episodeTmp.setAired(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("Overview"))
        {
            episodeTmp.setOverview(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("seriesid"))
        {
            episodeTmp.setSeriesId(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("EpisodeName"))
        {
            episodeTmp.setEpisodeName(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("poster"))
        {
            try
            {
                String posterUrl = String.format("http://thetvdb.com/banners/%s", sb.toString());

                BitmapProperties pic = new BitmapProperties();
                byte[] thumbnailByteStream = pic.getBitmapFromURL(posterUrl);
                DbShowHandler db = new DbShowHandler(context);
                db.AddThumbnailToSeries(thumbnailByteStream, seriesId);
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
            sb = null;
        }

        if(element.equalsIgnoreCase("Rating"))
        {
            episodeTmp.setRating(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("Director"))
        {
            episodeTmp.setDirector(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("Episode"))
            episodes.add(episodeTmp);
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
