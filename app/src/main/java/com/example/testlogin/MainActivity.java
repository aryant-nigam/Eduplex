package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Animation translateX;
    private LinearLayout form;
    private EditText registrationNumber,password;
    private Button login;
    private ImageView showHideToggle;
    private TextView forgotYourPassword;
    boolean isPasswordVisible=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialise();

        //to login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent=new Intent(getApplicationContext(),drawer.class);
            startActivity(intent);
            }
        });

        //to show and hide the password
        showHideToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPasswordVisible)
                {
                    isPasswordVisible=true;
                    showHideToggle.setImageResource(R.drawable.eye_off);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    isPasswordVisible=false;
                    showHideToggle.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        //when forgot the password
        forgotYourPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
    }
}