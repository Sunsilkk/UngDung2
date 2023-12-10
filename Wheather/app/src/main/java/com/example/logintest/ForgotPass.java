package com.example.logintest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPass extends AppCompatActivity {

    private TextInputLayout Forgotpassinputlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass);

        Forgotpassinputlayout = findViewById(R.id.ForgotPass);
        Button btnreset = findViewById(R.id.btnReset);

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


    }
}
