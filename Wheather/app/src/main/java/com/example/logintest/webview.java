package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.internal.Util;

public class webview extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        WebView wv = findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().removeAllCookies(null);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        CookieManager.getInstance().removeAllCookies(null);

        String usr = extras.getString("EXTRA_USERNAME");
        String email = extras.getString("EXTRA_EMAIL");
        String pwd = extras.getString("EXTRA_PASSWORD");
        String rePwd = extras.getString("EXTRA_REPASSWORD");

        String forgotpass = extras.getString("forgotpass");



        wv.setWebViewClient(new WebViewClient(){
            @Override

            public void onPageFinished(WebView view, String url) {

                if (url.contains("auth")){
                    String redirect = "document.getElementsByTagName('a')[0].click();";
                    view.evaluateJavascript(redirect,null);
                }


                if (url.contains("login-actions/registration")) {
                    Log.d("webView", "onPageFinished: Fill form");

                    String dataError = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
                    extras.putString("dataError",dataError);


                    view.evaluateJavascript(dataError,dErr->{
                        if (!dErr.isEmpty()) {
                            Intent intent = new Intent();
                            intent.putExtra("dataError", dErr);
                            setResult(RegisActivity.REGIS_REQUEST_CODE, intent);
                            finishActivity(RegisActivity.REGIS_REQUEST_CODE);
                        }

                        String usrScrript = "document.getElementById('username').value='" + usr + "';";
                        String emailScrript = "document.getElementById('email').value='" + email + "';";
                        String pwdScrript = "document.getElementById('password').value='" + pwd + "';";
                        String rePwdScrript = "document.getElementById('password-confirm').value='" + rePwd + "';";

                        view.evaluateJavascript(usrScrript,null);
                        view.evaluateJavascript(emailScrript,null);
                        view.evaluateJavascript(pwdScrript,null);
                        view.evaluateJavascript(rePwdScrript,null);
                        view.evaluateJavascript("document.getElementsByTagName('form')[0].submit();", null);
                    });

                }

                if (url.contains("login-actions/reset-credentials")){
                    String dataError = "document.getElementsByClassName('helper-text')[0].getAttribute('data-error');";
                    extras.putString("dataError",dataError);


                    view.evaluateJavascript(dataError,dErr->{
                        String ForgotScrript = "document.getElementById('username').value='" + forgotpass + "';";

                        view.evaluateJavascript(ForgotScrript,null);

                        view.evaluateJavascript("document.getElementsByTageName('form')[0].submit();",null);
                    });

                }

                if (url.contains("manager")){
                    Intent intent = new Intent(webview.this, MainActivity.class);
                    startActivity(intent);

                }
            }
        });



        wv.loadUrl("https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/auth?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&state=e2d1bdbc-ef9b-4cb4-9789-69e4af40871e&response_mode=fragment&response_type=code&scope=openid&nonce=1a892f30-ea5d-40bf-a01d-c4defc3b5fa2");
    }

}