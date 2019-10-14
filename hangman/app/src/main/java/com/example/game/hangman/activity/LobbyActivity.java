package com.example.game.hangman.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.game.hangman.LetterAdapter;
import com.example.game.hangman.R;
import com.example.game.hangman.WordHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class LobbyActivity extends AppCompatActivity {

    private GridView letters;
    private LetterAdapter ltrAdapt;

    private String word;
    private LinearLayout wordLayout;
    private TextView[] charViews;

    private ImageView[] bodyParts;
    private int bodyPartsNum = 6;
    private int currentPart = 0;
    private int wordCharsNum;
    private int correctCharsNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        bodyParts = new ImageView[bodyPartsNum];

        bodyParts[0] = (ImageView) findViewById(R.id.head);
        bodyParts[1] = (ImageView) findViewById(R.id.body);
        bodyParts[2] = (ImageView) findViewById(R.id.armRight);
        bodyParts[3] = (ImageView) findViewById(R.id.armLeft);
        bodyParts[4] = (ImageView) findViewById(R.id.legRight);
        bodyParts[5] = (ImageView) findViewById(R.id.legLeft);

        for (int i = 0; i < 6; i++) {
            bodyParts[i].setVisibility(View.INVISIBLE);
        }

        Resources res = getResources();

        URL url = null;
        try {
            url = new URL("http://10.0.2.2:8080/hangman/getWord");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        WordHandler wordHandler = new WordHandler(url);

        Object[] ObjectTmp = new Object[1];
        AsyncTask taskResult = wordHandler.execute(ObjectTmp);
        try {
            Object result = taskResult.get();
            word = result.toString().toUpperCase();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        wordLayout = (LinearLayout)findViewById(R.id.word);

        letters = (GridView)findViewById(R.id.letters);
        ltrAdapt=new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);

        playGame();
    }

    private void playGame() {
        String newWord = word;

        charViews = new TextView[word.length()];
        wordLayout.removeAllViews();

        for (int c = 0; c < word.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText("" + word.charAt(c));

            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(charViews[c].getTextColors().withAlpha(0));
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            wordLayout.addView(charViews[c]);

            wordCharsNum = word.length();
        }
    }

    public void letterPressed(View view) {
        String ltr=((TextView)view).getText().toString();
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setVisibility(View.GONE);

        boolean correct = false;
        for(int k = 0; k < word.length(); k++) {
            if(word.charAt(k)==letterChar){
                correct = true;
                correctCharsNum++;
                charViews[k].setTextColor(Color.WHITE);
            }
        }

        if (correct) {
            if (correctCharsNum == wordCharsNum) {
                disableBtns();
                Intent intent = new Intent(LobbyActivity.this, WinGameActivity.class);
                intent.putExtra("answer", word);
                startActivity(intent);
                finish();
            }
        } else {
            if (currentPart < bodyPartsNum) {
                bodyParts[currentPart].setVisibility(View.VISIBLE);
                currentPart++;
            }
            if (currentPart == bodyPartsNum){
                disableBtns();
                Intent intent = new Intent(LobbyActivity.this, LooseGameActivity.class);
                intent.putExtra("answer", word);
                startActivity(intent);
                finish();
                }
            }
    }

    public void disableBtns() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LobbyActivity.this, NewGameActivity.class);
        startActivity(intent);
        finish();
    }
}
