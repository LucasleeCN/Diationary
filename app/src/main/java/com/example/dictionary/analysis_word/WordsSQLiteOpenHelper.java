package com.example.dictionary.analysis_word;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class WordsSQLiteOpenHelper extends SQLiteOpenHelper {
    private  String CREATE_MYWORDS = "create table Words(id Integer primary key autoincrement,isChinese text, word_key text,fy text,psE text,psA text,pronA text,pronE text,posAcceptation text,sent text)";
    private  String CREATE_WORDS = "create table MyWords(id Integer primary key autoincrement,isChinese text, word_key text,fy text,psE text,psA text,pronA text,pronE text,posAcceptation text,sent text)";
    public WordsSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MYWORDS);
        Log.d("TAG","数据库创建成功");
        db.execSQL(CREATE_WORDS);
        Log.d("TAG","我的数据库创建成功");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop  table if  exists  Words");
        db.execSQL("drop  table if  exists  MyWords");
        onCreate(db);
    }
}
