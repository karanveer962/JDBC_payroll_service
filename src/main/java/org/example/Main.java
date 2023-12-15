package org.example;
import org.example.EmployeePayroll;
import java.util.*;

public class Main{
    public static void main(String[] args) {

        EmployeePayrollService payrollService = EmployeePayrollService.getInstance();
        try {
            // UC-8: Add a new employee to the payroll using transactions
            EmployeePayroll newEmployee = new EmployeePayroll();
            newEmployee.setName("Sidhi");
            newEmployee.setPhoneNumber("7566564985");
            newEmployee.setAddress("Indore near Jai Sweets House");
            newEmployee.setDepartment("CSE");
            newEmployee.setBasicPay(90000);
            newEmployee.setDeductions(1000.0);
            newEmployee.setTaxablePay(45000.0);
            newEmployee.setTax(500.0);
            newEmployee.setNetPay(44500.0);
            newEmployee.setStartDate("2022-01-01");
            newEmployee.setGender("F");

            payrollService.addEmployeeToPayrollDetails(newEmployee);
            System.out.println("New employee added successfully and record updated as well in payroll_details table: ");

          for(EmployeePayroll it:payrollService.retrieveEmployeePayrollData()){
              System.out.println(it);
          }
          payrollService.display_payroll_details();
        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
    }

    }
