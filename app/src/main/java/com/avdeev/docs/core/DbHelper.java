package com.avdeev.docs.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {

        super(context, "DocDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE User (token text, apiPath text)");
        db.execSQL("CREATE TABLE ApiHistory (apiPath text)");
        db.execSQL("CREATE TABLE DocIn (id text primary key, title text, author text, type text, number text, updated_at NUMERIC)");
        db.execSQL("CREATE TABLE DocOut (id text primary key, title text, author text, type text, number text, updated_at NUMERIC)");
        db.execSQL("CREATE TABLE DocInner (id text primary key, title text, author text, type text, number text, updated_at NUMERIC)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
