package is.parsers.schedules;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import is.contracts.datacontracts.EventData;
import is.contracts.servicecontracts.IScheduleService;

/**
 * This class to parse xml files.  It uses the Sax Parser and extends DefaultHandler.
 * It parses Skjárinn schedules.
 *
 * @Created by Arnar
 * @see org.xml.sax.helpers.DefaultHandler
 */
public class SkjarinnScheduleParser extends DefaultHandler implements IScheduleService
{
    private String xmlResponse;
    private List<EventData> events;
    private EventData eventTmp;
    private String serviceName;
    private StringBuilder sb;

    public SkjarinnScheduleParser(String xmlResponse)
    {
        this.xmlResponse = xmlResponse;
        this.events = new ArrayList<EventData>();
    }

    public List<EventData> GetSchedules() throws IOException, SAXException, ParserConfigurationException
    {
        parseDocument();
        return this.events;
    }

    private void parseDocument() throws ParserConfigurationException, SAXException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(new InputSource(new StringReader(xmlResponse)), this);
    }

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException
    {
        if (elementName.equalsIgnoreCase("event"))
        {
            eventTmp = new EventData();
            eventTmp.setStartTime(String.format("%s:00",attributes.getValue("start-time")));
            eventTmp.setEventDate(String.format("%s:00", attributes.getValue("start-time")));
            eventTmp.setDuration(attributes.getValue("duration"));
        }

        if(elementName.equalsIgnoreCase("episode"))
        {
            eventTmp.setCurrentEpisode(attributes.getValue("number"));
            eventTmp.setNumberOfEpisodes(attributes.getValue("number-of-episodes"));
        }

        if(elementName.equalsIgnoreCase("service"))
            serviceName = attributes.getValue("service-name");

        sb = new StringBuilder();
    }

    @Override
    public void endElement(String s, String s1, String element) throws SAXException
    {
        if (element.equals("event")) {
            eventTmp.setServiceName(serviceName);
            events.add(eventTmp);
        }
        if (element.equalsIgnoreCase("title")){
            eventTmp.setTitle(sb.toString());
        }

        if (element.equalsIgnoreCase("description")) {
            eventTmp.setDescription(sb.toString());
        }

        if(element.equalsIgnoreCase("subtitle")) {
            eventTmp.setSubtitles(sb.toString());
        }
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
