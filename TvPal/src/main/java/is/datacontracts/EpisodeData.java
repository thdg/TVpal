package is.datacontracts;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Arnar on 17.10.2013.
 */
public class EpisodeData implements Serializable
{
    private String episodeId;
    private String seriesId;
    private String seasonNumber;
    private String episodeNumber;
    private String episodeName;
    private String aired;
    private String overview;
    private String seen;
    private Bitmap picture;
    private String director;
    private String rating;

    public String getEpisodeId() { return episodeId; }
    public void setEpisodeId(String episodeId) { this.episodeId = episodeId;}

    public String getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(String seasonNumber) { this.seasonNumber = seasonNumber; }

    public String getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(String episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getAired() { return aired; }
    public void setAired(String aired) { this.aired = aired; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getSeriesId() { return seriesId; }
    public void setSeriesId(String seriesId) { this.seriesId = seriesId; }

    public String getEpisodeName() { return episodeName; }
    public void setEpisodeName(String episodeName) { this.episodeName = episodeName; }

    public String getSeen() { return seen; }
    public void setSeen(String seen){ this.seen = seen; }

    public Bitmap getPicture() { return picture; }
    public void setPicture(Bitmap picture) { this.picture = picture;}

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating;}
}
