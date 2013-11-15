package is.contracts.datacontracts;

/**
 * Created by Arnar on 15.11.2013.
 */
public class TraktData
{
    public String title;
    public String overview;
    public String tvdbid;

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return this.overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getTvdbid() { return this.tvdbid; }
    public void setTvdbid(String tvdbid) { this.tvdbid = tvdbid; }
}
