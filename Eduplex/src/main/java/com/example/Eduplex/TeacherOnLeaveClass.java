package com.example.Eduplex;

public class TeacherOnLeaveClass {
    private String teacherOnLeaveTid;
    private String teacherOnLeaveMessage;
    private String leaveDate;

    public TeacherOnLeaveClass(String teacherOnLeaveTid,  String teacherOnLeaveMessage,String leaveDate) {
        this.teacherOnLeaveTid = teacherOnLeaveTid;
        this.teacherOnLeaveMessage = teacherOnLeaveMessage;
        this.leaveDate=leaveDate;
    }
    public TeacherOnLeaveClass(){}

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getTeacherOnLeaveTid() { return teacherOnLeaveTid; }

    public void setTeacherOnLeaveTid(String teacherOnLeaveTid) { this.teacherOnLeaveTid = teacherOnLeaveTid; }


    public String getTeacherOnLeaveMessage() { return teacherOnLeaveMessage; }

    public void setTeacherOnLeaveMessage(String teacherOnLeaveMessage) { this.teacherOnLeaveMessage = teacherOnLeaveMessage; }
}
