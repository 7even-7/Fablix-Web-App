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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
@WebServlet(name = "AddMovieServlet", urlPatterns = "/api/add_movie")
public class AddMovieServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        int year = Integer.parseInt(request.getParameter("year"));
        String director = request.getParameter("director");
        String star_name = request.getParameter("star_name");
        String genre = request.getParameter("genre");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        JsonObject responseJsonObject = new JsonObject();
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT * FROM movies WHERE title=? AND year=? AND director=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, title);
            statement.setInt(2, year);
            statement.setString(3, director);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                responseJsonObject.addProperty("message", "Movie already exists in database!");
                response.getWriter().write(responseJsonObject.toString());
                return;
            }

            query = "CALL add_movie(?,?,?,?,?,?)";
            statement = conn.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, title);
            statement.setInt(3, year);
            statement.setString(4, director);
            statement.setString(5, star_name);
            statement.setString(6, genre);
            System.out.println(statement);
            statement.executeUpdate();
            System.out.println("Movie inserted successfully");
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "Star inserted successfully");
            response.getWriter().write(responseJsonObject.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
