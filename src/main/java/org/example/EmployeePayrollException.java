package org.example;

public class EmployeePayrollException extends Exception {


    public EmployeePayrollException() {
        super();
    }

    public EmployeePayrollException(String message) {
        super(message);
    }

    public EmployeePayrollException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeePayrollException(Throwable cause) {
        super(cause);
    }

}
