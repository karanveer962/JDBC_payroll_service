package org.example;
import org.example.EmployeePayroll;
import java.util.*;

public class Main{
    public static void main(String[] args) {

        EmployeePayrollService payrollService = new EmployeePayrollService();

        try {
            List<EmployeePayroll> payrollData = payrollService.retrieveEmployeePayrollData();
            for (EmployeePayroll it : payrollData)
                System.out.println(it);
        }
        catch (EmployeePayrollException e) {
            e.printStackTrace();
        }

        try {
            // Update the salary for Employee Merissa
            payrollService.updateEmployeeSalary("Merissa", 3000000.00);

        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
    }

    }
