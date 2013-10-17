package is.datacontracts;

/**
 * Created by Arnar on 17.10.2013.
 */
public class EpisodeDataContract
{
    private String episodeId;
    private String seasonNumber;
    private String showNumber;
    private String aired;
    private String overview;

    public String getEpisodeId() { return episodeId; }
    public void setEpisodeId(String episodeId) { this.episodeId = episodeId;}

    public String getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(String seasonNumber) { this.seasonNumber = seasonNumber; }

    public String getShowNumber() { return showNumber; }
    public void setShowNumber(String showNumber) { this.showNumber = showNumber; }

    public String getAired() { return aired; }
    public void setAired(String aired) { this.aired = aired; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    @Override
    public String toString()
    {
        return String.format("%s-%s", seasonNumber, showNumber);
    }
}
