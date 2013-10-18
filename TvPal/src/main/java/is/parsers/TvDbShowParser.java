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

import is.datacontracts.ShowData;

/**
 * Created by Arnar on 12.10.2013.
 */
public class TvDbShowParser extends DefaultHandler {

    private String baseURL;
    private List<ShowData> shows;
    private String tmpValue;
    private ShowData showTmp;

    public TvDbShowParser(String baseUrl)
    {
        this.baseURL = baseUrl;
        this.shows = new ArrayList<ShowData>();
    }

    public List<ShowData> GetShows()
    {
        parseDocument();

        return this.shows;
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
        if (elementName.equalsIgnoreCase("Series"))
            showTmp = new ShowData();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if (element.equalsIgnoreCase("Series"))
            shows.add(showTmp);

        if (element.equalsIgnoreCase("seriesid"))
            showTmp.setSeriesId(tmpValue);

        if (element.equalsIgnoreCase("SeriesName"))
            showTmp.setTitle(tmpValue);

        if (element.equalsIgnoreCase("Overview"))
            showTmp.setOverview(tmpValue);

        if (element.equalsIgnoreCase("Network"))
            showTmp.setNetwork(tmpValue);
    }

    @Override
    public void characters(char[] ac, int i, int j) throws SAXException
    {
        tmpValue = new String(ac, i, j);
    }
}
