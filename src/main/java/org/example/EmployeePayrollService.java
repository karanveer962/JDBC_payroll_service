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

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Error performing statistics", e);
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
