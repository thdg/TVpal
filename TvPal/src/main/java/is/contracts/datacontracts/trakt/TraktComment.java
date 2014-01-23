package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 5.12.2013.
 */
public class TraktComment
{
    @SerializedName("inserted")
    private int inserted;

    @SerializedName("text")
    private String comment;

    @SerializedName("user")
    private TraktUser user;

    public int getInserted() { return this.inserted; }
    public String getComment() { return this.comment; }
    public TraktUser getUser() { return this.user; }
}
