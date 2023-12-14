import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // JDBC URL, username, and password of MySQL server
        String url = "jdbc:mysql://localhost:3306/payroll_service";
        String user = "root";
        String password = "";
        String query = "SELECT * FROM employee_payroll";

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            Connection con = DriverManager.getConnection(url, user, password);

            // Create a Statement
            Statement st = con.createStatement();

            // Execute the query
            ResultSet rs = st.executeQuery(query);

            // Process the results
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }

            // Don't forget to close the resources
            rs.close();
            st.close();
            con.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
