package is.contracts.datacontracts;

/**
 * Created by Arnar on 21.12.2013.
 */
public class StatisticData
{
    private int numberOfShows;
    private int numberOfAiredShows;
    private int numberOfMovies;

    public int getNumberOfShows() { return this.numberOfShows;}
    public void setNumberOfShows(int numberOfShows) { this.numberOfShows = numberOfShows;}

    public int getNumberOfAiredShows() { return this.numberOfAiredShows;}
    public void setNumberOfAiredShows(int numberOfAiredShows) { this.numberOfAiredShows = numberOfAiredShows; }

    public int getNumberOfMovies() { return this.numberOfMovies;}
    public void setNumberOfMovies(int numberOfMovies) { this.numberOfMovies = numberOfMovies;}
}
