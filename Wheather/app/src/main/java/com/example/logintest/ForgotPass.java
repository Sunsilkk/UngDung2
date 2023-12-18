package com.example.logintest;

import static com.example.logintest.allVar.darkBackground;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPass extends AppCompatActivity {

    private TextInputLayout Forgotpassinputlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass);

        Forgotpassinputlayout = findViewById(R.id.ForgotPass);
        Button btnreset = findViewById(R.id.btnReset);
        Button btnBack = findViewById(R.id.btnBack2);
        ConstraintLayout background = findViewById(R.id.forgotPass);
        if (darkBackground) {
            background.setBackgroundResource(R.drawable.dark_backgr);
        }
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPass.this, Webview2.class);
                Bundle extras = new Bundle();

                String forgotpass = Forgotpassinputlayout.getEditText().getText().toString();
                extras.putString("forgotpass",forgotpass);

                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}
