package org.example;
import org.example.EmployeePayroll;
import java.util.*;

public class Main{
    public static void main(String[] args) {

        EmployeePayrollService payrollService = new EmployeePayrollService();


        List<EmployeePayroll> payrollData = payrollService.retrieveEmployeePayrollData();
         for(EmployeePayroll it:payrollData)
             System.out.println(it);

    }
}
