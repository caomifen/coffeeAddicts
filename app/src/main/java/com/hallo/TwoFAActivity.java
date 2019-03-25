package com.hallo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

import static com.hallo.MainActivity.PARAM_PHONE_NUMBER;
import static com.hallo.MainActivity.PARAM_REMEMBER_PHONE_NUMBER;

public class TwoFAActivity extends AppCompatActivity {
    final int min = 0;
    final int max = 26;
    final int size =5;
    private String auth_code="";
    TextView tvTimer;
    EditText etVerificationcode;
    CountDownTimer tmr;
    Button btnVerify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twofa_layout);
        TextView tvInstruction = findViewById(R.id.tvInstruction);
        btnVerify = findViewById(R.id.btnVerify);
        tvTimer = findViewById(R.id.tvCountdownTimer);
        etVerificationcode = findViewById(R.id.etAuthCode);
        etVerificationcode.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        TextView tvResend = findViewById(R.id.tvResend);
        final ConstraintLayout mainlayout =findViewById(R.id.clMainFrame);
        final ConstraintLayout Verifiedlayout =findViewById(R.id.VerifiedFrame);

        final String phone_num = getIntent().getExtras().get(PARAM_PHONE_NUMBER).toString();
        final String remember_me = getIntent().getExtras().get(PARAM_REMEMBER_PHONE_NUMBER).toString();
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                auth_code = generateCode();
                sendSMS(phone_num, auth_code+ " is your code.");
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etVerificationcode.getText().toString().equals(auth_code)){
                    //Snackbar.make(view, "Number Verified!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    PersistentStore store = PersistentStore.getInstance(TwoFAActivity.this);
                    store.saveUser(phone_num, remember_me);
                    mainlayout.setVisibility(View.GONE);
                    Verifiedlayout.setVisibility(View.VISIBLE);
                    VerifiedScreenTimer(phone_num);

                }else {
                    Snackbar.make(view, "Invalid Verification code. ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        auth_code = generateCode();
        String sms_msg=auth_code+ " is your code.";
        //et
        //Verificationcode.setText(auth_code);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendSMS(phone_num, sms_msg);
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            //sendSMS(phone_num, sms_msg);
        }

        tvInstruction.setText("The verification code has been sent via SMS to mobile number "+ phone_num + ". Key in the code below to proceed.");
        startTimer();
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void startTimer(){
        btnVerify.setEnabled(true);
        if(tmr!= null){
            tmr.cancel();
        }
        tmr = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText( millisUntilFinished / 1000 + " sec left");
            }

            public void onFinish() {
                tvTimer.setText("code expired");
                btnVerify.setEnabled(false);

            }


        }.start();
    }

    public String generateCode(){
        Random r;
        String code = "";
        int num;
        for( int i =0; i< size; i++){
            r = new Random();
            num = r.nextInt( (max- min)+1) + min;
            switch(num){
                case 1:
                    code = code +"A";
                    break;
                case 2:
                    code = code +"B";
                    break;
                case 3:
                    code = code +"C";
                    break;
                case 4:
                    code = code +"D";
                    break;
                case 5:
                    code = code +"E";
                    break;
                case 6:
                    code = code +"F";
                    break;
                case 7:
                    code = code +"G";
                    break;
                case 8:
                    code = code +"H";
                    break;
                case 9:
                    code = code +"I";
                    break;
                case 10:
                    code = code +"J";
                    break;
                case 11:
                    code = code +"K";
                    break;
                case 12:
                    code = code +"L";
                    break;
                case 13:
                    code = code +"M";
                    break;
                case 14:
                    code = code +"N";
                    break;
                case 15:
                    code = code +"O";
                    break;
                case 16:
                    code = code +"P";
                    break;
                case 17:
                    code = code +"Q";
                    break;
                case 18:
                    code = code +"R";
                    break;
                case 19:
                    code = code +"S";
                    break;
                case 20:
                    code = code +"T";
                    break;
                case 21:
                    code = code +"U";
                    break;
                case 22:
                    code = code +"V";
                    break;
                case 23:
                    code = code +"W";
                    break;
                case 24:
                    code = code +"X";
                    break;
                case 25:
                    code = code +"Y";
                    break;
                case 26:
                    code = code +"Z";
                    break;
                default:
                    code = code + "1";
                    break;
            }
        }
        return code;
    }

    public void VerifiedScreenTimer(final String Phone_num){

        CountDownTimer V_tmr = new CountDownTimer(3000, 3000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra(PARAM_PHONE_NUMBER, Phone_num);
                finish();
                startActivity(i);
            }


        }.start();
    }

}
