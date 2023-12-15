package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeePayrollService {

    // Singleton instance
    private static EmployeePayrollService instance;

    // Cached PreparedStatement
    private static PreparedStatement preparedStatement;

    // Private constructor for Singleton
    private EmployeePayrollService() {
        // Initialize the database connection and PreparedStatement
        initializeDatabase();
    }

    // Method to get the singleton instance
    public static EmployeePayrollService getInstance() {
        if (instance == null) {
            instance = new EmployeePayrollService();
        }
        return instance;
    }

    // Initialize the database connection and PreparedStatement
    private void initializeDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            preparedStatement = connection.prepareStatement("SELECT * FROM employee_payroll WHERE name = ?");
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    // Retrieve employee details from the database
    public EmployeePayroll retrieveEmployeeDetails(String employeeName) throws EmployeePayrollException {
        try {
            // Set the name parameter in the PreparedStatement
            preparedStatement.setString(1, employeeName);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if employee exists
            if (!resultSet.next()) {
                throw new EmployeePayrollException("Employee not found: " + employeeName);
            }

            // Reuse the ResultSet to populate the EmployeePayroll object
            return  mapResultSetToEmployeePayroll(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Error retrieving employee payroll data", e);
        }
    }

    // UC-2: Retrieve Employee Payroll Data from Database
    public List<EmployeePayroll> retrieveEmployeePayrollData() throws EmployeePayrollException{
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_payroll");

            while (resultSet.next()) {
                EmployeePayroll employeePayroll = mapResultSetToEmployeePayroll(resultSet);
                employeePayrollList.add(employeePayroll);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Error retrieving Employee Payroll data", e);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return employeePayrollList;
    }

    // UC-3: Update Salary for Employee Merissa
    public void updateEmployeeSalary(String employeeName, double newSalary) throws EmployeePayrollException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");

            // Update salary in the database
            String updateQuery = "UPDATE employee_payroll SET basic_pay = ? WHERE name = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, newSalary);
            preparedStatement.setString(2, employeeName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new EmployeePayrollException("Employee not found: " + employeeName);
            }
            System.out.println("\nEmployee "+ employeeName+" salary updated successfully" );

           System.out.println(retrieveEmployeeDetails(employeeName));

        } catch (SQLException e) {
            throw new EmployeePayrollException("Error updating employee salary", e);
        } finally {
            // Close resources in the finally block
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // UC-5: Retrieve employees who have joined in a particular date range
    public List<EmployeePayroll> retrieveEmployeesByDateRange(String startDate, String endDate) throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            String query = "SELECT * FROM employee_payroll WHERE start BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EmployeePayroll employeePayroll = mapResultSetToEmployeePayroll(resultSet);
                employeePayrollList.add(employeePayroll);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Error retrieving employees by date range", e);
        }

        return employeePayrollList;
    }

    // UC-6: Ability to find sum, average, min, max and number of male and female employees
    public void doEmployeeStatistics() throws EmployeePayrollException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            String query = "SELECT gender, COUNT(id) as count, " +
                    "SUM(basic_pay) as sum_salary, AVG(basic_pay) as avg_salary, " +
                    "MIN(basic_pay) as min_salary, MAX(basic_pay) as max_salary " +
                    "FROM employee_payroll GROUP BY gender";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("\n Gender"+"\t"+"Number of Employees"+"\t"+"Total Salary"+"\t"+"Average Salary"+"\t"+"\"Minimum Salary"+"\t"+"Maximum Salary");

            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("count");
                double sumSalary = resultSet.getDouble("sum_salary");
                double avgSalary = resultSet.getDouble("avg_salary");
                double minSalary = resultSet.getDouble("min_salary");
                double maxSalary = resultSet.getDouble("max_salary");

                System.out.println(gender+"\t\t"+count+"\t\t"+sumSalary+"\t\t"+avgSalary+"\t\t"+minSalary+"\t\t"+maxSalary);
            }

            connection.close();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Error performing statistics", e);
        }
    }

    //display payroll details table in console
   public void display_payroll_details(){
       try {
           // Assuming you have a connection to your database
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");

           // Assuming you have a Statement to execute the query
           Statement statement = connection.createStatement();

           // Execute the query to fetch all records from payroll_service table
           ResultSet resultSet = statement.executeQuery("SELECT * FROM payroll_details");

           // Iterate through the result set and display each record
           System.out.println("\npayroll_details db is as follows: ");
           while (resultSet.next()) {
               // Assuming you have appropriate column names, update them accordingly
               int column1 = resultSet.getInt(1);
               double column2 = resultSet.getDouble(2);
               double column3 = resultSet.getDouble(3);
               double column4 = resultSet.getDouble(4);
               double column5 = resultSet.getDouble(5);


               // Display the retrieved data
               System.out.println(column1+"\t"+column2+"\t"+column3+"\t"+column4+"\t"+column5);
           }

           // Close resources
           connection.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

    // UC-7: Ability to add a new employee to the payroll with transactions
    public void addEmployeeToPayroll(EmployeePayroll employeePayroll) throws EmployeePayrollException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            connection.setAutoCommit(false);

            insertIntoEmployeePayroll(employeePayroll, connection);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new EmployeePayrollException("Error adding employee to payroll with transaction", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertIntoEmployeePayroll(EmployeePayroll employeePayroll, Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO employee_payroll (name, phone_number, address, department, " +
                "basic_pay, deductions, taxable_pay, tax, net_pay, start, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        preparedStatement = connection.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, employeePayroll.getName());
        preparedStatement.setString(2, employeePayroll.getPhoneNumber());
        preparedStatement.setString(3, employeePayroll.getAddress());
        preparedStatement.setString(4, employeePayroll.getDepartment());
        preparedStatement.setInt(5, employeePayroll.getBasicPay());
        preparedStatement.setDouble(6, employeePayroll.getDeductions());
        preparedStatement.setDouble(7, employeePayroll.getTaxablePay());
        preparedStatement.setDouble(8, employeePayroll.getTax());
        preparedStatement.setDouble(9, employeePayroll.getNetPay());
        preparedStatement.setString(10, employeePayroll.getStartDate());
        preparedStatement.setString(11, employeePayroll.getGender());

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected == 0) {
            throw new SQLException("Adding employee to payroll failed, no rows affected.");
        }

        // Retrieve the auto-generated employee ID
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            employeePayroll.setId(generatedKeys.getInt(1));
        } else {
            throw new SQLException("Failed to retrieve auto-generated ID for employee");
        }

        preparedStatement.close();
    }

    // UC-8: Add Payroll Details when a new Employee is added
    public void addEmployeeToPayrollDetails(EmployeePayroll employeePayroll) throws EmployeePayrollException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            connection.setAutoCommit(false); // Turn off auto-commit

            // Insert into employee_payroll table
            insertIntoEmployeePayroll(employeePayroll,connection);

            // Insert into payroll_details table
            insertIntoPayrollDetails(connection, employeePayroll);

            // Commit the transaction if both inserts are successful
            connection.commit();

        } catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new EmployeePayrollException("Error adding employee to payroll", e);
        } finally {
            // Set back to auto-commit mode and close the connection
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //inserting all stats in payroll_details table
    private void insertIntoPayrollDetails(Connection connection, EmployeePayroll employeePayroll) throws SQLException {
        // Calculate derived fields
        double deductions = employeePayroll.getBasicPay() * 0.2;
        double taxablePay = employeePayroll.getBasicPay() - deductions;
        double tax = taxablePay * 0.1;
        double netPay = employeePayroll.getBasicPay() - tax;

        String insertQuery = "INSERT INTO payroll_details (employee_id, deductions, taxable_pay, tax, net_pay) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        // Set parameters for payroll_details table
        preparedStatement.setInt(1, employeePayroll.getId());
        preparedStatement.setDouble(2, deductions);
        preparedStatement.setDouble(3, taxablePay);
        preparedStatement.setDouble(4, tax);
        preparedStatement.setDouble(5, netPay);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    // UC-12: Remove Employee from the Payroll
    public void removeEmployeeFromPayroll(String employeeName) throws EmployeePayrollException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");

            // Set is_active to false for the specified employee
            String updateQuery = "UPDATE employee_payroll SET is_active = FALSE WHERE name = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, employeeName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new EmployeePayrollException("Employee not found: " + employeeName);
            }
            System.out.println("\nEmployee " + employeeName + " removed from payroll successfully");
        } catch (SQLException e) {
            throw new EmployeePayrollException("Error removing employee from payroll", e);
        } finally {
            // Close resources in the finally block
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // UC-12: Retrieve only active employees
    public void retrieveActiveEmployeePayrollData() throws EmployeePayrollException {
        List<EmployeePayroll> activeEmployeePayrollList = new ArrayList<>();
        try {
            // Assuming you have a connection to your database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");

            // Assuming you have a Statement to execute the query
            Statement statement = connection.createStatement();

            // Execute the query to fetch all records from payroll_service table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_payroll where is_active=1");
            // Iterate through the result set and display each record
            System.out.println("\nActive employees payroll data  is as follows: ");
            System.out.println("Id"+"\t"+"Name");
            while (resultSet.next()) {
                // Assuming you have appropriate column names, update them accordingly
                int column1 = resultSet.getInt(1);
                String column2 = resultSet.getString(2);

                // Display the retrieved data
                System.out.println(column1+"\t"+column2);
            }

            // Close resources
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private EmployeePayroll mapResultSetToEmployeePayroll(ResultSet resultSet) throws SQLException {
        EmployeePayroll employeePayroll = new EmployeePayroll();
        employeePayroll.setId(resultSet.getInt("id"));
        employeePayroll.setName(resultSet.getString("name"));
        employeePayroll.setPhoneNumber(resultSet.getString("phone_number"));
        employeePayroll.setAddress(resultSet.getString("address"));
        employeePayroll.setDepartment(resultSet.getString("department"));
        employeePayroll.setBasicPay(resultSet.getInt("basic_pay"));
        employeePayroll.setDeductions(resultSet.getDouble("deductions"));
        employeePayroll.setTaxablePay(resultSet.getDouble("taxable_pay"));
        employeePayroll.setTax(resultSet.getDouble("tax"));
        employeePayroll.setNetPay(resultSet.getDouble("net_pay"));
        employeePayroll.setStartDate(resultSet.getString("start"));
        employeePayroll.setGender(resultSet.getString("gender"));
        return employeePayroll;
    }
}
