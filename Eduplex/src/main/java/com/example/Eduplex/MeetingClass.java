package com.example.Eduplex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MeetingClass {
    private String meetingId;
    private String meetingDate;
    private String meetingTime;
    private String fromClass,toClass;
    private  String meetingDuration;

    public MeetingClass(){}
    public MeetingClass(String meetingId, String meetingDate, String meetingTime,String fromClass,String toClass,String meetingDuration) {
        this.meetingId = meetingId;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.fromClass=fromClass;
        this.toClass=toClass;
        this.meetingDuration=meetingDuration;

    }

    //-------------------------------------------GETTER---------------------------------------
    public String getMeetingId() { return meetingId; }

    public String getMeetingDate() { return meetingDate; }

    public String getMeetingTime() { return meetingTime; }

    public String getFromClass() { return fromClass;}

    public String getToClass() { return toClass; }

    public String getMeetingDuration() { return meetingDuration; }

    //-------------------------------------------SETTER---------------------------------------
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public void setMeetingDate(String meetingDate) { this.meetingDate = meetingDate; }

    public void setMeetingTime(String meetingTime) { this.meetingTime = meetingTime; }

    public void setFromClass(String fromClass) { this.fromClass = fromClass; }

    public void setToClass(String toClass) { this.toClass = toClass; }

    public void setMeetingDuration(String meetingDuration) { this.meetingDuration = meetingDuration; }
}
