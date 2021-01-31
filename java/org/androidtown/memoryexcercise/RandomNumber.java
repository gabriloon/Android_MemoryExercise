package org.androidtown.memoryexcercise;

/**
 * Created by Jaena Jang on 2017-11-28.
 */

public class RandomNumber {

    private int randNum100;
    private int randNum10;
    private int randNum1;

    private int randNum;

    public RandomNumber(){
        randNum100 = (int)(Math.random() * 10);
        randNum10 =  (int)(Math.random() * 10);
        randNum1 =  (int)(Math.random() * 9)+1;
        randNum = randNum100*100 + randNum10 * 10 + randNum1;

    }

    public int getNumber(int stage){

        switch (stage){
            case 1:
                return randNum1;
            case 2:
                return randNum10 *10 + randNum1;
            case 3:
                return randNum;
            default:
                return 0;
        }
    }
}



