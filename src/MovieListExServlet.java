import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "MovieListExServlet", urlPatterns = "/api/movie-list-ex")
public class MovieListExServlet extends HttpServlet {
    private static final long serialVersionUID = 2L;

    // Create a dataSource which registered in web.xml
    private DataSource dataSource;

    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json"); // Response mime type

        // Retrieve parameter id from url request.
        String genre = request.getParameter("genre");
        //Retrieve parameter alpha from url
        String alpha = request.getParameter("alpha");
        System.out.println("Genres: " + genre + " Alpha: " + alpha);
        // The log message can be found in localhost log
        request.getServletContext().log("getting genre: " + genre);

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        // Get a connection from dataSource and let resource manager close the connection after usage.
        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            // Construct a query with parameter represented by "?"
//            String query =
//                    "select distinct movies.title, movies.year, movies.id, movies.director, g.genres, s.stars, ratings.rating" +
//                            " from movies inner join ratings on movies.id = ratings.movieId" +
//                            " inner join (" +
//                            "select group_concat(distinct g.name order by g.name asc separator ', ') as genres, gm.movieId" +
//                            " from genres g inner join genres_in_movies gm on g.id = gm.genreId group by gm.movieId)" +
//                            " g on g.movieId = movies.id" +
//                            " inner join (select substring_index(group_concat(distinct concat(s.id, ':', s.name) order by s.name asc separator ', '), ', ', 3)" +
//                            " As stars, sm.movieId" +
//                            " from stars s" +
//                            " inner join stars_in_movies sm on s.id = sm.starId" +
//                            " group by sm.movieId)" +
//                            " s on s.movieId = movies.id" +
//                            " order by rating desc " +
//                            "limit 20";
            String query = "SELECT DISTINCT movies.title, movies.year, movies.id, movies.director, g.genres, s.stars, ratings.rating " +
                        "FROM movies " +
                        "INNER JOIN ratings ON movies.id = ratings.movieId " +
                        "INNER JOIN (" +
                        "  SELECT group_concat(DISTINCT g.name ORDER BY g.name ASC SEPARATOR ', ') AS genres, gm.movieId " +
                        "  FROM genres g " +
                        "  INNER JOIN genres_in_movies gm ON g.id = gm.genreId " +
                        "  GROUP BY gm.movieId" +
                        ") g ON g.movieId = movies.id " +
                        "INNER JOIN (" +
                        "  SELECT substring_index(group_concat(DISTINCT CONCAT(s.id, ':', s.name) ORDER BY s.name ASC SEPARATOR ', '), ', ', 3) AS stars, sm.movieId " +
                        "  FROM stars s " +
                        "  INNER JOIN stars_in_movies sm ON s.id = sm.starId " +
                        "  GROUP BY sm.movieId" +
                        ") s ON s.movieId = movies.id ";
            if (genre != null){
                query = query +
                        "INNER JOIN genres_in_movies gim ON gim.movieId = movies.id " +
                        "INNER JOIN genres genre ON genre.id = gim.genreId " +
                        "WHERE lower(genre.name) = lower(" +  "'" + genre +"'" + ") ";
            }
            if (alpha != null){
                query = query + "WHERE movies.title LIKE " + "'" + alpha + "%' ";
            }
            query = query + "ORDER BY ratings.rating DESC";
            // Declare our statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Set the parameter represented by "?" in the query to the id we get from url,
            // num 1 indicates the first "?" in the query
            //statement.setString(1, id);
            System.out.println(query);
            // Perform the query
            ResultSet rs = statement.executeQuery(query);

            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {

                String movieTitle = rs.getString("title");
                String movieYear = rs.getString("year");
                String movieId = rs.getString("id");
                String movieDirector = rs.getString("director");
                String genres = rs.getString("genres");
                String stars = rs.getString("stars");
                String rating = rs.getString("rating");
                // Create a JsonObject based on the data we retrieve from rs
               // System.out.println("Title:" + movieTitle + " Year: " + movieYear + " id: "
               // + movieId + " Director: " + movieDirector + " Genres:" + genres);
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("movie_title", movieTitle);
                jsonObject.addProperty("movie_year", movieYear);
                jsonObject.addProperty("movie_id", movieId);
                jsonObject.addProperty("movie_director", movieDirector);
                jsonObject.addProperty("movie_genres", genres);
                jsonObject.addProperty("star_name", stars);
                jsonObject.addProperty("movie_rating", rating);
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
