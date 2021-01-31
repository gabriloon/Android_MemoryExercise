package org.androidtown.memoryexcercise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 박재성 on 2017-11-25.
 */

public class Excercise1Activity extends AppCompatActivity {

    //UI 요소들
    Button ex1YesBtn;
    Button ex1NoBtn;

    TextView ex1Question1;
    TextView ex1Question2;
    TextView ex1Question3;
    TextView countdown;

    ImageView ex1Image;
    LinearLayout answerLinear;

    static int problemNumber = 0;

    //타이머
    CountDownTimer timer = null;

    //카운트
    int count = 4;

    //정답 카운트
    static int answerCount = 0;

    //랜덤변수
    String randomImage1 = null;
    String randomImage2 = null;

    int randomnumber0;
    int randomnumber1;
    int randomnumber2;

    String[] randomCatagory;

    //뒤로가기 버튼 누를시 사용 변수
    int returnValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excercise1);

        //문제 수 초기화
        //counterProblem();

        //UI 가져오기
        ex1YesBtn = (Button) findViewById(R.id.ex1YesBtn);
        ex1NoBtn = (Button) findViewById(R.id.ex1NoBtn);

        ex1Question1 = (TextView) findViewById(R.id.ex1Question1);
        ex1Question2 = (TextView) findViewById(R.id.ex1Question2);
        ex1Question3 = (TextView) findViewById(R.id.ex1Question3);
        countdown = (TextView) findViewById(R.id.countdown);

        ex1Image = (ImageView) findViewById(R.id.ex1Image);
        answerLinear = (LinearLayout) findViewById(R.id.answerLinear);

        //ex1Question1.setText(problemNumber + ". " + "사진을 기억하세요");

        //첫 랜덤 초기화
        initRandom();

        //첫 이미지 초기화
        initImage();

        //카운트 다운
        timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count--;
                countdown.setText("Questions will be asked\nin " + count + " second");
            }

            @Override
            public void onFinish() {
                count = 4;

                ex1Question1.setVisibility(View.INVISIBLE);
                countdown.setVisibility(View.INVISIBLE);

                ex1Question2.setVisibility(View.VISIBLE);
                ex1Question3.setVisibility(View.VISIBLE);
                answerLinear.setVisibility(View.VISIBLE);

                //이미지를 바꿔준다
                changeImage();
            }
        }.start();

        ex1YesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //정답일경우
                if (randomImage1.equals(randomImage2)) {

                    //정답갯수 올리기
                    answerCount++;

                    //게임 상황 알림
                    AlertDialog.Builder builder = new AlertDialog.Builder(Excercise1Activity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Correct!");

                    //마지막 문제일경우
                    if(problemNumber == 5)
                    {
                        builder.setMessage("Correct!.\n(This is the last question.)");
                        builder.setNegativeButton("yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        counterProblem();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    //마지막 문제가 아닐 경우
                    else {
                    builder.setPositiveButton("next game",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), Excercise1Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                    builder.setNegativeButton("Go Home",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    answerCount = 0;
                                    problemNumber = 0;
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    }

                //틀린경우
                } else {
                    //게임 상황 알림
                    AlertDialog.Builder builder = new AlertDialog.Builder(Excercise1Activity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Incorrect!");

                    //마지막 문제일경우
                    if (problemNumber == 5) {
                        startVibrate();
                        builder.setMessage("Incorrect!\n(This is the last question.)");
                        builder.setNegativeButton("yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    //마지막 문제가 아닌경우
                    else {
                        startVibrate();
                        builder.setPositiveButton("next game",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), Excercise1Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                        builder.setNegativeButton("Go Home",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (problemNumber != 5) {
                                            answerCount = 0;
                                            problemNumber = 0;
                                            finish();
                                        }
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        });

        ex1NoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //정답일경우
                if (!randomImage1.equals(randomImage2)) {

                    //정답갯수 올리기
                    answerCount++;

                    //게임 상황 알림
                    AlertDialog.Builder builder = new AlertDialog.Builder(Excercise1Activity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Correct!");

                    //마지막 문제일경우
                    if(problemNumber == 5)
                    {
                        builder.setMessage("Correct!\n(This is the last question.)");
                        builder.setNegativeButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        counterProblem();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    //마지막 문제가 아닐 경우
                    else {
                        builder.setPositiveButton("next game",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), Excercise1Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                        builder.setNegativeButton("Go Home",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        answerCount = 0;
                                        problemNumber = 0;
                                        finish();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    //틀린경우
                } else {
                    //게임 상황 알림
                    AlertDialog.Builder builder = new AlertDialog.Builder(Excercise1Activity.this);
                    builder.setCancelable(false);
                    builder.setTitle("Incorrect!");

                    //마지막 문제일경우
                    if (problemNumber == 5) {
                        startVibrate();
                        builder.setNegativeButton("yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        counterProblem();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    //마지막 문제가 아닌경우
                    else {
                        startVibrate();
                        builder.setPositiveButton("next game",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), Excercise1Activity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                        builder.setNegativeButton("Go Home",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (problemNumber != 5) {
                                            answerCount = 0;
                                            problemNumber = 0;
                                            finish();
                                        }
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        });
    }

    public void counterProblem() {
        problemNumber++;

        // 최대 5문제까지
        if (problemNumber > 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Excercise1Activity.this);
            builder.setCancelable(false);
            builder.setTitle("Result");
            builder.setMessage("Correct! : " + answerCount );
            answerCount = 0;
            builder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            problemNumber = 0;
                            Excercise1Activity.super.onBackPressed();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void initRandom() {
        //랜덤 카테고리 목록들
        randomCatagory = new String[]{"dog", "object"};

        //랜덤 카테고리(0 ~)
        randomnumber0 = (int) (Math.random() * 2);

        //랜덤함수 ( 1 ~ 10 )
        randomnumber1 = (int) (Math.random() * 10) + 1;
        randomnumber2 = (int) (Math.random() * 10) + 1;

        //랜덤이미지
        randomImage1 = randomCatagory[randomnumber0] + randomnumber1;
        randomImage2 = randomCatagory[randomnumber0] + randomnumber2;

        //랜덤 결과 출력
        System.out.println("랜덤 결과 : " + randomImage1 + " ," + randomImage2);
    }

    public void initImage() {
        //첫 이미지
        if (randomImage1.contains("dog")) {
            if (randomImage1.equals("dog1")) {
                ex1Image.setImageResource(R.drawable.dog1);
            } else if (randomImage1.equals("dog2")) {
                ex1Image.setImageResource(R.drawable.dog2);
            } else if (randomImage1.equals("dog3")) {
                ex1Image.setImageResource(R.drawable.dog3);
            } else if (randomImage1.equals("dog4")) {
                ex1Image.setImageResource(R.drawable.dog4);
            } else if (randomImage1.equals("dog5")) {
                ex1Image.setImageResource(R.drawable.dog5);
            } else if (randomImage1.equals("dog6")) {
                ex1Image.setImageResource(R.drawable.dog6);
            } else if (randomImage1.equals("dog7")) {
                ex1Image.setImageResource(R.drawable.dog7);
            } else if (randomImage1.equals("dog8")) {
                ex1Image.setImageResource(R.drawable.dog8);
            } else if (randomImage1.equals("dog9")) {
                ex1Image.setImageResource(R.drawable.dog9);
            } else if (randomImage1.equals("dog10")) {
                ex1Image.setImageResource(R.drawable.dog10);
            }
        } else if (randomImage1.contains("object")) {
            if (randomImage1.equals("object1")) {
                ex1Image.setImageResource(R.drawable.object1);
            } else if (randomImage1.equals("object2")) {
                ex1Image.setImageResource(R.drawable.object2);
            } else if (randomImage1.equals("object3")) {
                ex1Image.setImageResource(R.drawable.object3);
            } else if (randomImage1.equals("object4")) {
                ex1Image.setImageResource(R.drawable.object4);
            } else if (randomImage1.equals("object5")) {
                ex1Image.setImageResource(R.drawable.object5);
            } else if (randomImage1.equals("object6")) {
                ex1Image.setImageResource(R.drawable.object6);
            } else if (randomImage1.equals("object7")) {
                ex1Image.setImageResource(R.drawable.object7);
            } else if (randomImage1.equals("object8")) {
                ex1Image.setImageResource(R.drawable.object8);
            } else if (randomImage1.equals("object9")) {
                ex1Image.setImageResource(R.drawable.object9);
            } else if (randomImage1.equals("object10")) {
                ex1Image.setImageResource(R.drawable.object10);
            }
        }
    }

    public void changeImage() {
        if (randomImage2.contains("dog")) {
            if (randomImage2.equals("dog1")) {
                ex1Image.setImageResource(R.drawable.dog1);
            } else if (randomImage2.equals("dog2")) {
                ex1Image.setImageResource(R.drawable.dog2);
            } else if (randomImage2.equals("dog3")) {
                ex1Image.setImageResource(R.drawable.dog3);
            } else if (randomImage2.equals("dog4")) {
                ex1Image.setImageResource(R.drawable.dog4);
            } else if (randomImage2.equals("dog5")) {
                ex1Image.setImageResource(R.drawable.dog5);
            } else if (randomImage2.equals("dog6")) {
                ex1Image.setImageResource(R.drawable.dog6);
            } else if (randomImage2.equals("dog7")) {
                ex1Image.setImageResource(R.drawable.dog7);
            } else if (randomImage2.equals("dog8")) {
                ex1Image.setImageResource(R.drawable.dog8);
            } else if (randomImage2.equals("dog9")) {
                ex1Image.setImageResource(R.drawable.dog9);
            } else if (randomImage2.equals("dog10")) {
                ex1Image.setImageResource(R.drawable.dog10);
            }
        } else if (randomImage2.contains("object")) {
            if (randomImage2.equals("object1")) {
                ex1Image.setImageResource(R.drawable.object1);
            } else if (randomImage2.equals("object2")) {
                ex1Image.setImageResource(R.drawable.object2);
            } else if (randomImage2.equals("object3")) {
                ex1Image.setImageResource(R.drawable.object3);
            } else if (randomImage2.equals("object4")) {
                ex1Image.setImageResource(R.drawable.object4);
            } else if (randomImage2.equals("object5")) {
                ex1Image.setImageResource(R.drawable.object5);
            } else if (randomImage2.equals("object6")) {
                ex1Image.setImageResource(R.drawable.object6);
            } else if (randomImage2.equals("object7")) {
                ex1Image.setImageResource(R.drawable.object7);
            } else if (randomImage2.equals("object8")) {
                ex1Image.setImageResource(R.drawable.object8);
            } else if (randomImage2.equals("object9")) {
                ex1Image.setImageResource(R.drawable.object9);
            } else if (randomImage2.equals("object10")) {
                ex1Image.setImageResource(R.drawable.object10);
            }
        }
    }

    @Override
    public void onBackPressed() {
        dialogShow();
    }

    public void dialogShow() {
        //게임 종료 알림
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("End game");
        builder.setMessage("Would you like to end the game?\n(* Game data will be removed)");
        builder.setPositiveButton("Next Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setNegativeButton("Go Home",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        answerCount = 0;
                        problemNumber = 0;
                        Excercise1Activity.super.onBackPressed();
                    }
                });
        builder.show();
    }

    public void startVibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
}
