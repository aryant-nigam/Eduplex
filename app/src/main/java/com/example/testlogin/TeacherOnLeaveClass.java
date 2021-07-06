package com.example.testlogin;

public class TeacherOnLeaveClass {
    private String teacherOnLeaveTid;
    private String teacherOnLeaveTName;
    private String teacherOnLeaveMessage;

    public TeacherOnLeaveClass(String teacherOnLeaveTid, String teacherOnLeaveTName, String teacherOnLeaveMessage) {
        this.teacherOnLeaveTid = teacherOnLeaveTid;
        this.teacherOnLeaveTName = teacherOnLeaveTName;
        this.teacherOnLeaveMessage = teacherOnLeaveMessage;
    }

    public String getTeacherOnLeaveTid() { return teacherOnLeaveTid; }

    public void setTeacherOnLeaveTid(String teacherOnLeaveTid) { this.teacherOnLeaveTid = teacherOnLeaveTid; }

    public String getTeacherOnLeaveTName() { return teacherOnLeaveTName; }

    public void setTeacherOnLeaveTName(String teacherOnLeaveTName) { this.teacherOnLeaveTName = teacherOnLeaveTName; }

    public String getTeacherOnLeaveMessage() { return teacherOnLeaveMessage; }

    public void setTeacherOnLeaveMessage(String teacherOnLeaveMessage) { this.teacherOnLeaveMessage = teacherOnLeaveMessage; }
}
