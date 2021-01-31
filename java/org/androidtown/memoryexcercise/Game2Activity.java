package org.androidtown.memoryexcercise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class Game2Activity extends AppCompatActivity {
    public ImageButton[] buttonArr = new ImageButton[9];
    public String buttonId[] = {"c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8"};
    TextView textView1, textView2, textView3, textView4, textView5, textView6,PointText;

    public ConstraintLayout buttonLayout;

    Random random;
    BackgroundTask task;
    final int randNum[] = new int[4];
    boolean[] ansCheck = new boolean[4];
    int ansCnt = 0;
    int timerVal;
    int timerCount = 4;
    int playChance = 2;
    int num;
    int currentPoint ;
    int highestPoint;

    static int stageNumber = 0;
    CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);



        Intent intent1 = getIntent();
        num = intent1.getIntExtra("circleValue",1);
        currentPoint = intent1.getIntExtra("currentPoint",0);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(),"ManagerPoint.db",null,1);

        if(dbHelper.isColumnExist("game2"))
            highestPoint= dbHelper.getResult("game2");
        else
            dbHelper.insert("game2",0);

        if(currentPoint > highestPoint) {
            dbHelper.update("2game",currentPoint);
        }
        highestPoint = dbHelper.getResult("2game");





        random = new Random();
        buttonArr[0] = (ImageButton) findViewById(R.id.c0);
        buttonArr[1] = (ImageButton) findViewById(R.id.c1);
        buttonArr[2] = (ImageButton) findViewById(R.id.c2);
        buttonArr[3] = (ImageButton) findViewById(R.id.c3);
        buttonArr[4] = (ImageButton) findViewById(R.id.c4);
        buttonArr[5] = (ImageButton) findViewById(R.id.c5);
        buttonArr[6] = (ImageButton) findViewById(R.id.c6);
        buttonArr[7] = (ImageButton) findViewById(R.id.c7);
        buttonArr[8] = (ImageButton) findViewById(R.id.c8);

        textView1 = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        //textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        PointText = (TextView) findViewById(R.id.pointText);

        buttonLayout = (ConstraintLayout)findViewById(R.id.buttonLayout);



        //문제 수증가
        stageNumber++;

        timerVal = 0;
        timer = new CountDownTimer(5000, 990) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView3.setText("" + (5 - timerVal));
                timerVal++;
            }

            @Override
            public void onFinish() {
                timerVal = 0;
                MemoryTest(dbHelper);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 서로 다른 random number 4개(0 ~ 8)를 찾는다.
        for (int i = 0; i < num; i++) {
            int tmp = random.nextInt(9);
            int cnt = 0;
            for (int j = 0; j < i; j++) {
                if (tmp != randNum[j]) cnt++;
            }

            if (cnt == i) randNum[i] = tmp;
            else i--;
        }

        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
            task.cancel(true);

        task = new BackgroundTask();
        task.execute(0);
    }

    @Override
    protected void onStop() {
        super.onStop();

        task.cancel(true);
        finish(); // 다른 화면으로 넘어갈 때, 종료시킨다.
    }

    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        boolean flag = true;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(Integer... values) {
            // 4개의 버튼을 4번 깜박이기
            for (int i = 0; i < 8; i++) { // n번 깜박이기 위해서 n*2번 post()호출
                if (isCancelled()) break; //*** 이게 없으면 종료에 시간이 걸리므로 back pressed 후 다시 시작할 때 느린 시작 현상
                try {
                    Thread.sleep(1000); // 1초 간격으로
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //깜박이기
                publishProgress();

            }

            //1초 쉬었다가 onPostExecute()로 넘어간다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return values[0];
        }

        protected void onProgressUpdate(Integer... values) {
            PointText.setText("Current point: "+currentPoint + "   HighestPoint: "+highestPoint
                    +"\n\nRequired point for next level: "+ 50*num);
            if (flag) { // 4개의 버튼을 빨간색으로 바꿔준다.
                flag = false;
                for (int i = 0; i < num; i++) {
                    int idx = randNum[i];
                    buttonArr[idx].setImageResource(R.drawable.oval2);
                }
            } else { // 4개의 버튼을 원래의 색으로 바꿔준다.
                flag = true;
                for (int i = 0; i < num; i++) {
                    int idx = randNum[i];
                    buttonArr[idx].setImageResource(R.drawable.oval1);
                }
                timerCount--;
                if (timerCount != 0)
                    textView2.setText("After "+timerCount + " blinks, the problem will be appeared.");


            }

        }

        protected void onPostExecute(Integer result) {
//            for (int i = 0; i < num; i++) {
//                buttonArr[i].setVisibility(View.INVISIBLE);
//            }
            buttonLayout.setVisibility(View.GONE);

            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            PointText.setVisibility(View.GONE);
            textView3.setVisibility(View.VISIBLE);

            //5초 뒤에 기억력 테스트 화면으로 이동
            timer.cancel();
            timer.start();
        }

        protected void onCancelled() {
            super.onCancelled();
        }
    }

    void MemoryTest(final DBHelper dbHelper) {
//        for (int i = 0; i < 9; i++) {
//            buttonArr[i].setVisibility(View.VISIBLE);
//        }
        PointText.setText("Current point: "+currentPoint + "   HighestPoint: "+highestPoint
                +"\n\nRequired point for next level: "+50*num);
        buttonLayout.setVisibility(View.VISIBLE);
        textView1.setVisibility(View.VISIBLE);
        PointText.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.GONE);
        textView1.setText("Find blinking circles");


        // 찾은 깜박이 수 표시
        textView4.setVisibility(View.VISIBLE);
        textView6.setVisibility(View.VISIBLE);

        for (int i = 0; i < num; i++) {
            ansCheck[i] = false;
        }

        ansCnt = 0;
        textView4.setText("Circles found(" + ansCnt + "/" + num +")");

        for (int i = 0; i < 9; i++) {
            final int buttonIdx = i;
            buttonArr[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean isAnswer = false;
                    System.out.println("test : 클릭하면 여기 들어와 : " + isAnswer);
                    for (int j = 0; j < num; j++) {
                        if (buttonIdx == randNum[j]) {
                            if (!ansCheck[j]) {
                                ansCheck[j] = true;
                                buttonArr[buttonIdx].setImageResource(R.drawable.oval2);
                                isAnswer = true;
                                ansCnt++;
                                textView4.setText("Circles found(" + ansCnt +"/" + num +")");
                                break;
                            } else { // 이미 고른 답을 또 눌렀다면
                                isAnswer = true;
                            }
                        }
                    }
                    System.out.println("test : " + isAnswer);
                    if (!isAnswer) { // 고른 것이 답이 아니라면
                        playChance--;
                        startVibrate();
                        if (playChance > 0) {
                            textView6.setText("(" + playChance + " remaining chances)");
                        } else if (playChance == 0)
                            textView6.setText("(Last chance)");
                        else
                        {
                            //게임 종료 알림
                            AlertDialog.Builder builder = new AlertDialog.Builder(Game2Activity.this);
                            builder.setCancelable(false);
                            builder.setTitle("Incorrect!");
                            //builder.setMessage("틀렸습니다.\n(다음문제로 넘어가시겠습니까?)");
                            builder.setPositiveButton("next game",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), Game2Activity.class);
                                            intent.putExtra("circleValue",num);
                                            intent.putExtra("currentPoint",currentPoint);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            startActivity(intent);
                                        }
                                    });
                            builder.setNegativeButton("Go Home",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            stageNumber = 0;
                                            finish();
                                        }
                                    });
                            builder.show();
                        }

                        System.out.println("test : 여기 들어오나?");
                    }

                    if (ansCnt == num) { // 4개의 깜박이를 모두 찾은 경우, -> 다음 게임 시작 or 홈화면 선택.
                        //textView1.setText("Congratulation!");

                        //게임 종료 알림
                        AlertDialog.Builder builder = new AlertDialog.Builder(Game2Activity.this);
                        builder.setCancelable(false);
                        builder.setTitle("Correct!");

                        //builder.setMessage("맞았습니다.\n(다음문제로 넘어가시겠습니까?)");
                        builder.setPositiveButton("next game",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), Game2Activity.class);
                                        intent.putExtra("currentPoint",currentPoint+10);
                                        if(currentPoint <40)
                                            intent.putExtra("circleValue",1);
                                        else if(currentPoint >= 40 && currentPoint <90)
                                            intent.putExtra("circleValue",2);
                                        else if(currentPoint >= 90 && currentPoint < 140)
                                            intent.putExtra("circleValue",3);
                                        else if(currentPoint >= 140)
                                            intent.putExtra("circleValue",4);
                                        else
                                            intent.putExtra("circleValue",4);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        startActivity(intent);
                                    }
                                });
                        builder.setNegativeButton("Go Home",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        stageNumber = 0;
                                        finish();
                                    }
                                });
                        builder.show();

                    }
                }
            });
        }
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
        builder.setPositiveButton("next game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("Go Home",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stageNumber = 0;
                        Game2Activity.super.onBackPressed();
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
