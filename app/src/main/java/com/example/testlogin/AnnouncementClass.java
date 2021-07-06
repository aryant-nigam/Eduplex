package com.example.testlogin;

public class AnnouncementClass {
    private String announcementTitle;
    private String announcementBody;

    public AnnouncementClass(String announcementTitle, String announcementBody) {
        this.announcementTitle = announcementTitle;
        this.announcementBody = announcementBody;
    }

    public String getAnnouncementSubject() {
        return announcementTitle;
    }

    public void setAnnouncementSubject(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementBody() {
        return announcementBody;
    }

    public void setAnnouncementBody(String announcementBody) {
        this.announcementBody = announcementBody;
    }
}
