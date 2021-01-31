package org.androidtown.memoryexcercise;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumRemeberQuiz extends AppCompatActivity {
    //변수
    SharedPreferences randNumPref;
    SharedPreferences roundPref;
    SharedPreferences timesPref;
    SharedPreferences scorePref;

    int randomNumber;
    int round;
    int times;
    int score;
    int currentPoint,highestPoint1,num;

    //editText
    EditText editText;
    TextView pointText;
    boolean isNextStage = false;


    //스테이지 부여
    static int stageNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_remeber_quiz);

        pointText = (TextView)findViewById(R.id.PointText);
        editText = (EditText) findViewById(R.id.editText2);
        editText.requestFocus();

        randNumPref = getSharedPreferences("randNum", MODE_PRIVATE);
        randomNumber = randNumPref.getInt("randNum", 0);

//        roundPref = getSharedPreferences("round",MODE_PRIVATE);
//        round = roundPref.getInt("round",1);
//
//        timesPref = getSharedPreferences("times",MODE_PRIVATE);
//        times = timesPref.getInt("times",1);
//
//        scorePref = getSharedPreferences("score",MODE_PRIVATE);
//        score = scorePref.getInt("score",0);

        Intent intent1 = getIntent();
        num = intent1.getIntExtra("currentStage",1);
        currentPoint = intent1.getIntExtra("currentPoint",0);
        highestPoint1 = intent1.getIntExtra("highestPoint",0);
        System.out.println("NumberRemember Quiz 최고점:"+ highestPoint1);

        pointText.setText("Current point: "+ currentPoint+"     Highest point: "+highestPoint1+
                "\n\n Required points for next level: "+50*num);


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String numStr = editText.getText().toString();
                    if (numStr.equals("") || numStr.equals("-")) {// 사용자가 입력을 하지 않거나 숫자가 아닌 값을 입력한 경우
                        Toast.makeText(getApplicationContext(), "Enter the number", Toast.LENGTH_SHORT).show(); // 안내 메시지

                        return true; // 키보드가 남아있음. return false하면 키보드 내려감.
                    }

                    int number = Integer.parseInt(editText.getText().toString());
                    checkNumber(number);
                    return true;
                }
                return false;
            }
        });

        //스테이지
        stageNumber++;
    }

    public void checkNumber(int number) {
        if (isCorrect(number)) {//맞았을 때
//            if(times<3) {
//                times++;
//
//                SharedPreferences.Editor editor = timesPref.edit();
//                editor.putInt("times", times);
//                editor.commit();
//
//                score++;
//                SharedPreferences.Editor scoreEditor = scorePref.edit();
//                scoreEditor.putInt("score", score);
//                scoreEditor.commit();
//
//                if(times==3) {
//                    if(round!=3){
//                        times=1;
//                        SharedPreferences.Editor editor2 = timesPref.edit();
//                        editor2.putInt("times", times);
//                        editor2.commit();
//                        round++;
//                        SharedPreferences.Editor editor3 = roundPref.edit();
//                        editor3.putInt("round", round);
//                        editor3.commit();}
//                }
//            }
//            Intent intent = new Intent(getApplicationContext(), Correct.class);
//            startActivity(intent);
//            finish();

            //난이도 상승
            if(currentPoint == 40 ||currentPoint== 90 ||currentPoint== 140)
                NumRemember.round *= 10;


            //게임 종료 알림
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Correct!");
            //builder.setMessage("맞았습니다.\n(다음문제로 넘어가시겠습니까?)\n");
            builder.setPositiveButton("next game",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), NumRemember.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("currentPoint",currentPoint+10);
                            if(currentPoint <40)
                                intent.putExtra("currentStage",1);
                            else if(currentPoint >= 40)
                                intent.putExtra("currentStage",2);
                            else if(currentPoint >= 90)
                                intent.putExtra("currentStage",3);
                            else if(currentPoint >= 140)
                                intent.putExtra("currentStage",4);
                            else
                                intent.putExtra("currentStage",4);

                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("Go Home",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            stageNumber = 0;
                            InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            finish();
                        }
                    });
            builder.show();
        } else {//틀렸을 때
//            if(times<3) {
//                times++;
//
//                SharedPreferences.Editor editor = timesPref.edit();
//                editor.putInt("times", times);
//                editor.commit();
//
//                if(times==3) {
//                    if(round!=3){
//                        times=1;
//                        SharedPreferences.Editor editor2 = timesPref.edit();
//                        editor2.putInt("times", times);
//                        editor2.commit();
//                        round++;
//                        SharedPreferences.Editor editor3 = roundPref.edit();
//                        editor3.putInt("round", round);
//                        editor3.commit();
//                    }
//                }
//            }
//            Intent intent = new Intent(getApplicationContext(), Incorrect.class);
//            startActivity(intent);
//            finish();

            // 난이도 하강
            // NumRemember.round /= 10;

            // 틀렸을 때 진동
            startVibrate();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Incorrect!");
            //builder.setMessage("틀렸습니다.\n(다음문제로 넘어가시겠습니까?)\n");
            builder.setPositiveButton("next game",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), NumRemember.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("currentPoint",currentPoint);
                            intent.putExtra("currentStage",num);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton("Go Home",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            stageNumber = 0;
                            InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            finish();
                        }
                    });
            builder.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public boolean isCorrect(int number) {
        if (number == randomNumber)
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {

        dialogShow();
    }

    public void dialogShow() {
        //게임 종료 알림
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End game");
        builder.setCancelable(false);
        builder.setMessage("Would you like to end the game?\n(* Game data will be removed)");
        builder.setPositiveButton("Next Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        immhide.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                });
        builder.setNegativeButton("Go Home",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NumRemember.round = 10;
                        NumRemeberQuiz.super.onBackPressed();
                    }
                });
        builder.show();
    }

    public void startVibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
}