import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.servlet.ServletConfig;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.*;

@WebServlet(name = "FullTextSearchServlet", urlPatterns = "/api/full_text_search")
public class FullTextSearchServlet extends HttpServlet {
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void init(ServletConfig config) {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/moviedb");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println("In GET request:FullTextSearchServlet");
//        System.out.println("the keyword is: " + request.getParameter("query"));
//        System.out.println("the keyword is: " + request.getParameter("pageNum"));
        long startTS = System.nanoTime();
        String contextPath = request.getServletContext().getRealPath("/");
        System.out.println("The path is " + contextPath);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        response.setContentType("application/json"); // Response mime type
        String search = request.getParameter("query");
        int pageNumber = 0;

        String [] keywords = search.split("\\s+");
        //System.out.println(keywords.toString());
        for (int i = 0; i < keywords.length; i++)
            keywords[i] = "+" + keywords[i] + "*";
/*        for (int i = 0; i < keywords.length; i++)
            System.out.println(keywords[i]);*/
        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        int itemsPerPage = 10;
        JsonArray jsonArray = new JsonArray();

        try (Connection conn = dataSource.getConnection()) {
            //System.out.println(request.getServletContext().getRealPath("/"));
            String query = "SELECT MAX(movies.id) as id, movies.title, MAX(movies.year) AS year, MAX(movies.director) AS director, MAX(ratings.rating) AS rating, GROUP_CONCAT(DISTINCT stars.name) AS star_names, GROUP_CONCAT(DISTINCT genres.name) AS genre_names " +
                    "FROM ft " +
                    "INNER JOIN movies ON ft.entry = movies.title " +
                    "INNER JOIN stars_in_movies ON movies.id = stars_in_movies.movieId " +
                    "INNER JOIN stars ON stars_in_movies.starId = stars.id " +
                    "INNER JOIN genres_in_movies ON movies.id = genres_in_movies.movieId " +
                    "INNER JOIN genres ON genres_in_movies.genreId = genres.id " +
                    "INNER JOIN ratings ON movies.id = ratings.movieId " +
                    "WHERE MATCH (entry) AGAINST ('" + keywords[0];
            ;

            for (int i = 1; i < keywords.length; i++)
                query = query + " " + keywords[i];
            if (request.getParameter("pageNum") == null){
                query = query + "' IN BOOLEAN MODE) " +
                        "GROUP BY movies.title LIMIT 100";
            }
            else{
                pageNumber = Integer.parseInt(request.getParameter("pageNum"));
                query = query + "' IN BOOLEAN MODE) " +
                        "GROUP BY movies.title " +
                        "LIMIT " + itemsPerPage + " " + // Number of items to retrieve per page
                        "OFFSET " + ((pageNumber- 1) * itemsPerPage);
            }
            // Calculate the offset

//            System.out.println(query);
            PreparedStatement statement = conn.prepareStatement(query);
//            System.out.println("Statement prepared successfully");
            long startTJ = System.nanoTime();

            ResultSet resultSet = statement.executeQuery();

            long endTQ = System.nanoTime();
            long elapsedTQ = endTQ - startTJ;

            String filePath = contextPath + "TQLog.txt";
            File file = new File(filePath);
            //System.out.println(filePath);
            if (!file.exists()){
                file.createNewFile();
            }


            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Query Execution Time: " + String.valueOf(elapsedTQ) + "\n");
            bw.close();

//           System.out.println("Statement executed properly");
            //System.out.println(resultSet.next());
            while (resultSet.next()) {
                String title = resultSet.getString("title");
//                System.out.println(title);
                String year = resultSet.getString("year");
//                System.out.println(year);
                String director = resultSet.getString("director");
//                System.out.println(director);
                String starname = resultSet.getString("star_names");
//                System.out.println(starname);
                String genres = resultSet.getString("genre_names");
                String rating = resultSet.getString("rating");
//                System.out.println(rating);
                String id = resultSet.getString("id");
                //System.out.println("Title " + title + " Year: " + year + " Director: " + director + "Name: " + name);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("movieTitle", title);
                jsonObject.addProperty("movieYear", year);
                jsonObject.addProperty("movieDirector", director);
                jsonObject.addProperty("movieStar", starname);
                jsonObject.addProperty("genres", genres);
                jsonObject.addProperty("movieId", id);
                jsonObject.addProperty("movieRating", rating);


                jsonArray.add(jsonObject);
            }
            //System.out.println("Database information retrieval success!");
            long endTJ = System.nanoTime();
            long elapsedTJ = endTJ - startTJ; // elapsed time in nano seconds. Note: print the values in nanoseconds
            filePath = contextPath + "TJLog.txt";
            file = new File(filePath);
            //System.out.println(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write("JDBC Execution Time: " + String.valueOf(elapsedTJ) + "\n");
            bw.close();
            //System.out.println("File writing success!");

            response.getWriter().write(jsonArray.toString());
            response.setStatus(200);

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        long endTS = System.nanoTime();
        long elapsedTS = endTS - startTS; // elapsed time in nano seconds. Note: print the values in nanoseconds

        String path = contextPath + "TSLog.txt";

        //System.out.println(path);
        File file = new File(path);
        if (!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Search Servlet Total Execution Time: " + String.valueOf(elapsedTS) + "\n");
        bw.close();
    }
}
