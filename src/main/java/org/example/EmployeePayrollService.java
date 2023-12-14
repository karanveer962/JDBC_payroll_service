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

    // UC-2: Retrieve Employee Payroll Data from Database
    public List<EmployeePayroll> retrieveEmployeePayrollData() {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();


        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_service", "root", "waheguruJI");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_payroll");

            while (resultSet.next()) {
                // Populate EmployeePayroll object and add to the list
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
                // Add other attributes...

                employeePayrollList.add(employeePayroll);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
//            throw new EmployeePayrollException("Error retrieving Employee Payroll data", e);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return employeePayrollList;
    }

    // Other methods...

}
