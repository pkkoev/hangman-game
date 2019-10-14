package com.example.game.hangman.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.game.hangman.NetworkHandler;
import com.example.game.hangman.R;

import org.json.JSONObject;

import java.net.URL;

public class RegisterFailedActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_failed);

        text = (TextView) findViewById(R.id.registerUsernameExists);
        text.setText(getIntent().getStringExtra("text"));

        Button regButton = (Button) findViewById(R.id.btnRegisterFailed);
        final EditText username = (EditText) findViewById(R.id.regUsernameRegisterFailed);
        final EditText password = (EditText) findViewById(R.id.regPasswordRegisterFailed);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject urlParameters = new JSONObject();
                try {
                    Intent intent;
                    URL url = new URL("http://10.0.2.2:8080/hangman/register");
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
                        intent = new Intent(RegisterFailedActivity.this, NewGameActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(RegisterFailedActivity.this, RegisterFailedActivity.class);
                        intent.putExtra("text", "username already exists");
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
        Intent intent = new Intent(RegisterFailedActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
