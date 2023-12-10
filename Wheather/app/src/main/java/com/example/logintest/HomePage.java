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



public class HomePage extends AppCompatActivity {

    private ImageView backgroundImage;
    private com.google.android.material.button.MaterialButton changeButton;
    private View dot1, dot2, dot3, dot4;

    private int currentImageIndex = 1;
    private int totalImages = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        backgroundImage = findViewById(R.id.backgroundImage);
        changeButton = findViewById(R.id.changeButton);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);

        updateDots();


        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageIndex = (currentImageIndex % totalImages) + 1;

                // Fade out the current image
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(backgroundImage, View.ALPHA, 1f, 0f);
                fadeOut.setDuration(500);
                fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());

                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        int imageResource = getResources().getIdentifier("image" + currentImageIndex, "drawable", getPackageName());
                        backgroundImage.setImageResource(imageResource);

                        // Fade in the new image
                        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(backgroundImage, View.ALPHA, 0f, 1f);
                        fadeIn.setDuration(500);
                        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
                        fadeIn.start();
                    }
                });

                fadeOut.start();

                updateDots();

                if (currentImageIndex == totalImages) {
                    changeButton.setText("Login");
//                    changeButton.setIcon(getDrawable());
                    changeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Chuyển sang activity mới (ví dụ: LoginActivity)
                            Intent intent = new Intent(HomePage.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void updateDots() {
        dot1.setBackgroundResource(R.drawable.dot_inactive);
        dot2.setBackgroundResource(R.drawable.dot_inactive);
        dot3.setBackgroundResource(R.drawable.dot_inactive);
        dot4.setBackgroundResource(R.drawable.dot_inactive);

        AnimatorSet dotAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.dot_animation);
        dotAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        switch (currentImageIndex) {
            case 1:
                dotAnimation.setTarget(dot1);
                dotAnimation.start();
                dot1.setBackgroundResource(R.drawable.dot_active);
                break;
            case 2:
                dotAnimation.setTarget(dot2);
                dotAnimation.start();
                dot2.setBackgroundResource(R.drawable.dot_active);
                break;
            case 3:
                dotAnimation.setTarget(dot3);
                dotAnimation.start();
                dot3.setBackgroundResource(R.drawable.dot_active);
                break;
            case 4:
                dotAnimation.setTarget(dot4);
                dotAnimation.start();
                dot4.setBackgroundResource(R.drawable.dot_active);
                break;
        }
    }
}