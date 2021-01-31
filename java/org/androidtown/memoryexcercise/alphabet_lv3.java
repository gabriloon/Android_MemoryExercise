package org.androidtown.memoryexcercise;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import java.util.Random;

public class alphabet_lv3 extends AppCompatActivity {

    TextView gameExplain, textCenter, textQuestion, noti, pointText;
    EditText underLine;
    GridLayout ansGrid;
    TextView[] text;
    Random random;
    int[] randNum;
    String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
    "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    String[] number = {"①", "②", "③", "④"};
    String ans = "";

    int timerVal;
    CountDownTimer timer;

    BackgroundTask task;

    Button[] ansBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final DBHelper dbHelper = new DBHelper(getApplicationContext(),"ManagerPoint.db",null,1);
        if(dbHelper.isColumnExist("game5"))
            Alphabet_round.highest = dbHelper.getResult("game5");
        else
            dbHelper.insert("game5",0);

        if(Alphabet_round.current > Alphabet_round.highest) {
            dbHelper.update("game5",Alphabet_round.current);
        }
        Alphabet_round.highest = dbHelper.getResult("game5");

        super.onCreate(savedInstanceState);
        System.out.println("test get in onCreate");
        setContentView(R.layout.activity_alphabet_lv3);

        gameExplain = (TextView) findViewById(R.id.gameExplain);
        textQuestion = (TextView) findViewById(R.id.textQuestion);
        ansGrid = (GridLayout) findViewById(R.id.ansGrid);
        text = new TextView[4];
        text[0] = (TextView) findViewById(R.id.text0);
        text[1] = (TextView) findViewById(R.id.text1);
        text[2] = (TextView) findViewById(R.id.text2);
        text[3] = (TextView) findViewById(R.id.text3);

        textCenter = (TextView) findViewById(R.id.textCenter);
        noti = (TextView)findViewById(R.id.noti);
        pointText = (TextView) findViewById(R.id.pointText);

        random = new Random();
        randNum = new int[4];

        ansBtn = new Button[4];
        ansBtn[0] = (Button) findViewById(R.id.ansBtn0);
        ansBtn[1] = (Button) findViewById(R.id.ansBtn1);
        ansBtn[2] = (Button) findViewById(R.id.ansBtn2);
        ansBtn[3] = (Button) findViewById(R.id.ansBtn3);

        timerVal = 0;
        timer = new CountDownTimer(5000, 990) {
            @Override
            public void onTick(long millisUntilFinished) {
                textCenter.setText("" + (5 - timerVal));
                timerVal++;
            }

            @Override
            public void onFinish() {
                timerVal = 0;
                memoryTest();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("test get in onStart");
        // 4개의 알파벳을 random으로 고른다. 0 ~ 25

        for (int i = 0; i < 4; i++) {
            randNum[i] = random.nextInt(52);
            ans += alphabet[randNum[i]];
        }

        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
            task.cancel(true);

        System.out.println("test end in onStart");
        task = new BackgroundTask();
        task.execute(0);
    }

    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        boolean flag = true;
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(Integer... values) {
            System.out.println("test get in doInBackground");

            // 3개의 알파벳을 차례로 깜박이며 보여주기.
            for (int i = 0; i < 4; i++) {
                if (isCancelled()) break; //*** 이게 없으면 종료에 시간이 걸리므로 back pressed 후 다시 시작할 때 느린 시작 현상
//                try {
//                    Thread.sleep(1000); // 1초 간격으로
//                } catch(InterruptedException e) {
//                    e.printStackTrace();
//                }

                for (int j = 0; j < 6; j++) {
                    if (isCancelled()) break; //***
                    try {
                        Thread.sleep(1000); // 1초 간격으로
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 보여주기
                    publishProgress(i);
                }
            }
            System.out.println("test end in doInBackground");
            // 1초 쉬었다가 onPostExecute()로 넘어간다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return values[0];
        }

        protected void onProgressUpdate(Integer... values) {
            int idx = values[0];

            pointText.setText("Current point: " + Alphabet_round.current +
                    "   HighestPoint: " + Alphabet_round.highest + "\n\nRequired point for next level: 150");
            pointText.setVisibility(View.VISIBLE);

            if (flag) {
                flag = false;
                text[idx].setText(alphabet[randNum[idx]]);
            } else {
                flag = true;
                text[idx].setText("  ");
            }
        }

        protected void onPostExecute(Integer result) {
            gameExplain.setVisibility(View.GONE);
            noti.setVisibility(View.GONE);
            for (int i = 0; i < 4; i++) {
                text[i].setVisibility(View.GONE);
            }

            textCenter.setVisibility(View.VISIBLE);
            //underLine.setVisibility(View.GONE);

            //5초 뒤에 기억력 테스트 화면으로 이동
            timer.cancel();
            timer.start();
        }

        protected void onCancelled() {
        }
    }

    void memoryTest() {
        gameExplain.setVisibility(View.VISIBLE);
        gameExplain.setText("\n" +
                "Respond to\nthe following question");

        pointText.setText("Current point: " + Alphabet_round.current +
                "   HighestPoint: " + Alphabet_round.highest + "\n\nRequired point for next level: 150");
        pointText.setVisibility(View.GONE);

        textCenter.setVisibility(View.GONE);

        String[] candidate = new String[4];
        // 먼저 정답이 아닌 4개의 서로 다른(정답과도 다른) 후보(선택지)를 만든다.
        for (int i = 0; i < 4; i++) {
            String tmp = "";
            for (int j = 0; j < 4; j++) { // 무작위로 알파벳 4개를 골라서 만든다.
                tmp += alphabet[random.nextInt(52)];
            }

            int cnt = 0;
            for (int j = 0; j < i; j++) { // 정답 및 앞의 선택지들과 다른지 체크
                if (!tmp.equals(ans) && !tmp.equals(candidate[j])) cnt++;
            }

            if (cnt == i) candidate[i] = number[i] + " " + tmp; //모두 다르면 확정
            else i--; //아닐 경우 다시...
        }
        // 그리고 4개의 선택지 중 하나의 선택지를 랜덤하게 골라서 정답을 넣는다.
        int ansIdx = random.nextInt(4);
        candidate[ansIdx] = number[ansIdx] + " " + ans;

        for (int i = 0; i < 4; i++) { // 4개의 선택지 setting.
            ansBtn[i].setText(candidate[i]);
        }

        ansGrid.setVisibility(View.VISIBLE);
        textQuestion.setVisibility(View.VISIBLE);
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            final int aIdx = ansIdx;
            ansBtn[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (idx == aIdx) { // 정답을 선택
                        Alphabet_round.round++;
                        Alphabet_round.current = Alphabet_round.round * 10;


                        //게임 종료 알림
                        AlertDialog.Builder builder = new AlertDialog.Builder(alphabet_lv3.this);
                        builder.setCancelable(false);
                        builder.setTitle("Correct!");
                        //builder.setMessage("맞았습니다.\n(다음문제로 넘어가시겠습니까?)");
                        if(Alphabet_round.round < 15) {
                            builder.setPositiveButton("next game",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), alphabet_lv3.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    });
                        }
                        else {
                            builder.setPositiveButton("next game",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), alphabet_lv4.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }
                        builder.setNegativeButton("Go Home",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                                            System.out.println("test on back pressed");
                                            task.cancel(true);
                                            System.out.println("test on back pressed 2");
                                        }
                                        finish();
                                    }
                                });
                        builder.show();
                    } else { // 오답을 선택
                        startVibrate();
                        //게임 종료 알림

                        AlertDialog.Builder builder = new AlertDialog.Builder(alphabet_lv3.this);
                        builder.setCancelable(false);
                        builder.setTitle("Incorrect!");
                        //builder.setMessage("틀렸습니다.\n(다음문제로 넘어가시겠습니까?)");

                        builder.setPositiveButton("next game",
                                new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), alphabet_lv3.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    });

                        builder.setNegativeButton("Go Home",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                                            System.out.println("test on back pressed");
                                            task.cancel(true);
                                            System.out.println("test on back pressed 2");
                                        }
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
    protected void onStop() {
        super.onStop();

        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            System.out.println("test on back pressed");
            task.cancel(true);
            System.out.println("test on back pressed 2");
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

                        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                            System.out.println("test on back pressed");
                            task.cancel(true);
                            System.out.println("test on back pressed 2");
                        }
                        alphabet_lv3.super.onBackPressed();
                    }
                });
        builder.show();
    }

    public void startVibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
}