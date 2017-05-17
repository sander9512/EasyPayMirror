package com.avans.easypay.DomainModel;

import java.io.Serializable;

/**
 * Created by Felix on 10-5-2017.
 */

public class Customer implements Serializable {
    private int customerId;
    private String username, password, email, firstname, lastname, bankAccountNumber;
    private Balance balance;
    private String timeLog;

    public Customer(int customerId, String username, String password, String email, String firstname,
                    String lastname, String bankAccountNumber, Balance balance, String timeLog) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bankAccountNumber = bankAccountNumber;
        this.balance = balance;
        this.timeLog = timeLog;
    }

    public Customer(int customerId, String username, String password, String email, String firstname,
                    String lastname, String bankAccountNumber) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bankAccountNumber = bankAccountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public String getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(String timeLog) {
        this.timeLog = timeLog;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", balance=" + balance +
                ", timeLog=" + timeLog +
                '}';
    }
}
