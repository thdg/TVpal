package is.contracts.datacontracts;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Arnar on 17.10.2013.
 */
public class EpisodeData implements Serializable
{
    private int episodeId;
    private int seriesId;
    private int  seasonNumber;
    private int  episodeNumber;
    private String episodeName;
    private String aired;
    private String overview;
    private int seen;
    private Bitmap picture;
    private String director;
    private String rating;
    private String guestStars;

    public int getEpisodeId() { return episodeId; }
    public void setEpisodeId(int episodeId) { this.episodeId = episodeId;}

    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getAired() { return aired; }
    public void setAired(String aired) { this.aired = aired; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public int getSeriesId() { return seriesId; }
    public void setSeriesId(int seriesId) { this.seriesId = seriesId; }

    public String getEpisodeName() { return episodeName; }
    public void setEpisodeName(String episodeName) { this.episodeName = episodeName; }

    public int getSeen() { return seen; }
    public void setSeen(int seen){ this.seen = seen; }

    public Bitmap getPicture() { return picture; }
    public void setPicture(Bitmap picture) { this.picture = picture;}

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating;}

    public String getGuestStars() { return guestStars; }
    public void setGuestStars(String guestStars) { this.guestStars = guestStars; }
}
