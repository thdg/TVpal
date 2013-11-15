package is.parsers.tvdb;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import is.contracts.datacontracts.SeriesData;

/**
 * This class parses xml files.  It uses the Sax Parser and extends DefaultHandler.
 * It parses series data from the TheTvDB database.
 * Created by Arnar on 12.10.2013.
 * @see org.xml.sax.helpers.DefaultHandler
 */

public class TvDbShowParser extends DefaultHandler {

    private String baseURL;
    private List<SeriesData> shows;
    private SeriesData showTmp;
    private StringBuilder sb;

    public TvDbShowParser(String baseUrl)
    {
        this.baseURL = baseUrl;
        this.shows = new ArrayList<SeriesData>();
    }

    public List<SeriesData> GetShows() throws IOException, SAXException, ParserConfigurationException
    {
        parseDocument();
        return this.shows;
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
        if (element.equalsIgnoreCase("Series"))
            showTmp = new SeriesData();

        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if (element.equalsIgnoreCase("Series"))
            shows.add(showTmp);

        if (element.equalsIgnoreCase("seriesid"))
            showTmp.setSeriesId(Integer.parseInt(sb.toString()));

        if (element.equalsIgnoreCase("SeriesName"))
            showTmp.setTitle(sb.toString());

        if (element.equalsIgnoreCase("Overview"))
            showTmp.setOverview(sb.toString());

        if (element.equalsIgnoreCase("Network"))
            showTmp.setNetwork(sb.toString());

        if (element.equalsIgnoreCase("FirstAired"))
            showTmp.setFirstAired(sb.toString());

        if(element.equalsIgnoreCase("banner"))
            showTmp.setBanner(sb.toString());

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
