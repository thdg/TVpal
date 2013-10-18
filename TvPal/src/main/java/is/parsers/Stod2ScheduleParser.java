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

import is.datacontracts.EventData;

/**
 * This class to parse xml files.  It uses the Sax Parser and extends DefaultHandler.
 * It parses Stöð 2 schedules.
 *
 * Created by Arnar
 * @see org.xml.sax.helpers.DefaultHandler
 */
public class Stod2ScheduleParser extends DefaultHandler
{
    private String baseURL;
    private List<EventData> events;
    private String tmpValue;
    private EventData eventTmp;

    public Stod2ScheduleParser(String baseUrl)
    {
        this.baseURL = baseUrl;
        this.events = new ArrayList<EventData>();
    }

    public List<EventData> GetSchedules()
    {
        parseDocument();

        return this.events;
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
        if (elementName.equalsIgnoreCase("event"))
        {
            eventTmp = new EventData();
            eventTmp.setStartTime(attributes.getValue("starttime"));
            eventTmp.setDuration(attributes.getValue("duration"));
            eventTmp.setEventDate(attributes.getValue("starttime"));
        }

        if(elementName.equalsIgnoreCase("series"))
        {
            eventTmp.setCurrentEpisode(attributes.getValue("episode"));
        }
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if (element.equals("event")) {
            events.add(eventTmp);
        }
        if (element.equalsIgnoreCase("title")){
            eventTmp.setTitle(tmpValue);
        }

        if (element.equalsIgnoreCase("description")) {
            eventTmp.setDescription(tmpValue);
        }

        if (element.equalsIgnoreCase("service")) {
            eventTmp.setServiceName(tmpValue);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException
    {
        tmpValue = new String(ac, i, j);
    }
}
