package is.parsers.tvdb;

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
import is.contracts.datacontracts.EpisodeData;
import is.contracts.datacontracts.SeriesData;
import is.utilities.PictureTask;

/**
 * This class parses xml files.  It uses the Sax Parser and extends DefaultHandler.
 * It parses episode data from the TheTvDB database.
 * Created by Arnar on 12.10.2013.
 * @see org.xml.sax.helpers.DefaultHandler
 */

public class TvDbEpisodeParser extends DefaultHandler {

    private String baseURL;
    private List<EpisodeData> episodes;
    private EpisodeData episodeTmp;
    private StringBuilder sb;
    public SeriesData series;
    private boolean seriesNode;

    public TvDbEpisodeParser(String baseUrl, SeriesData series)
    {
        this.baseURL = baseUrl;
        this.episodes = new ArrayList<EpisodeData>();
        episodeTmp = new EpisodeData();
        this.series = series;
        this.seriesNode = true;
    }

    public List<EpisodeData> GetEpisodes() throws IOException, SAXException, ParserConfigurationException
    {
        parseDocument();

        return this.episodes;
    }

    public SeriesData getSeries() { return this.series; }

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
            episodeTmp = new EpisodeData();

        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        //Episode info below

        if(element.equalsIgnoreCase("id"))
            episodeTmp.setEpisodeId(Integer.parseInt(sb.toString()));

        if(element.equalsIgnoreCase("SeasonNumber"))
            episodeTmp.setSeasonNumber(Integer.parseInt(sb.toString()));

        if(element.equalsIgnoreCase("EpisodeNumber"))
            episodeTmp.setEpisodeNumber(Integer.parseInt(sb.toString()));

        if(element.equalsIgnoreCase("FirstAired"))
            episodeTmp.setAired(sb.toString());

        if(element.equalsIgnoreCase("Overview"))
            episodeTmp.setOverview(sb.toString());

        if(element.equalsIgnoreCase("seriesid"))
            episodeTmp.setSeriesId(Integer.parseInt(sb.toString()));

        if(element.equalsIgnoreCase("EpisodeName"))
            episodeTmp.setEpisodeName(sb.toString());

        if(element.equalsIgnoreCase("Rating"))
            episodeTmp.setRating(sb.toString());

        if(element.equalsIgnoreCase("Director"))
            episodeTmp.setDirector(sb.toString());

        if(element.equalsIgnoreCase("Episode"))
            episodes.add(episodeTmp);

        if (element.equalsIgnoreCase("GuestStars"))
            episodeTmp.setGuestStars(ArrayToString(sb.toString()));

        //Series info below

        if(element.equalsIgnoreCase("poster"))
        {
            try
            {
                String posterUrl = String.format("http://thetvdb.com/banners/%s", sb.toString());

                PictureTask pic = new PictureTask();
                byte[] posterByteStream = pic.getBitmapFromURL(posterUrl);
                series.setPosterStream(posterByteStream);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }
        }

        if (element.equalsIgnoreCase("Genre"))
            series.setGenres(ArrayToString(sb.toString()));

        if(element.equalsIgnoreCase("lastupdated") && seriesNode)
        {
            series.setLastUpdated(Integer.parseInt(sb.toString()));
            seriesNode = false;
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

    private String ArrayToString(String guestStars)
    {
        String[] temp = guestStars.split("(?!^)\\|");

        StringBuilder sb = new StringBuilder();

        for (String s : temp)
        {
            sb.append(s.replaceFirst("\\|", "") + ", ");
        }

        String actors = sb.toString();
        int length = actors.lastIndexOf(",");
        return actors.substring(0, length);//Remove last comma
    }
}
