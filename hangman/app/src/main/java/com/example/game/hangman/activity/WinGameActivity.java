package com.example.game.hangman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.game.hangman.R;

public class WinGameActivity extends AppCompatActivity {

    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_game);

        answer = (TextView) findViewById(R.id.wordAnswerWinGame);
        answer.setText(getIntent().getStringExtra("answer"));

        Button newGameButton = (Button) findViewById(R.id.btnNewGameWinGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WinGameActivity.this, LobbyActivity.class));
                finish();
            }
        });

        Button exitGameButton = (Button) findViewById(R.id.btnExitWinGame);
        exitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WinGameActivity.this, NewGameActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WinGameActivity.this, NewGameActivity.class);
        startActivity(intent);
        finish();
    }
}
