package com.example.testlogin;

public class ResultClass {
    private String subjectVal;
    private double scoreVal;
    private double maximumMarks;
    public ResultClass(){}
    public ResultClass(String subjectVal, double scoreVal, double maximumMarks) {
        this.subjectVal = subjectVal;
        this.scoreVal = scoreVal;
        this.maximumMarks = maximumMarks;
    }

    public String getSubjectVal() { return subjectVal; }

    public void setSubjectVal(String subjectVal) { this.subjectVal = subjectVal; }

    public double getScoreVal() { return scoreVal; }

    public void setScoreVal(double scoreVal) { this.scoreVal = scoreVal; }

    public double getMaximumMarks() { return maximumMarks; }

    public void setMaximumMarks(double maximumMarks) { this.maximumMarks = maximumMarks; }
}
