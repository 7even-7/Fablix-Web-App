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
@WebServlet(name = "EmployeeLoginServlet", urlPatterns = "/api/employee_login")
public class EmployeeLoginServlet extends HttpServlet {
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
        String username = request.getParameter("username");
        String userpassword = request.getParameter("password");

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        JsonObject responseJsonObject = new JsonObject();
        System.out.println(username + " " + userpassword);
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT email, password FROM employees where email = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            //System.out.println(resultSet.next());

            if (resultSet.next()) {
                System.out.println("Test");
                String userEmail = resultSet.getString("email");
                String userPass = resultSet.getString("password");
                if (userpassword.equals(userPass)){
                    request.getSession().setAttribute("user", new User(username));
                    System.out.println("Successfully set session as employee!");
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                    // Process the count value here
                }
                if (!userpassword.equals(userPass)) {
                    responseJsonObject.addProperty("message", "Incorrect password!");
                }

            }
            else {
                // Login fail
                System.out.println("Failed");
                responseJsonObject.addProperty("status", "fail");
                // Log to localhost log
                request.getServletContext().log("Login failed");
                // sample error messages. in practice, it is not a good idea to tell user which one is incorrect/not exist.

                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");

            }

            response.getWriter().write(responseJsonObject.toString());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

