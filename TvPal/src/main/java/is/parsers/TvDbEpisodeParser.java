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
 * Created by Arnar on 12.10.2013.
 */
public class TvDbEpisodeParser extends DefaultHandler {

    private String baseURL;
    private List<EpisodeData> episodes;
    private String tmpValue;
    private EpisodeData episodeTmp;
    private StringBuilder sb;

    public TvDbEpisodeParser(String baseUrl)
    {
        this.baseURL = baseUrl;
        this.episodes = new ArrayList<EpisodeData>();
        episodeTmp = new EpisodeData();
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
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException
    {
        if (elementName.equalsIgnoreCase("Episode"))
        {
            episodeTmp = new EpisodeData();
        }

        if(elementName.equalsIgnoreCase("Overview"))
            sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if(element.equalsIgnoreCase("id"))
            episodeTmp.setEpisodeId(tmpValue);

        if(element.equalsIgnoreCase("SeasonNumber"))
            episodeTmp.setSeasonNumber(tmpValue);

        if(element.equalsIgnoreCase("EpisodeNumber"))
            episodeTmp.setEpisodeNumber(tmpValue);

        if(element.equalsIgnoreCase("FirstAired"))
            episodeTmp.setAired(tmpValue);

        if(element.equalsIgnoreCase("Overview"))
        {
            episodeTmp.setOverview(sb.toString());
            sb = null;
        }

        if(element.equalsIgnoreCase("seriesid"))
            episodeTmp.setSeriesId(tmpValue);

        if(element.equalsIgnoreCase("EpisodeName"))
            episodeTmp.setEpisodeName(tmpValue);

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
        else
            tmpValue = new String(ac, i, j);
    }
}