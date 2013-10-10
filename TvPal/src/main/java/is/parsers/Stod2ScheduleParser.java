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
import is.datacontracts.EventDataContract;

/**
 * Created by Arnar on 9.10.2013.
 */
public class Stod2ScheduleParser extends DefaultHandler
{
    private String baseURL;
    private List<EventDataContract> events;
    private String tmpValue;
    private EventDataContract eventTmp;
    private String desc;

    public Stod2ScheduleParser(String baseUrl)
    {
        this.baseURL = baseUrl;
        this.events = new ArrayList<EventDataContract>();
    }

    public List<EventDataContract> GetSchedules()
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
            eventTmp = new EventDataContract();
            eventTmp.setStartTime(attributes.getValue("starttime"));
            eventTmp.setDuration(attributes.getValue("duration"));
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
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException
    {
        tmpValue = new String(ac, i, j);
    }
}