import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class RatingManagement {
    private ArrayList<Rating> ratings;
    private ArrayList<Movie> movies;
    private ArrayList<User> users;

    // @Requirement 1
    public RatingManagement(String moviePath, String ratingPath, String userPath) {
        this.movies = loadMovies(moviePath);
        this.users = loadUsers(userPath);
        this.ratings = loadEdgeList(ratingPath);
    }

    private ArrayList<Rating> loadEdgeList(String ratingPath)
    {
        ArrayList<Rating> rtResult = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ratingPath)))
        {
            br.readLine();
            String l;
            while ((l = br.readLine()) != null)
            {
                if (l.trim().isEmpty())
                {
                    continue;
                }
                String[] p = l.split(",");
                rtResult.add(new Rating(Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim()),
                                Integer.parseInt(p[2].trim()), Long.parseLong(p[3].trim())));
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return rtResult;
    }

    private ArrayList<Movie> loadMovies(String moviePath)
    {
        ArrayList<Movie> mvResult = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(moviePath)))
        {
            br.readLine();
            String l;
            while ((l = br.readLine()) != null)
            {
                if (l.trim().isEmpty())
                {
                    continue;
                }
                String[] p = l.split(",");
                String[] genArr = p[2].trim().split("-");
                ArrayList<String> genres = new ArrayList<>();
                for (String gen : genArr)
                {
                    genres.add(gen.trim());
                }
                mvResult.add(new Movie(Integer.parseInt(p[0].trim()), p[1].trim(), genres));
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return mvResult;
    }

    private ArrayList<User> loadUsers(String userPath)
    {
        ArrayList<User> usResult = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userPath)))
        {
            br.readLine();
            String l;
            while ((l = br.readLine()) != null)
            {
                if (l.trim().isEmpty())
                {
                    continue;
                }
                String[] p = l.split(",");
                usResult.add(new User(Integer.parseInt(p[0].trim()), p[1].trim(), 
                                Integer.parseInt(p[2].trim()), p[3].trim(), p[4].trim()));
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return usResult;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Rating> getRating() {
        return ratings;
    }

    // @Requirement 2
    public ArrayList<Movie> findMoviesByNameAndMatchRating(int userId, int rating)
    {
        ArrayList<Movie> mvResult = new ArrayList<>();
        for (Rating rt : ratings)
        {
            if ((rt.getUserID() == userId) && (rt.getRating() >= rating))
            {
                Movie mv = getMovieWithId(rt.getMovieId());
                if ((!mvResult.contains(mv)) && (mv != null))
                {
                    mvResult.add(mv);
                }
            }
        }
        sortMoviesByName(mvResult);
        return mvResult;
    }

    private Movie getMovieWithId(int movieId)
    {
        for (Movie mv : movies)
        {
            if (mv.getId() == movieId)
            {
                return mv;
            }
        }
        return null;
    }

    private void sortMoviesByName(ArrayList<Movie> movies)
    {
        Collections.sort(movies, new Comparator<Movie>()
        {
            @Override
            public int compare(Movie mv1, Movie mv2)
            {
                return mv1.getName().compareToIgnoreCase(mv2.getName());
            }
        });
    }

    // Requirement 3
    public ArrayList<User> findUsersHavingSameRatingWithUser(int userId, int movieId)
    {
        ArrayList<User> usResult = new ArrayList<>();
        int userRt = getUserWithRating(userId, movieId);
        if (userRt == -1)
        {
            return usResult;
        }
        for (Rating rt : ratings)
        {
            if ((rt.getUserID() != userId) && (rt.getMovieId() == movieId) 
                    && (rt.getRating() == userRt))
            {
                User us = getUserWithId(rt.getUserID());
                if (!usResult.contains(us) && (us != null))
                {
                    usResult.add(us);
                }
            }
        }
        return usResult;
    }

    private int getUserWithRating(int userId, int movieId)
    {
        for (Rating rt : ratings)
        {
            if ((rt.getUserID() == userId) && (rt.getMovieId() == movieId))
            {
                return rt.getRating();
            }
        }
        return -1;
    }

    private User getUserWithId(int userId)
    {
        for (User us : users)
        {
            if (us.getId() == userId)
            {
                return us;
            }
        }
        return null;
    }

    // Requirement 4
    public ArrayList<String> findMoviesNameHavingSameReputation()
    {
        ArrayList<String> result = new ArrayList<>();
        int[] movieReputationCount = countMoviesWithReputation();
        for (Movie mv : movies)
        {
            int reCount = movieReputationCount[mv.getId()];
            if (reCount >= 2)
            {
                result.add(mv.getName());
            }
        }
        Collections.sort(result);
        return result;
    }

    private int[] countMoviesWithReputation()
    {
        int[] mvReCount = new int[10000];
        for (Rating rt : ratings)
        {
            if (rt.getRating() > 3) 
            {
                mvReCount[rt.getMovieId()]++;
            }
        }
        return mvReCount;
    }

    // @Requirement 5
    public ArrayList<String> findMoviesMatchOccupationAndGender(String occupation, String gender, int k, int rating)
    {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> added = new ArrayList<>();
        for (User us : users)
        {
            if (us.getGender().equalsIgnoreCase(gender) 
                && us.getOccupation().equalsIgnoreCase(occupation))
            {
                for (Rating rt : ratings)
                {
                    if ((us.getId() == rt.getUserID()) && (rt.getRating() == rating))
                    {
                        Movie mv = getMovieWithId(rt.getMovieId());
                        if (!added.contains(mv.getName()) && (mv != null))
                        {
                            added.add(mv.getName());
                            result.add(mv.getName());
                        }
                    }
                }
            }
        }
        Collections.sort(result);
        result = limitSizeOfResult(result, k);
        return result;
    }

    private ArrayList<String> limitSizeOfResult(ArrayList<String> result, int k)
    {
        if (result.size() > k)
        {
            result = new ArrayList<>(result.subList(0, k));
        }
        return result;
    }

    // @Requirement 6
    public ArrayList<String> findMoviesByOccupationAndLessThanRating(String occupation, int k, int rating)
    {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> added = new ArrayList<>();
        for (User us : users)
        {
            if (us.getOccupation().equalsIgnoreCase(occupation))
            {
                getMoviesWithLessRating(added, result, us.getId(), rating);
            }
        }
        Collections.sort(result);
        result = limitSizeOfResult(result, k);
        return result;
    }

    private void getMoviesWithLessRating(ArrayList<String> result, ArrayList<String> addedMovies, int userId, int rating)
    {
        for (Rating rt : ratings)
        {
            if ((rt.getUserID() == userId) && (rt.getRating() < rating))
            {
                Movie mv = getMovieWithId(rt.getMovieId());
                if (!addedMovies.contains(mv.getName()) && (mv != null))
                {
                    addedMovies.add(mv.getName());
                    result.add(mv.getName());
                }
            }
        }
    }

    // @Requirement 7
    public ArrayList<String> findMoviesMatchLatestMovieOf(int userId, int rating, int k) {
        /* code here */
        return null; /* change here */
    }
}