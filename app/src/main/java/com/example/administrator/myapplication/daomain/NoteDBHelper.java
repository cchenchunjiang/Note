package com.example.administrator.myapplication.daomain;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.myapplication.dao.Musics;
import com.example.administrator.myapplication.dao.Pictures;
import com.example.administrator.myapplication.dao.Texts;
import com.example.administrator.myapplication.dao.Videos;

import java.io.Serializable;

public class NoteDBHelper extends SQLiteOpenHelper implements Serializable {
    private final static String DATABASE_NAME = "notedb";
    private final static int DATABASE_VERSION = 1;
    private final static String SQL_CREATE_DATABASE_Video = "CREATE TABLE " + Videos.Video.TABLE_NAME+"("
            +Videos.Video.COLUMN_NAME_id+"  INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +Videos.Video.COLUMN_NAME_name+" Text ,"
            +Videos.Video.COLUMN_NAME_path+" Text ,"
            +Videos.Video.COLUMN_NAME_time+" Text )";
    private final static String SQL_CREATE_DATABASE_Picture = "CREATE TABLE " + Pictures.Picture.TABLE_NAME+"("
            + Pictures.Picture.COLUMN_NAME_id+"   INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Pictures.Picture.COLUMN_NAME_name+" Text ,"
            +Pictures.Picture.COLUMN_NAME_path+" Text ,"
            +Pictures.Picture.COLUMN_NAME_time+" Text )";
    private final static String SQL_CREATE_DATABASE_Music = "CREATE TABLE " + Musics.Music.TABLE_NAME+"("
            +Musics.Music.COLUMN_NAME_id+"  INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Musics.Music.COLUMN_NAME_name+" Text ,"
            +Musics.Music.COLUMN_NAME_path+" Text ,"
            +Musics.Music.COLUMN_NAME_time+" Text )";
    private final static String SQL_CREATE_DATABASE_Text = "CREATE TABLE " + Texts.Text.TABLE_NAME+"("
            +Texts.Text.COLUMN_NAME_id+"  INTEGER PRIMARY KEY AUTOINCREMENT,"
            +Texts.Text.COLUMN_NAME_name+" Text ,"
            +Texts.Text.COLUMN_NAME_path+" Text ,"
            +Texts.Text.COLUMN_NAME_time+" Text )";



    private final static String SQL_DELETE_DATABASE_Video= "DROP TABLE IF EXISTS " + Videos.Video.TABLE_NAME;
    private final static String SQL_DELETE_DATABASE_Music= "DROP TABLE IF EXISTS " + Musics.Music.TABLE_NAME;
    private final static String SQL_DELETE_DATABASE_Picture= "DROP TABLE IF EXISTS " + Pictures.Picture.TABLE_NAME;
    private final static String SQL_DELETE_DATABASE_Text= "DROP TABLE IF EXISTS " + Texts.Text.TABLE_NAME;

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE_Video);
        db.execSQL(SQL_CREATE_DATABASE_Music);
        db.execSQL(SQL_CREATE_DATABASE_Picture);
        db.execSQL(SQL_CREATE_DATABASE_Text);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DATABASE_Music);
        db.execSQL(SQL_DELETE_DATABASE_Picture);
        db.execSQL(SQL_DELETE_DATABASE_Text);
        db.execSQL(SQL_DELETE_DATABASE_Video);
        onCreate(db);
    }

}
