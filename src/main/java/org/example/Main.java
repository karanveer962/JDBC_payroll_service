package org.example;
import org.example.EmployeePayroll;
import java.util.*;

public class Main{
    public static void main(String[] args) {

        EmployeePayrollService payrollService = EmployeePayrollService.getInstance();
        try {
            //remove EmployeePayroll Object from the List and set is _active to false
            payrollService.removeEmployeeFromPayroll("Sidhi");
            //retrieve only active employees
            payrollService.retrieveActiveEmployeePayrollData();

        } catch (EmployeePayrollException e) {
            e.printStackTrace();
        }
    }

    }
