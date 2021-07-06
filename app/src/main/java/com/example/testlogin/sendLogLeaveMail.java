package com.example.testlogin;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import  javax.mail.Message;
import  javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import  javax.mail.Session;
import  javax.mail.Transport;
import  javax.mail.internet.InternetAddress;
import  javax.mail.internet.MimeMessage;
import  java.util.Properties;
public class sendLogLeaveMail {
    String classTeacherMail;
    String studentRegistrationNumber;
    String Subject;
    String Class;
    String Section;
    String Description;
    String StudentMail;
    String StudentMailPassword;

    public sendLogLeaveMail(String classTeacherMail, String studentRegistrationNumber, String Class, String section,String studentMail,String StudentMailPassword) {
        this.classTeacherMail = classTeacherMail;
        this.studentRegistrationNumber = studentRegistrationNumber;
        this.Class = Class;
        this.Section = section;
        this.StudentMail = studentMail;
        this.StudentMailPassword=StudentMailPassword;
    }

    public sendLogLeaveMail(String classTeacherMail, String studentRegistrationNumber, String subject,
                            String Class, String Section, String Description, String StudentMail,
                            String StudentMailPassword)//ClassTeacherMail, studentRegistrationNumber, Subject, studentsClass,
    //studentsSection, Description, studentEmailId, password
    {
        this.classTeacherMail = classTeacherMail;
        this.studentRegistrationNumber = studentRegistrationNumber;
        this.Subject = subject;
        this.Class = Class;
        this.Section = Section;
        this.Description = Description;
        this.StudentMail=StudentMail;
        this.StudentMailPassword=StudentMailPassword;
    }
    public boolean logMail(int flagActivity)
    {
        boolean logMyLeaveStatus=false;
        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session=Session.getInstance(properties,new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(StudentMail,StudentMailPassword);
            }
        });
        //Log.d("hi"," ");
        try {
            if(flagActivity==0) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(StudentMail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("shobhanigam9453@gmail.com"));
                message.setSubject(Subject);
                message.setText(createLeaveFormat());
                Transport.send(message);
                logMyLeaveStatus = true;
            }
            else if(flagActivity==1)
            {
                String attendenceCorrectionRequestSubject="Regarding correction of attendence";
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(StudentMail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("shobhanigam9453@gmail.com"));
                message.setSubject(attendenceCorrectionRequestSubject);
                message.setText(createRequestFormat());
                Log.d("logMail: ", String.valueOf(message));
                Transport.send(message);
                logMyLeaveStatus = true;
            }

        }catch (MessagingException me)
        {
            logMyLeaveStatus=false;
            throw new RuntimeException(me);
        }
        finally {
            return logMyLeaveStatus;
        }

    }

    private String createRequestFormat() {

        String attendenceCorrectionRequestBody="Sir/Mam my attendence that is being reflected in Eduplex is erroneous." +
                " I request you to kindly scrutinize it and inform/update suitably "+"\n"+Class+" - "+Section+"\n"+studentRegistrationNumber;;
        return attendenceCorrectionRequestBody;
    }

    private String createLeaveFormat()
    {
        String leaveApplication=Description+"\n"+Class+" - "+Section+"\n"+studentRegistrationNumber;
        return leaveApplication;
    }

}
