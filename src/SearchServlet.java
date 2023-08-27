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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


// Declaring a WebServlet called StarsServlet, which maps to url "/api/stars"
@WebServlet(name = "SearchServlet", urlPatterns = "/api/search")
public class SearchServlet extends HttpServlet {
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
        String field = request.getParameter("field");
        System.out.println(field);
        String[] data = field.split("/");
        String movieTitle = data[0];
        String movieYear = data[1].substring(5);
        String movieDirector = data[2].substring(9);
        String movieStar = data[3].substring(5);
        System.out.println("Title: " + movieTitle + " Year: " + movieYear + " Director: "
                + movieDirector + " Star: " + movieStar);
//        System.out.println(request.getParameterMap());
        // The log message can be found in localhost log
        //request.getServletContext().log("getting id: " + input);
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
        // Get a connection from dataSource and let resource manager close the connection after usage.

        try (Connection conn = dataSource.getConnection()) {
            // Get a connection from dataSource
            System.out.println("WOKRED");
            // Construct a query with parameter represented by "?"
            String singleQuery = "SELECT DISTINCT movies.title, movies.year, movies.director, stars.name " +
                    "FROM movies " +
                    "INNER JOIN stars_in_movies ON movies.id = stars_in_movies.movieId " +
                    "INNER JOIN stars ON stars_in_movies.starId = stars.id " +
                    "WHERE ";
           // System.out.println(singleQuery);
            // Declare our statement



            if (movieTitle.equals("")) {
                movieTitle = "NONEXIST";
            }
            if (movieDirector.equals("")) {
                movieDirector = "NONEXIST";
            }
            if (movieStar.equals("")) {
                movieStar = "NONEXIST";
            }
            if (movieYear.equals("")) {
                movieYear = "NONEXIST";
            }

            String[] conditions = {"movies.title", "movies.director", "stars.name", "movies.year"};
            String[] values = {movieTitle.toLowerCase(), movieDirector.toLowerCase(), movieStar.toLowerCase(),
                    movieYear.toLowerCase()};

           // System.out.println(singleQuery);

            int count = 0;
            int first = 0;
            //loop to process the query
            while (true){
                if (count == 4) break;

                if (first == 0 && !values[count].equals("nonexist")){
                    if (count != 3){
                        singleQuery = singleQuery +  "lower(" + conditions[count] + ")" + " like " + "'" + "%" + values[count] + "%"  + "'";
                    }
                    else{
                        singleQuery = singleQuery + conditions[count] + " = " + values[count] + " ";
                    }
                    first++;
                }
                else if (!values[count].equals("nonexist")) {
                    if (count != 3){
                        singleQuery = singleQuery + " and " + "lower(" + conditions[count] + ")" + " like " + "'" + "%" + values[count] + "%" +"' ";
                    }
                    else{
                        singleQuery = singleQuery + " and " + conditions[count] + " = " + values[count] + " ";
                    }
                }
                count++;
            }
        singleQuery += "ORDER BY movies.title ASC";
        System.out.println(singleQuery);
        PreparedStatement statement = conn.prepareStatement(singleQuery);
        ResultSet rs = statement.executeQuery();
        // Set the parameter represented by "?" in the query to the id we get from url,
        // num 1 indicates the first "?" in the query
        //statement.setString(1, "%" + input + "%");

        // Perform the query


        JsonArray jsonArray = new JsonArray();

        // Iterate through each row of rs
        while (rs.next()) {
            String title = rs.getString("title");
            String year = rs.getString("year");
            String director = rs.getString("director");
            String starname = rs.getString("name");

            //System.out.println("Title " + title + " Year: " + year + " Director: " + director + "Name: " + name);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("movieTitle", title);
            jsonObject.addProperty("movieYear", year);
            jsonObject.addProperty("movieDirector", director);
            jsonObject.addProperty("movieStar", starname);


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
