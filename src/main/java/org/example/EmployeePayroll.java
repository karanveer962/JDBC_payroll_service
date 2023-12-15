package org.example;

import java.util.Date;
import java.lang.String;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EmployeePayroll {

    private int id;
    private String name;
    private String phoneNumber;
    private String address;
    private String department;
    private int basicPay;
    private double deductions;
    private double taxablePay;
    private double tax;
    private double netPay;
    private String startDate;
    private char gender;


    public EmployeePayroll() {
        // Default constructor
    }

    public EmployeePayroll(int id, String name, String phoneNumber, String address, String department,
                           int basicPay, double deductions, double taxablePay, double tax,
                           double netPay, String startDate, char gender) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.department = department;
        this.basicPay = basicPay;
        this.deductions = deductions;
        this.taxablePay = taxablePay;
        this.tax = tax;
        this.netPay = netPay;
        this.startDate = startDate;
        this.gender = gender;
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address){
        this.address=address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setBasicPay(int bp){
        this.basicPay=bp;
    }
    public int getBasicPay(){
        return this.basicPay;
    }
    public void setPhoneNumber(String num){
        this.phoneNumber=num;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setTax(Double t){
        this.tax=t;
    }
    public Double getTax(){
       return this.tax;
    }

    public void setTaxablePay(Double t){
      this.taxablePay=t;
    }
    public Double getTaxablePay(){
        return this.taxablePay;
    }
    public void setDepartment(String department){
        this.department=department;
    }
    public String getDepartment(){
        return this.department;
    }
    public void setGender(String gender){
        if(gender!=null)
          this.gender=gender.charAt(0);

    }
    public String getGender(){
       return String.valueOf(this.gender);
    }

    public void setNetPay(Double t){
        this.netPay=t;
    }
    public Double getNetPay(){
        return this.netPay;
    }
    public void setDeductions(Double t){
        this.deductions=t;
    }
    public Double getDeductions(){
      return  this.deductions;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", department='" + department + '\'' +
                ", basicPay=" + basicPay +
                ", deductions=" + deductions +
                ", taxablePay=" + taxablePay +
                ", tax=" + tax +
                ", netPay=" + netPay +
                ", start=" + startDate +
                ", gender='" + gender + '\'' +
                '}';
    }
}
