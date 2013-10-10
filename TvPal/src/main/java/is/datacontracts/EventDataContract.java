package is.datacontracts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arnar on 28.9.2013.
 */
public class EventDataContract
{
    private String title;
    private String startTime;
    private String currentEpisode;
    private String numberOfEpisodes;
    private String category;
    private String subtitles;
    private String description;
    private String duration;
    private String eventDate;

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getStartTime() { return this.startTime; }
    public void setStartTime(String startTime) { this.startTime = setCorrectDateFormat(startTime); }

    public String getCurrentEpisode() { return this.currentEpisode; }
    public void setCurrentEpisode(String currentEpisode) { this.currentEpisode = currentEpisode; }

    public String getNumberOfEpisodes() { return this.numberOfEpisodes; }
    public void setNumberOfEpisodes(String numberOfEpisodes) { this.numberOfEpisodes = numberOfEpisodes; }

    public String getCategory() { return this.category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubtitles() { return this.subtitles; }
    public void setSubtitles(String subtitles) { this.subtitles = subtitles; }

    public String getDescription() { return this.description; }
    public void setDescription (String description) { this.description = description; }

    public String getDuration() { return this.duration; }
    public void setDuration(String duration) { this.duration= duration; }

    public String getEventDate() { return this.eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = setCorrectEventDateFormat(eventDate); }

    public String setCorrectDateFormat(String startTime)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = dt.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm");
        return dt1.format(date);
    }

    public String setCorrectEventDateFormat(String eventDate)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = dt.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        return dt1.format(date);
    }

    @Override
    public String toString()
    {
        return String.format("%s - %s", this.startTime, this.title);
    }
}
