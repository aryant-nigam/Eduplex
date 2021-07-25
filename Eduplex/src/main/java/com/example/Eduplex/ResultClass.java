package com.example.Eduplex;

public class ResultClass {
    private String subjectVal;
    private double scoreVal;
    private double maximumMarks;
    private String testType;
    public ResultClass(){}
    public ResultClass(String subjectVal, double scoreVal, double maximumMarks,String testType) {
        this.subjectVal = subjectVal;
        this.scoreVal = scoreVal;
        this.maximumMarks = maximumMarks;
        this.testType=testType;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getSubjectVal() { return subjectVal; }

    public void setSubjectVal(String subjectVal) { this.subjectVal = subjectVal; }

    public double getScoreVal() { return scoreVal; }

    public void setScoreVal(double scoreVal) { this.scoreVal = scoreVal; }

    public double getMaximumMarks() { return maximumMarks; }

    public void setMaximumMarks(double maximumMarks) { this.maximumMarks = maximumMarks; }
}
