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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AutoCompleteServlet", urlPatterns = "/api/autocomplete")
public class AutoCompleteServlet extends HttpServlet {
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

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        response.setContentType("application/json"); // Response mime type

        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */

        String [] keywords = request.getParameter("query").split("\\s+");

        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < keywords.length; i++)
            keywords[i] = "+" + keywords[i] + "*";
        for (int i = 0; i < keywords.length; i++)
            System.out.println(keywords[i]);

        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT entry FROM ft " +
            "WHERE MATCH (entry) AGAINST ('" + keywords[0];
            for (int i = 1; i < keywords.length; i++)
                query = query + " " + keywords[i];
            query = query + "' IN BOOLEAN MODE) LIMIT 10";
            System.out.println(query);
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("entry");

                jsonArray.add(title);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("query", "Unit");
            jsonObject.add("suggestions", jsonArray);
            System.out.println(jsonObject.toString());
            response.getWriter().write(jsonObject.toString());
            response.setStatus(200);

        } catch (Exception e) {
            //System.out.println("Full text search POST error!");
            throw new RuntimeException(e);
        }
    }
}
