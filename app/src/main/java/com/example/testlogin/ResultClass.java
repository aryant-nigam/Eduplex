package com.example.testlogin;

import android.widget.ImageView;

public class ResultClass {
    private String subjectVal;
    private double scoreVal;
    private double percentageVal;

    public ResultClass(String subjectVal, double scoreVal, double percentageVal) {
        this.subjectVal = subjectVal;
        this.scoreVal = scoreVal;
        this.percentageVal = percentageVal;
    }

    public String getSubjectVal() { return subjectVal; }

    public void setSubjectVal(String subjectVal) { this.subjectVal = subjectVal; }

    public double getScoreVal() { return scoreVal; }

    public void setScoreVal(double scoreVal) { this.scoreVal = scoreVal; }

    public double getPercentageVal() { return percentageVal; }

    public void setPercentageVal(double percentageVal) { this.percentageVal = percentageVal; }
}
