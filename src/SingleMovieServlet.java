import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Create a dataSource which registered in web.
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String id = request.getParameter("id");

        // The log message can be found in localhost log
        request.getServletContext().log("getting id: " + id);
        System.out.println("Got id:" + id);
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            //System.out.println(id);
            // Construct a query with parameter represented by "?"
            System.out.println("Running single movie query");
//            String query =
//                    "select movies.title, movies.year, movies.id, movies.director, IFNULL(g.genres, NULL) AS genres, IFNULL(s.stars, NULL) AS stars, IFNULL(ratings.rating, NULL) AS rating" +
//                            " from movies inner join ratings on movies.id = ratings.movieId" +
//                            " inner join ( " +
//                            "select group_concat(distinct g.name order by g.name asc separator ', ') as genres, gm.movieId" +
//                            " from genres g inner join genres_in_movies gm on g.id = gm.genreId group by gm.movieId)" +
//                            " g on g.movieId = movies.id" +
//                            " inner join ( select group_concat(distinct concat(s.id, ':', s.name) order by s.name asc separator ', ')" +
//                            " As stars, sm.movieId" +
//                            " from stars s" +
//                            " inner join stars_in_movies sm on s.id = sm.starId" +
//                            " group by sm.movieId )" +
//                            " s on s.movieId = movies.id" +
//                            " where movies.id = ?";
            String query = "SELECT IFNULL(movies.title, NULL) AS title, IFNULL(movies.year, NULL) AS year, movies.id, IFNULL(movies.director, NULL) AS director, IFNULL(g.genres, NULL) AS genres, IFNULL(s.stars, NULL) AS stars, IFNULL(ratings.rating, NULL) AS rating" +
                    " FROM movies " +
                    "LEFT JOIN ratings ON movies.id = ratings.movieId " +
                    "LEFT JOIN (" +
                    "  SELECT GROUP_CONCAT(DISTINCT g.name ORDER BY g.name ASC SEPARATOR ', ') AS genres, gm.movieId" +
                    "  FROM genres g" +
                    "  INNER JOIN genres_in_movies gm ON g.id = gm.genreId" +
                    "  GROUP BY gm.movieId " +
                    ") g ON g.movieId = movies.id " +
                    "LEFT JOIN (" +
                    "  SELECT GROUP_CONCAT(DISTINCT CONCAT(s.id, ':', s.name) ORDER BY s.name ASC SEPARATOR ', ') AS stars, sm.movieId" +
                    "  FROM stars s" +
                    "  INNER JOIN stars_in_movies sm ON s.id = sm.starId" +
                    "  GROUP BY sm.movieId" +
                    ") s ON s.movieId = movies.id " +
                    "WHERE movies.id = ?";
            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            statement.setString(1, id);

            // Perform the query
            ResultSet rs = statement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            System.out.println("Finished movie query");
            // Iterate through each row of rs
            while (rs.next()) {


                String movieId = rs.getString("id");
                String movieTitle = rs.getString("title");
                String movieYear = rs.getString("year");
                String movieDirector = rs.getString("director");
                String movieGenres = rs.getString("genres");
                String movieStars = rs.getString("stars");
                String movieRating = rs.getString("rating");
                System.out.println(movieRating);
                // Create a JsonObject based on the data we retrieve from rs

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movie_id", movieId);
                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_genres", movieGenres);
                jsonObject.addProperty("movie_stars", movieStars);
                jsonObject.addProperty("movie_rating", movieRating);

                jsonArray.add(jsonObject);
            }
            rs.close();
            statement.close();

            // Write JSON string to output
            out.write(jsonArray.toString());
            // Set response status to 200 (OK)
            response.setStatus(200);

        } catch (Exception e) {
            // Write error message JSON object to output
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());

            // Log error to localhost log
            request.getServletContext().log("Error:", e);
            // Set response status to 500 (Internal Server Error)
            response.setStatus(500);
        } finally {
            out.close();
        }

        // Always remember to close db connection after usage. Here it's done by try-with-resources

    }
}
