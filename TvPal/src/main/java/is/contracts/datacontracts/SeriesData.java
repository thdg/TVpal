package is.contracts.datacontracts;

/**
 * Created by Arnar on 12.10.2013.
 */
public class SeriesData
{
    private int seriesId;
    private String title;
    private String overview;
    private String network;
    private String firstAired;
    private String banner;
    private byte[] posterStream;
    private String genres;
    private int lastUpdated;
    private String actors;
    private String imdbid;

    public int getSeriesId() { return this.seriesId; }
    public void setSeriesId(int seriesId) { this.seriesId = seriesId; }

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

    public int getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(int lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getActors() { return actors; }
    public void setActors(String actors) { this.actors = actors; }

    public String getImdbid() { return imdbid; }
    public void setImdbid(String imdbid) { this.imdbid = imdbid; }

    @Override
    public String toString()
    {
        return this.title;
    }
}
