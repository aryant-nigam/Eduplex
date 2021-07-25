package com.example.Eduplex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeePayment extends AppCompatActivity {

    LinearLayout statement;
    Button paynow;
    AlertDialog.Builder builder;
    Animation translateNegX,translateX;
    TextView registrationNumberPayment, studentNamePayment, studentClassPayment, monthPayment, dueFeePayment, datePayment, schoolUpiPayment,feePaymentTitle;
    final int UPI_PAYMENT = 0;
    ArrayList<String> RecieptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_payment);


        initialise();
        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("CONFIRMATION")
                        .setMessage("You are about to move to payment gateway click CONTINUE to continue")
                        .setCancelable(false)
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                                makePayment();
                                Toast.makeText(FeePayment.this, "You will move to razor pay", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Toast.makeText(FeePayment.this, "You declined the payment", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void initialise() {
        translateNegX = AnimationUtils.loadAnimation(this, R.anim.translatenegx);
        translateX = AnimationUtils.loadAnimation(this, R.anim.translatex);
        feePaymentTitle=findViewById(R.id.feePaymentTitle);
        feePaymentTitle.startAnimation(translateNegX);
        statement = findViewById(R.id.statement);
        paynow = findViewById(R.id.paynow);
        statement.startAnimation(translateX);
        builder = new AlertDialog.Builder(this);
        registrationNumberPayment = findViewById(R.id.registrationNumberPayment);
        studentNamePayment = findViewById(R.id.studentNamePayment);
        studentClassPayment = findViewById(R.id.studentClassPayment);
        monthPayment = findViewById(R.id.monthPayment);
        dueFeePayment = findViewById(R.id.dueFeePayment);
        datePayment = findViewById(R.id.datePayment);
        schoolUpiPayment = findViewById(R.id.schoolUPIPayment);

        StudentsManager extras=(StudentsManager)getIntent().getSerializableExtra("StudentData");
        registrationNumberPayment.setText(extras.get_registrationNumber());
        studentNamePayment.setText(extras.get_name());
        studentClassPayment.setText(extras.get_class()+" - "+extras.get_section());
        ZoneId zoneId=ZoneId.of("Asia/Kolkata");
        LocalDate today=LocalDate.now(zoneId);
        monthPayment.setText(today.getMonth().toString());
        dueFeePayment.setText(extras.get_dueFee());
        datePayment.setText(today.toString());
        schoolUpiPayment.setText("UPI@institute");

    }

    private void makePayment() {
        final String dueFee = dueFeePayment.getText().toString();
        String schoolUpi = schoolUpiPayment.getText().toString();
        String note = "I "
                + studentNamePayment.getText().toString()
                + " of class "
                + studentClassPayment.getText().toString()
                + " confirms my due fee payment of month"
                + monthPayment.getText().toString()
                + " that amounts to "
                + dueFee
                + " on date "
                + datePayment.getText().toString();

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "9116208280@ybl")
                .appendQueryParameter("pn", "Vishal Nigam")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPaymentIntent = new Intent(Intent.ACTION_VIEW);
        upiPaymentIntent.setData(uri);

        //to show options among the app that can help with the transaction
        Intent paymentOptions = Intent.createChooser(upiPaymentIntent, "Proceed payment with");

        //Checks if the intent resolves
        if (null != paymentOptions.resolveActivity(getPackageManager())) {
            startActivityForResult(paymentOptions, UPI_PAYMENT);
        } else {
            Toast.makeText(getApplicationContext(), "No UPI app found, please install one to continue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getApplicationContext())) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            Map<String,String>responseMap= new HashMap<>();
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    responseMap.put(equalStr[0],equalStr[1]);
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            //Log.d("responsemap", String.valueOf(responseMap));
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(getApplicationContext(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getApplicationContext(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}