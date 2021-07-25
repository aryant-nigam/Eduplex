package com.example.Eduplex;

public class TimetableClass  {
    private String subjectName;
    private String subjectTiming;
    private String subjectTeacher;
    public TimetableClass(){}
    public TimetableClass(String subjectName, String subjectTiming, String subjectTeacher) {

        this.subjectName = subjectName;
        this.subjectTiming = subjectTiming;
        this.subjectTeacher = subjectTeacher;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTiming() {
        return subjectTiming;
    }

    public void setSubjectTiming(String subjectTiming) {
        this.subjectTiming = subjectTiming;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }
}
