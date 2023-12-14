package org.example;
import org.example.EmployeePayroll;
import java.util.*;

public class Main{
    public static void main(String[] args) {

        EmployeePayrollService payrollService = EmployeePayrollService.getInstance();
        try {
            // UC-5: Retrieve employees by date range
            List<EmployeePayroll> employeesByDateRange = payrollService.retrieveEmployeesByDateRange("2020-01-01", "2021-04-06");
            System.out.println("\nEmployees who have joined in the date range:");
            for (EmployeePayroll employee : employeesByDateRange) {
                System.out.println(employee);
            }
            // UC-6: Perform stats using database functions
            payrollService.doEmployeeStatistics();
        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
    }

    }
