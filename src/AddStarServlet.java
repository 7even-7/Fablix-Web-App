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
@WebServlet(name = "AddStarServlet", urlPatterns = "/api/add_star")
public class AddStarServlet extends HttpServlet {
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
        Random rand = new Random();
        String id = "nm" + rand.nextInt(9999999);
        String username = request.getParameter("name");
        String birthYear = request.getParameter("birthYear");
        System.out.println(id + username + birthYear);

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        JsonObject responseJsonObject = new JsonObject();
        try (Connection conn = dataSource.getConnection()) {
            String query = "INSERT INTO stars (id, name, birthYear) VALUES(?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, username);
            statement.setInt(3, Integer.parseInt(birthYear));
            System.out.println(statement);
            statement.executeUpdate();
            System.out.println("Star inserted successfully");
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "Star inserted successfully");
            response.getWriter().write(responseJsonObject.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
