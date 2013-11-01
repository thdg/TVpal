package is.parsers;

import android.content.Context;
import android.util.Log;

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
import is.utilities.BitmapProperties;

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

    public List<EpisodeData> GetEpisodes() throws IOException, SAXException, ParserConfigurationException
    {
        parseDocument();

        return this.episodes;
    }

    private void parseDocument() throws ParserConfigurationException, SAXException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(baseURL, this);

    }

    @Override
    public void startElement(String s, String s1, String element, Attributes attributes) throws SAXException
    {
        sb = new StringBuilder();

        if (element.equalsIgnoreCase("Episode"))
        {
            episodeTmp = new EpisodeData();
        }

        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if(element.equalsIgnoreCase("id"))
            episodeTmp.setEpisodeId(sb.toString());

        if(element.equalsIgnoreCase("SeasonNumber"))
            episodeTmp.setSeasonNumber(sb.toString());

        if(element.equalsIgnoreCase("EpisodeNumber"))
            episodeTmp.setEpisodeNumber(sb.toString());

        if(element.equalsIgnoreCase("FirstAired"))
            episodeTmp.setAired(sb.toString());

        if(element.equalsIgnoreCase("Overview"))
            episodeTmp.setOverview(sb.toString());

        if(element.equalsIgnoreCase("seriesid"))
            episodeTmp.setSeriesId(sb.toString());

        if(element.equalsIgnoreCase("EpisodeName"))
            episodeTmp.setEpisodeName(sb.toString());

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
                Log.e(getClass().getName(), ex.getMessage());
            }
        }

        if(element.equalsIgnoreCase("Rating"))
            episodeTmp.setRating(sb.toString());

        if(element.equalsIgnoreCase("Director"))
            episodeTmp.setDirector(sb.toString());

        if(element.equalsIgnoreCase("Episode"))
            episodes.add(episodeTmp);

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
