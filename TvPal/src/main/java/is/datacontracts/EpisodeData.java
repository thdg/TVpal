package is.datacontracts;

/**
 * Created by Arnar on 17.10.2013.
 */
public class EpisodeData
{
    private String episodeId;
    private String seriesId;
    private String seasonNumber;
    private String episodeNumber;
    private String episodeName;
    private String aired;
    private String overview;
    private boolean seen;

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

    public boolean getSeen() { return seen; }
    public void setSeen(int seen){ this.seen = seen == 1; }

    @Override
    public String toString()
    {
        return "Season " + seasonNumber;
    }
}
