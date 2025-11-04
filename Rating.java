public class Rating
{
    private int userId;
    private int movieId;
    private int rating;
    private long timestamps;

    public Rating(int userId, int movieId, int rating, long timestamps)
    {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamps = timestamps;
    }

    public int getUserID()
    {
        return userId;
    }

    public void setUserID(int userId)
    {
        this.userId = userId;
    }

    public int getMovieId()
    {
        return movieId;
    }

    public void setMovieId(int movieId)
    {
        this.movieId = movieId;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public long getTimestamps()
    {
        return timestamps;
    }

    public void setTimestamps(long timestamps)
    {
        this.timestamps = timestamps;
    }

    @Override
    public String toString()
    {
        return String.format("Rating[%d, %d, %d, %d]", userId, movieId, rating, timestamps);
    }
}