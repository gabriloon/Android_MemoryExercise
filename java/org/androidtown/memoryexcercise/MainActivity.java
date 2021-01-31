package org.androidtown.memoryexcercise;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //버튼 UI

    ImageButton enterEx1Btn, enterEx2Btn, enterEx3Btn, enterEx4Btn, enterEx5Btn;

    //종료버튼
    long bpTime = 0;
    Toast bpToast;

    //클래스
    NumRemember numRemember;
    NumRemeberQuiz numRemeberQuiz;

    /// /버튼
    Button numRememberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //클래스
        numRemember = new NumRemember();
        numRemeberQuiz = new NumRemeberQuiz();

        //클래스
        SharedPreferences roundPref = getSharedPreferences("round",MODE_PRIVATE);
        SharedPreferences timesPref = getSharedPreferences("times",MODE_PRIVATE);
        SharedPreferences randNumPref = getSharedPreferences("randNum", MODE_PRIVATE);

        SharedPreferences.Editor editor = randNumPref.edit();
        editor.putInt("randNum", 0);
        editor.commit();

        editor = timesPref.edit();
        editor.putInt("times", 1);
        editor.commit();

        editor = roundPref.edit();
        editor.putInt("round", 1);
        editor.commit();

        //버튼 UI 선언

        enterEx1Btn = (ImageButton) findViewById(R.id.prevPictureRemember);
        enterEx2Btn = (ImageButton) findViewById(R.id.blinkingBallRemember);
        enterEx3Btn = (ImageButton) findViewById(R.id.lottoNumRemember);
        enterEx4Btn = (ImageButton) findViewById(R.id.fruitRemember);
        enterEx5Btn = (ImageButton) findViewById(R.id.wordRemember);

        //종료 버튼 문구
        bpToast = Toast.makeText(this, "뒤로가기를 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT);


        //버튼을 클릭할 경우
        enterEx1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Excercise1Activity.class);
                startActivity(intent);
            }
        });

        enterEx2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game2Activity.class);
                startActivity(intent);
            }
        });

        enterEx3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NumRemember.class);
                startActivity(intent);
            }
        });
        enterEx4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), excercise4Activity.class);
                startActivity(intent);
            }
        });
        enterEx5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game5Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //참고 : http://best421.tistory.com/71
        //이렇게 해도 cancel이 안되는 이유는 onBackPressed()에 들어올 때마다 toast가 새로 생성되기 때문에
        //결국 cancel하는 toast는 다른 값이 된다. -> 전역변수로 설정하자.
//        Toast toast = Toast.makeText(this, "뒤로가기를 한번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT);
        if(bpTime == 0) {
            System.out.println("토스트1 : "+ bpToast);
            ViewGroup group = (ViewGroup) bpToast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            bpToast.show();
            bpTime = System.currentTimeMillis();
        }
        else {
            long sec = System.currentTimeMillis() - bpTime;

            if(sec > 2000) {
                System.out.println("토스트2 : " + bpToast);
                bpToast.show();
                bpTime = System.currentTimeMillis();
            }
            else {
                bpToast.cancel();
                super.onBackPressed();
                finish();
            }
        }
    }



}