package com.example.sudoku.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sudoku_scores.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_TIME = "time";

    // SQL-запрос для создания таблицы
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_SCORES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_COUNTRY + " TEXT, " +
                    COLUMN_TIME + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы при первом запуске приложения
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление схемы базы данных при изменении версии
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    // Метод для добавления нового результата
    public long addScore(String username, String country, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_COUNTRY, country);
        values.put(COLUMN_TIME, time);

        // Вставка записи и закрытие подключения
        long result = db.insert(TABLE_SCORES, null, values);
        db.close();

        return result;
    }

    public Cursor getAllScores() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SCORES +
                " ORDER BY (" +
                "substr('00:00:00', 1, 8 - length(" + COLUMN_TIME + ")) || " + COLUMN_TIME +
                ") ASC";

        return db.rawQuery(query, null);
    }


    public Cursor getPlayerDetails(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_SCORES,
                new String[]{COLUMN_USERNAME, COLUMN_COUNTRY, COLUMN_TIME},
                COLUMN_USERNAME + " = ?",
                new String[]{username},
                null, null, null);
    }

}
