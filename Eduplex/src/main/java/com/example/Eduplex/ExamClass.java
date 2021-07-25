package com.example.Eduplex;

public class ExamClass {
    private String subjectExam;
    private String date;
    private String time;
    private String duration;

    public ExamClass(String subjectExam, String date, String time, String duration) {
        this.subjectExam = subjectExam;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }
    public ExamClass(){}
    public String getSubjectExam() { return subjectExam; }

    public void setSubjectExam(String subjectExam) { this.subjectExam = subjectExam; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }
}
