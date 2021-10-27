package com.example.Eduplex;

public class ComplaintToken {
    String registrationNumber;
    Boolean isResolved;
    public ComplaintToken(){}
    public ComplaintToken(String registrationNumber, Boolean isResolved) {
        this.registrationNumber = registrationNumber;
        this.isResolved = isResolved;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Boolean getIsResolved() {
        return isResolved;
    }

    public void setNumberOfComplaints(Boolean isResolved) {
        this.isResolved = isResolved;
    }
}
