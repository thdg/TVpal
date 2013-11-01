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
    private ShowData showTmp;
    private StringBuilder sb;

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
    public void startElement(String s, String s1, String element, Attributes attributes) throws SAXException
    {
        if (element.equalsIgnoreCase("Series"))
            showTmp = new ShowData();

        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if (element.equalsIgnoreCase("Series"))
            shows.add(showTmp);

        if (element.equalsIgnoreCase("seriesid"))
            showTmp.setSeriesId(sb.toString());

        if (element.equalsIgnoreCase("SeriesName"))
            showTmp.setTitle(sb.toString());

        if (element.equalsIgnoreCase("Overview"))
            showTmp.setOverview(sb.toString());

        if (element.equalsIgnoreCase("Network"))
            showTmp.setNetwork(sb.toString());

        if (element.equalsIgnoreCase("FirstAired"))
            showTmp.setFirstAired(sb.toString());

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
