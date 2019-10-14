package com.example.game.hangman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.game.hangman.R;

public class LooseGameActivity extends AppCompatActivity {

    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loose_game);

        answer = (TextView) findViewById(R.id.wordAnswerLooseGame);
        answer.setText(getIntent().getStringExtra("answer"));

        Button newGameButton = (Button) findViewById(R.id.btnNewGameLooseGame);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LooseGameActivity.this, LobbyActivity.class));
                finish();
            }
        });

        Button exitGameButton = (Button) findViewById(R.id.btnExitLooseGame);
        exitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LooseGameActivity.this, NewGameActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LooseGameActivity.this, NewGameActivity.class);
        startActivity(intent);
        finish();
    }
}