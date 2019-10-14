package com.example.game.hangman.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.game.hangman.NetworkHandler;
import com.example.game.hangman.R;

import org.json.JSONObject;

import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.btnLogin);
        final EditText username = (EditText) findViewById(R.id.loginUsernameLogin);
        final EditText password = (EditText) findViewById(R.id.loginPasswordLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject urlParameters = new JSONObject();
                try {
                    Intent intent;
                    URL url = new URL("http://10.0.2.2:8080/hangman/authorize");
                    String un = username.getText().toString();
                    String pw = password.getText().toString();
                    urlParameters.put("username", un);
                    urlParameters.put("password", pw);
                    NetworkHandler networkHandler = new NetworkHandler(urlParameters, url);
                    Object[] ObjectTmp = new Object[1];
                    AsyncTask taskResult = networkHandler.execute(ObjectTmp);
                    Object result = taskResult.get();
                    String resultStr = result.toString();

                    if (resultStr.equals("true")) {
                        intent = new Intent(LoginActivity.this, NewGameActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(LoginActivity.this, LoginFailedActivity.class);
                        intent.putExtra("text", "username or password is incorrect");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
