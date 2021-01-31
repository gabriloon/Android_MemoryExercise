package org.androidtown.memoryexcercise;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckAnswer extends AppCompatActivity {
    CountDownTimer timer;
    ImageView correctImg, incorrectImg;
    TextView textView0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_answer);

        correctImg = (ImageView) findViewById(R.id.correctImg);
        incorrectImg = (ImageView) findViewById(R.id.incorrectImg);
        textView0 = (TextView) findViewById(R.id.textView0);

        timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                timer.cancel();
                Intent intent = new Intent(getApplicationContext(), Game5Activity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Game5Activity로 부터 전달 받은 intent를 확인한다. (correct or incorrect)
        Intent intent = getIntent();
        boolean isCorrect = intent.getBooleanExtra("isCorrect", true);

        if(isCorrect) {
            correctImg.setVisibility(View.VISIBLE);
            incorrectImg.setVisibility(View.GONE);
            textView0.setText("Correct!");
        }
        else {
            correctImg.setVisibility(View.GONE);
            incorrectImg.setVisibility(View.VISIBLE);
            textView0.setText("Incorrect!");
        }

//        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        timer.cancel();
        timer.start();
    }
}
