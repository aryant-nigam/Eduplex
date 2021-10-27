package com.example.Eduplex;

public class ClassTeacherAssociativityClass {
    private String teacherName;
    private String classTeacherOf;

    public ClassTeacherAssociativityClass(String teacherName, String classTeacherOf) {
        this.teacherName = teacherName;
        this.classTeacherOf = classTeacherOf;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassTeacherOf() {
        return classTeacherOf;
    }

    public void setClassTeacherOf(String classTeacherOf) {
        this.classTeacherOf = classTeacherOf;
    }

}
