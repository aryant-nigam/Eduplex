package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class forgotPassword extends AppCompatActivity {
    Spinner _class, _section;
    EditText regno, newPassword, reNewPassword, otpEntered;
    Button findMe, changePassword;
    ArrayList<String> otps;
    int sentOtpIx;
    HashMap studentmap=new HashMap();
    HashMap loginmap=new HashMap();
    StudentsManager studentsManager=new StudentsManager();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        initialise();

        findMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               progressBar.setVisibility(View.VISIBLE);
               DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord")
                       .child(_class.getSelectedItem().toString()).child(_section.getSelectedItem().toString()).child(regno.getText().toString());
               NODE.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                        if(snapshot.exists()) {
                            studentsManager=snapshot.getValue(StudentsManager.class);
                             //Toast.makeText(forgotPassword.this, studentsManager.get_email(), Toast.LENGTH_SHORT).show();
                            if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext())){
                                if (sendConfirmation(studentsManager.get_email().trim())) {
                                    findMe.setEnabled(false);
                                    progressBar.setVisibility(View.GONE);
                                    otpEntered.setVisibility(View.VISIBLE);
                                    newPassword.setVisibility(View.VISIBLE);
                                    reNewPassword.setVisibility(View.VISIBLE);
                                    changePassword.setVisibility(View.VISIBLE);
                                    Toast.makeText(forgotPassword.this, "Check Your registered Email", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(forgotPassword.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(forgotPassword.this, "Oh snap!! Try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(forgotPassword.this, "No Record Found !!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) { }
                    @Override
                    public void onChildRemoved(@NonNull  DataSnapshot snapshot) { }
                    @Override
                    public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) { }
                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) { }
                });

            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpEntered.getText().toString().equals(otps.get(sentOtpIx))) {
                    if (!newPassword.getText().toString().equals(reNewPassword.getText().toString()))
                        Toast.makeText(forgotPassword.this, "Entered Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    else {
                        DatabaseReference NODE = FirebaseDatabase.getInstance().getReference().child("School").child("StudentsRecord").child(_class.getSelectedItem().toString()).child(_section.getSelectedItem().toString()).child(regno.getText().toString());
                        studentsManager.set_password(newPassword.getText().toString());

                        studentmap.put("_registrationNumber", studentsManager.get_registrationNumber());
                        studentmap.put("_class", studentsManager.get_class());
                        studentmap.put("_section", studentsManager.get_section());
                        studentmap.put("_name", studentsManager.get_name());
                        studentmap.put("_email", studentsManager.get_email());
                        studentmap.put("_password", studentsManager.get_password());
                        studentmap.put("_dueFee", studentsManager.get_dueFee());
                        studentmap.put("_feeStatus",studentsManager.get_feeStatus().toString());

                        NODE.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(forgotPassword.this, "Working with updates", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {

                            }
                        });

                        NODE.push().setValue(studentsManager).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(forgotPassword.this, "Updating Your Password", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(forgotPassword.this, "Password Updation Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        DatabaseReference LOGINRECORD = FirebaseDatabase.getInstance().getReference().child("School").child("LoginRecord").child(studentsManager.get_registrationNumber());

                        LOGINRECORD.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(forgotPassword.this, "Just A Second!!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {

                            }
                        });

                        LOGINRECORD.push().setValue(new usernamePassword(studentsManager.get_registrationNumber(),studentsManager.get_password(),studentsManager.get_class()+"-"+studentsManager.get_section())).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(forgotPassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                Toast.makeText(forgotPassword.this, "Request Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else
                    Toast.makeText(forgotPassword.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            }
        });
}

    private boolean sendConfirmation(String studentEmail) {
        boolean logMyLeaveStatus=false;
        Properties properties=new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        Session session=Session.getInstance(properties,new javax.mail.Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("studybuddy162@gmail.com","goofy@2000");
            }
        });
        try {
            String resetPasswordRequestSubject="Technical Team @ Institute Name";
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("studybuddy162@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(studentEmail));
            message.setSubject(resetPasswordRequestSubject);
            message.setText(createConfirmationFormat());
            Transport.send(message);
        }
        catch (MessagingException me)
        {
            return false;
        }

        return true;
    }

    private String createConfirmationFormat() {
        Random rand=new Random();
        sentOtpIx =rand.nextInt(17);
        return "You were trying to RESET your password your One Time Password for proceeding with resetting the password is : "+String.valueOf(otps.get(sentOtpIx))+"\n\nTechnical Support"+"\n Institute's Name";
    }

    private void initialise() {
        _class=findViewById(R.id.fpClass);
        _section=findViewById(R.id.fpSection);
        regno=findViewById(R.id.fpRegno);
        newPassword=findViewById(R.id.fpNewPassword);
        reNewPassword=findViewById(R.id.fpNewPasswordRetype);
        otpEntered=findViewById(R.id.otp);
        findMe=findViewById(R.id.findMeButton);
        changePassword=findViewById(R.id.changePasswordButton);
        progressBar=findViewById(R.id.load5);
        ArrayList<String> classes=new ArrayList<String>();
        classes.add("Select Class");
        classes.add("1");
        classes.add("2");
        classes.add("3");
        classes.add("4");
        classes.add("5");
        classes.add("6");
        classes.add("7");
        classes.add("8");
        classes.add("9");
        classes.add("10");
        classes.add("11");
        classes.add("12");

        ArrayAdapter<String> arrayAdapterClass=new ArrayAdapter<String >(this,android.R.layout.simple_spinner_item,classes);
        arrayAdapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _class.setAdapter(arrayAdapterClass);

        ArrayList<String> sections = new ArrayList<String>();
        sections.add("Select Section");
        sections.add("A");
        sections.add("B");
        sections.add("C");
        sections.add("D");

        ArrayAdapter<String> arrayAdapterSection = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sections);
        arrayAdapterSection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _section.setAdapter(arrayAdapterSection);
        otps=new ArrayList<>();
        otps.add("867656");
        otps.add("878458");
        otps.add("321243");
        otps.add("214494");
        otps.add("454942");
        otps.add("323664");
        otps.add("798131");
        otps.add("326612");
        otps.add("130154");
        otps.add("154515");
        otps.add("512178");
        otps.add("956648");
        otps.add("547910");
        otps.add("326054");
        otps.add("146713");
        otps.add("013467");
        otps.add("457841");
    }
}