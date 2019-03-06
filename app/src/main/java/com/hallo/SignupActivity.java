package com.hallo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static com.hallo.MainActivity.PARAM_PHONE_NUMBER;
import static com.hallo.MainActivity.PARAM_REMEMBER_PHONE_NUMBER;

public class SignupActivity extends Activity {
    Context context;
    Button btnsignup, btnSignUpContinue;
    CheckBox cbRememberMe;
    EditText etPhoneNumber;
    PersistentStore store;
    String Remember = "0";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.signup_layout);
        btnsignup = findViewById(R.id.btnSignIn);
        btnSignUpContinue = findViewById(R.id.btnSignUpContinue);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        store = PersistentStore.getInstance(context);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int phoneNumber = Integer.parseInt(etPhoneNumber.getText().toString());
                if(verifyPhoneNumber(phoneNumber)){
                    if(cbRememberMe.isChecked()){
                        Remember = "1";
                    }
                    store.saveUser(Integer.toString(phoneNumber), Remember);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
                    intent.putExtra(PARAM_REMEMBER_PHONE_NUMBER, Remember);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT);
                }
            }
        });
        btnSignUpContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store.saveUser("0", "0");
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(PARAM_PHONE_NUMBER, "0");
                startActivity(intent);
            }
        });
    }
    private boolean verifyPhoneNumber(int PhoneNumber){
        return true;
    }
}
