package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arnar on 23.11.2013.
 */
public class TraktPeople
{
    @SerializedName("directors")
    private List<TraktPerson> directors;

    @SerializedName("actors")
    private List<TraktPerson> actors;

    public List<TraktPerson> getDirectors() { return this.directors; };
    public List<TraktPerson> getActors() { return this.actors; }
}
