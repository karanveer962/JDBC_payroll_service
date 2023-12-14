package org.example;
import org.example.EmployeePayroll;
import java.util.*;

public class Main{
    public static void main(String[] args) {

        EmployeePayrollService payrollService = EmployeePayrollService.getInstance();

        try {

            List<EmployeePayroll> payrollData = payrollService.retrieveEmployeePayrollData();
            for (EmployeePayroll it : payrollData)
                System.out.println(it);


            // Retrieve payroll data by name
            EmployeePayroll employeePayroll = payrollService.retrieveEmployeeDetails("Merissa");
            System.out.println("Employee Payroll Details:\n" + employeePayroll);

            // Update salary for Employee Terisa and print updated details
            payrollService.updateEmployeeSalary("Merissa", 3500000.00);

        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
    }

    }
