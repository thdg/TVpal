package is.datacontracts;

/**
 * Created by Arnar on 12.10.2013.
 */
public class ShowData
{
    private String seriesId;
    private String title;
    private String overview;
    private String network;

    public String getSeriesId() { return this.seriesId; }
    public void setSeriesId(String seriesId) { this.seriesId = seriesId; }

    public String getTitle()  {return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() {return this.overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getNetwork() { return this.network; }
    public void setNetwork(String network) { this.network = network; }

    @Override
    public String toString()
    {
        return this.title;
    }
}
