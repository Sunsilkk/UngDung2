package com.example.logintest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class RegisActivity extends AppCompatActivity {
    public static final int REGIS_REQUEST_CODE = 2001;

    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout repasswordTextInputLayout;
    private TextInputLayout emailTextInputLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        Button btnSignup = findViewById(R.id.btnSignUp);
        usernameTextInputLayout  = findViewById(R.id.Username);
        emailTextInputLayout  = findViewById(R.id.email);
        passwordTextInputLayout  = findViewById(R.id.Pwd);
        repasswordTextInputLayout  = findViewById(R.id.Repwd);
        btnSignup.setOnClickListener(v -> {
            usernameTextInputLayout.setError(null);
            emailTextInputLayout.setError(null);
            passwordTextInputLayout.setError(null);
            repasswordTextInputLayout.setError(null);

            Intent intent = new Intent(RegisActivity.this, webview.class);
            Bundle extras = new Bundle();
            String username = usernameTextInputLayout.getEditText().getText().toString();
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();
            String rePass = repasswordTextInputLayout.getEditText().getText().toString();

            extras.putString("EXTRA_USERNAME",username);
            extras.putString("EXTRA_EMAIL",email);
            extras.putString("EXTRA_PASSWORD",password);
            extras.putString("EXTRA_REPASSWORD",rePass);

            String dataError = extras.getString("dataError");
            passwordTextInputLayout.setError(dataError);


            intent.putExtras(extras);
            startActivityForResult(intent, REGIS_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dErr = data.getStringExtra("dataError");
        if (dErr.contains("email")) {
            emailTextInputLayout.setError(dErr);
        } else if (dErr.contains("user")) {
            usernameTextInputLayout.setError(dErr);
        } else if (dErr.contains("pass")) {
            passwordTextInputLayout.setError(dErr);
        } else {
            Toast.makeText(this, dErr, Toast.LENGTH_SHORT).show();
        }
    }
}