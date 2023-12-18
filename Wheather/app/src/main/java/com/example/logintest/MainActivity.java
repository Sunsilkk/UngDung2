package com.example.logintest;
import static com.example.logintest.allVar.darkBackground;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.logintest.API.APIInterface;
import com.example.logintest.API.ApIClient;
import com.example.logintest.Model.token;
import com.google.android.material.textfield.TextInputLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private APIInterface apiInterface;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    Bundle bundle;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout background = findViewById(R.id.login);
        AnimationDrawable animation = (AnimationDrawable) background.getBackground();
        animation.setEnterFadeDuration(2000);
        animation.setExitFadeDuration(4500);
        animation.start();


//        if (darkBackground)
//        {
//            background.setBackgroundResource(R.drawable.dark_backgr);
//        }

        usernameTextInputLayout = findViewById(R.id.Username);
        passwordTextInputLayout  = findViewById(R.id.Password);
//        TextView status = findViewById(R.id.Status);
        TextView notaMem = findViewById(R.id.NotaMem);
        TextView fgPass = findViewById(R.id.FgPass);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Create an instance of Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the APIInterface
        apiInterface = retrofit.create(APIInterface.class);
       // Intent intent = new Intent(MainActivity.this, )
        // Login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientId = "openremote";
                String enteredUsername = usernameTextInputLayout.getEditText().getText().toString();
                String enteredPassword = passwordTextInputLayout.getEditText().getText().toString();
                String grantType = "password";

                // Call the login API method
                Call<token> call = apiInterface.login(clientId, enteredUsername, enteredPassword, grantType);
                call.enqueue(new Callback<token>() {
                    @Override
                    public void onResponse(Call<token> call, Response<token> response) {
                        if (response.isSuccessful()) {
                            // Login successful, handle the response
                            token loggedInUser = response.body();
                            String accessToken = loggedInUser.access_token;
                            ApIClient.setToken(accessToken);
                            ApIClient.setUsername(enteredUsername);
                            intent = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(intent);
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<token> call, Throwable t) {

                    }
                });
            }
        });


        notaMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RegisActivity.class);
                startActivity(intent);
            }
        });

        fgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPass.class);
                startActivity(intent);
            }
        });
        Button buttonchange = findViewById(R.id.btndaynight);
        buttonchange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                darkBackground = !(darkBackground);
                int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                int newNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES ?
                        AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
                AppCompatDelegate.setDefaultNightMode(newNightMode);
                recreate();
            }
        });
    }
}