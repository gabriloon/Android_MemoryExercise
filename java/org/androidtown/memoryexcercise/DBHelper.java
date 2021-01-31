package org.androidtown.memoryexcercise;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE ManagerPoint (Game TEXT PRIMARY KEY, highestPoint INT);");
/*
        this.insert("game1",0);
        this.insert("game2",0);
        this.insert("game3",0);
        this.insert("game4",0);
        this.insert("game5",0);
      */
    }


    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String game, int point) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO ManagerPoint VALUES('" + game + "', " + point  + ");");
        db.close();
    }
    public void update(String game,int point) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE ManagerPoint SET highestPoint=" + point + " WHERE Game='" + game+ "';");
        db.close();
    }
    public void delete(String game) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM ManagerPoint WHERE Game='" + game + "';");
        db.close();
    }
    public int getResult(String game) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT highestPoint FROM ManagerPoint Where Game ='"+ game+"';", null);
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;

    }
    public String getAllResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM ManagerPoint", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    +" "
                    + cursor.getString(1)
                    +" ";
        }
        return result;
    }
    public boolean isTableExist() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE name='ManagerPoint'",null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 1)
            return true;
        else
            return false;
    }

    public boolean isColumnExist(String game){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EXISTS (SELECT Game FROM ManagerPoint WHERE Game = '"+game+"');",null);
        cursor.moveToFirst();
        if(cursor.getInt(0)== 1)
            return true;
        else
            return false;
    }

    public String getGame(String game){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT Game FROM ManagerPoint Where Game = game", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        return result;
    }
}


