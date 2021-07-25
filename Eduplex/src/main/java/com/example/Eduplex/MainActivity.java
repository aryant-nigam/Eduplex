package com.example.Eduplex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private Animation translateX;
    private LinearLayout form;
    private EditText registrationNumber,password;
    private Button login;
    private ImageView showHideToggle;
    private TextView forgotYourPassword;
    private ProgressBar progressBar;
    boolean isPasswordVisible=false;
    private StudentsManager studentsManager=new StudentsManager();
    private usernamePassword userpass =new usernamePassword();
   // private SharedPreferences studentData=;
   // SharedPreferences.Editor editor;
    String user;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         initialise();


        //to login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(CheckNetworkAvailibility.isConnectionAvailable(getApplicationContext()))
                {
                    if (validate()) {
                        if (user.equals("student")) {
                            DatabaseReference LOGINRECORD = FirebaseDatabase.getInstance().getReference().child("School").child("LoginRecord").child(registrationNumber.getText().toString());
                            LOGINRECORD.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    userpass = (usernamePassword) snapshot.getValue(usernamePassword.class);
                                    if (userpass.getPassword().equals(password.getText().toString())) {
                                        DatabaseReference STUDENTNODE = FirebaseDatabase.getInstance().getReference()
                                                .child("School").child("StudentsRecord")
                                                .child(userpass.get_class().split("-")[0].trim()).child(userpass.get_class().split("-")[1].trim()).child(userpass.username_regno);
                                        STUDENTNODE.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                studentsManager = snapshot.getValue(StudentsManager.class);

                                                Intent intent = new Intent(getApplicationContext(), drawer.class);
                                                intent.putExtra("user", user);
                                                intent.putExtra("StudentData", studentsManager);

                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            }

                                            @Override
                                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                            }

                                            @Override
                                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else if (user.equals("admin")) {
                            DatabaseReference LOGINRECORD = FirebaseDatabase.getInstance().getReference().child("School").child(registrationNumber.getText().toString());
                            LOGINRECORD.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        if (snapshot.child("_password").getValue().toString().equals(password.getText().toString())) {
                                            Toast.makeText(MainActivity.this, "Successfully Logged In As ADMIN", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), drawer.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        //to show and hide the password
        showHideToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPasswordVisible)
                {
                    isPasswordVisible=true;
                    showHideToggle.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    isPasswordVisible=false;
                    showHideToggle.setImageResource(R.drawable.eye_off);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        //when forgot the password
        forgotYourPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),forgotPassword.class);
                startActivity(intent);
            }
        });
    }
    private boolean validate()
    {
        if(registrationNumber.getText().toString().trim().length()==8) {
            user="student";
            return true;
        }
        else if(registrationNumber.getText().toString().trim().length()==12) {
            user = "admin";
            return true;
        }
        Toast.makeText(this, "Invalid Registration Number", Toast.LENGTH_SHORT).show();
        return  false;
    }
    public void initialise()
    {

        translateX= AnimationUtils.loadAnimation(this,R.anim.translatex);
        form=findViewById(R.id.form);
        form.startAnimation(translateX);

        //attaching all the elements to their xml elements
        registrationNumber=findViewById(R.id.registrationNumber);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        registrationNumber=findViewById(R.id.registrationNumber);
        showHideToggle=findViewById(R.id.showHideToggle);
        forgotYourPassword=findViewById(R.id.forgotYourPasword);
        progressBar=findViewById(R.id.load1);
    }
}