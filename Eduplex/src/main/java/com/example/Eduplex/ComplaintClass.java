package com.example.Eduplex;

import java.io.Serializable;

public class ComplaintClass implements Serializable {
    private String complaintType;
    private String complaintDescription;
    private String lodgedBy;
    private String classOfStudent;
    private String registrationNoOfStudent;
    private String status;
    private String response;
    private String revealAnonimity;
    private String token;

    public ComplaintClass(){}
    public ComplaintClass(String complaintType, String complaintDescription, String lodgedBy, String classOfStudent, String registrationNoOfStudent,String status,String response,String revealAnonimity,String token) {
        this.complaintType = complaintType;
        this.complaintDescription = complaintDescription;
        this.lodgedBy = lodgedBy;
        this.classOfStudent = classOfStudent;
        this.registrationNoOfStudent = registrationNoOfStudent;
        this.status=status;
        this.response=response;
        this.revealAnonimity=revealAnonimity;
        this.token=token;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Getters - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public String getComplaintType() { return complaintType; }

    public String getComplaintDescription() {return complaintDescription; }

    public String getLodgedBy() { return lodgedBy; }

    public String getClassOfStudent() { return classOfStudent; }

    public String getRegistrationNoOfStudent() {return registrationNoOfStudent; }

    public String getStatus() { return status; }

    public String getResponse() { return response; }

    public String getRevealAnonimity() {return revealAnonimity; }

    public String getToken() { return token; }


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - Setters - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void setComplaintType(String complaintType) {this.complaintType = complaintType; }

    public void setComplaintDescription(String complaintDescription) {this.complaintDescription = complaintDescription; }

    public void setLodgedBy(String lodgedBy) { this.lodgedBy = lodgedBy; }

    public void setClassOfStudent(String classOfStudent) { this.classOfStudent = classOfStudent; }

    public void setRegistrationNoOfStudent(String registrationNoOfStudent) {this.registrationNoOfStudent = registrationNoOfStudent; }

    public void setStatus(String status) { this.status = status; }

    public void setResponse(String response) {this.response = response; }

    public void setRevealAnonimity(String revealAnonimity) {this.revealAnonimity = revealAnonimity; }

    public void setToken(String token) { this.token = token;}

}
