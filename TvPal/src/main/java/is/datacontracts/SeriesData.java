package is.datacontracts;

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

    @Override
    public String toString()
    {
        return this.title;
    }

}
