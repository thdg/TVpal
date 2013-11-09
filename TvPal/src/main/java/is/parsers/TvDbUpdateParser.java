package is.parsers;

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

/**
 * This class parses xml files.  It uses the Sax Parser and extends DefaultHandler.
 * It parses series data from the TheTvDB database, and only takes episodes that have been updated.
 * Created by Arnar on 12.10.2013.
 * @see org.xml.sax.helpers.DefaultHandler
 */

public class TvDbUpdateParser extends DefaultHandler {

    private String baseURL;
    private List<EpisodeData> episodes;
    private EpisodeData episodeTmp;
    private StringBuilder sb;
    private int lastUpdate;
    private boolean needToUpdate;

    public TvDbUpdateParser(String baseUrl, int lastUpdate)
    {
        this.baseURL = baseUrl;
        this.episodes = new ArrayList<EpisodeData>();
        this.lastUpdate = lastUpdate;
        this.needToUpdate = false;
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
        if (element.equalsIgnoreCase("Episode"))
            episodeTmp = new EpisodeData();

        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        //A check to make sure we are inside a Episode element, not Series
        if (episodeTmp == null) return;

        if (element.equalsIgnoreCase("Episode") && needToUpdate)
        {
            episodes.add(episodeTmp);
            needToUpdate = false;
        }

        if (element.equalsIgnoreCase("lastupdated"))
        {
            int episodeLastUpdated = Integer.parseInt(sb.toString());

            if (episodeLastUpdated > lastUpdate) {
                needToUpdate = true; }
        }

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

        if(element.equalsIgnoreCase("Rating"))
            episodeTmp.setRating(sb.toString());

        if(element.equalsIgnoreCase("Director"))
            episodeTmp.setDirector(sb.toString());

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
