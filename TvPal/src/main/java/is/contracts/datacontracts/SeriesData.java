package is.contracts.datacontracts;

/**
 * Created by Arnar on 12.10.2013.
 */
public class SeriesData
{
    private String seriesId;
    private String title;
    private String overview;
    private String network;
    private String firstAired;
    private String banner;
    private byte[] posterStream;
    private String genres;
    private String lastUpdated;

    public String getSeriesId() { return this.seriesId; }
    public void setSeriesId(String seriesId) { this.seriesId = seriesId; }

    public String getTitle()  {return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() {return this.overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getNetwork() { return this.network; }
    public void setNetwork(String network) { this.network = network; }

    public String getFirstAired() { return firstAired; }
    public void setFirstAired(String firstAired) { this.firstAired = firstAired; }

    public String getBanner() { return banner; }
    public void setBanner(String banner) { this.banner = banner; }

    public byte[] getPosterStream() { return posterStream; }
    public void setPosterStream(byte[] posterStream) { this.posterStream = posterStream; }

    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres;}

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    @Override
    public String toString()
    {
        return this.title;
    }
}
