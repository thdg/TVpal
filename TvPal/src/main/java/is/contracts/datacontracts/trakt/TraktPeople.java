package is.contracts.datacontracts.trakt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arnar on 23.11.2013.
 */
public class TraktPeople
{
    @SerializedName("directors")
    public List<TraktPerson> directors;

    @SerializedName("writers")
    public List<TraktPerson> writers;

    @SerializedName("producers")
    public List<TraktPerson> producers;

    @SerializedName("actors")
    public List<TraktPerson> actors;

    public List<TraktPerson> getDirectors() { return this.directors; };
    public List<TraktPerson> getWriters() { return this.writers; };
    public List<TraktPerson> getProducers() { return this.producers; }
    public List<TraktPerson> getActors() { return this.actors; }
}
