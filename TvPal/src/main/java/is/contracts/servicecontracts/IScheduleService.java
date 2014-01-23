package is.contracts.servicecontracts;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import is.contracts.datacontracts.EventData;

/**
 * Created by Arnar on 14.11.2013.
 */
public interface IScheduleService
{
    List<EventData> GetSchedules() throws IOException, SAXException, ParserConfigurationException;
}
