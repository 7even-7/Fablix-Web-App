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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@WebServlet(name = "GetMetadataServlet", urlPatterns = "/api/get_metadata")
public class GetMetadataServlet extends HttpServlet {
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
        /* This example only allows username/password to be test/test
        /  in the real project, you should talk to the database to verify username/password
        */
        // Set the content type of the response to JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JsonObject responseJsonObject = new JsonObject();
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT table_name, column_name, data_type, character_maximum_length " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = 'moviedb'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            //System.out.println(resultSet.next());

            JsonObject metadata = new JsonObject();
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                String columnName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                int maxLength = rs.getInt("character_maximum_length");

                // Check if we already have a JSON object for this table
                JsonObject table = metadata.getAsJsonObject(tableName);
                if (table == null) {
                    // If not, create a new JSON object for the table
                    table = new JsonObject();
                    table.addProperty("table", tableName);
                    metadata.add(tableName, table);
                }

                // Add the column information to the table JSON object
                JsonObject column = new JsonObject();
                column.addProperty("column", columnName);
                column.addProperty("type", dataType);
                column.addProperty("maxLength", maxLength);
                table.add(columnName, column);
            }

            ((PrintWriter) out).write(metadata.toString());

            rs.close();
            statement.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
