package com.example.administrator.myapplication.provide;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import com.example.administrator.myapplication.dao.Musics;
import com.example.administrator.myapplication.dao.Pictures;
import com.example.administrator.myapplication.dao.Texts;
import com.example.administrator.myapplication.dao.Videos;
import com.example.administrator.myapplication.daomain.NoteDBHelper;



public class MyContentProvider extends ContentProvider {
    private NoteDBHelper mDbHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final static String videos="select id from videos";
    private final static String pictures="select id from pictures";
    private final static String musics="select id from musics";
    private final static String texts="select id from texts";
    static {
        uriMatcher.addURI(Videos.AUTHORITY, Videos.Video.PATH_MULTIPLE, 0);
        uriMatcher.addURI(Videos.AUTHORITY, Musics.Music.PATH_MULTIPLE, 0);
        uriMatcher.addURI(Videos.AUTHORITY, Texts.Text.PATH_MULTIPLE, 0);
        uriMatcher.addURI(Videos.AUTHORITY, Pictures.Picture.PATH_MULTIPLE, 0);
    }
    public MyContentProvider(){}
    public void setNoteDBHelper(NoteDBHelper mDbHelper) {
        this.mDbHelper=mDbHelper;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c=db.rawQuery(videos,null);
        if (c!=null) {
            while (c.moveToNext()) {
                Log.v("int",String.valueOf(c.getInt(0)));
                uriMatcher.addURI(Videos.AUTHORITY, Videos.Video.PATH_MULTIPLE,c.getInt(0));
            }
        }
        c=db.rawQuery(pictures,null);
        if (c!=null) {
            while (c.moveToNext()) {
                uriMatcher.addURI(Videos.AUTHORITY, Pictures.Picture.PATH_MULTIPLE,c.getInt(0));
            }
        }
        c=db.rawQuery(texts,null);
        if (c!=null) {
            while (c.moveToNext()) {
                uriMatcher.addURI(Videos.AUTHORITY, Texts.Text.PATH_MULTIPLE,c.getInt(0));
            }
        }
        c=db.rawQuery(musics,null);
        if (c!=null) {
            while (c.moveToNext()) {
                uriMatcher.addURI(Videos.AUTHORITY, Musics.Music.PATH_MULTIPLE,c.getInt(0));
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tablename=uri.getPathSegments().get(0);
        String whereClause;
        int id=0;
        switch (tablename){
            case "picture":
                whereClause=Pictures.Picture.COLUMN_NAME_id + "=" + uri.getPathSegments().get(1);
                id = db.delete(Pictures.Picture.TABLE_NAME, whereClause, selectionArgs);
                break;
            case "video":
                whereClause=Videos.Video.COLUMN_NAME_id + "=" + uri.getPathSegments().get(1);
                id = db.delete(Videos.Video.TABLE_NAME, whereClause, selectionArgs);
                break;
            case "music":
                whereClause=Musics.Music.COLUMN_NAME_id + "=" + uri.getPathSegments().get(1);
                id = db.delete(Musics.Music.TABLE_NAME, whereClause, selectionArgs);
                break;
            case "text":
                whereClause=Texts.Text.COLUMN_NAME_id + "=" + uri.getPathSegments().get(1);
                id = db.delete(Texts.Text.TABLE_NAME, whereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
        //getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tablename=uri.getPathSegments().get(0);
        long id;
        switch (tablename){
            case "picture":
                id=db.insert(Pictures.Picture.TABLE_NAME,null,values);
                if ( id > 0 ){
                    Uri newUri = ContentUris.withAppendedId(Pictures.Picture.CONTENT_URI, id);
                //getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
          }
            case "video":
                id=db.insert(Videos.Video.TABLE_NAME,null,values);
                if ( id > 0 ){
                    Log.v("uri2",uri.getPath());
                    Uri newUri = ContentUris.withAppendedId(Videos.Video.CONTENT_URI, id);
                    Log.v("uri3",newUri.getPath());

                    //getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
            case "music":
               id=db.insert(Musics.Music.TABLE_NAME,null,values);
                if ( id > 0 ){
                    Uri newUri = ContentUris.withAppendedId(Musics.Music.CONTENT_URI, id);
                   // getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
            case "text":
                 id=db.insert(Texts.Text.TABLE_NAME,null,values);
                if ( id > 0 ){
                    Uri newUri = ContentUris.withAppendedId(Texts.Text.CONTENT_URI, id);
                    //getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
        }
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tablename=uri.getPathSegments().get(0);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (tablename){
            case "picture":
                qb.setTables(Pictures.Picture.TABLE_NAME);
                if(selection!=null)
                    qb.appendWhere(Pictures.Picture.COLUMN_NAME_name + "  like " + "'%"+selection+"%'");
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            case "video":
                qb.setTables(Videos.Video.TABLE_NAME);
                if(selection!=null)
                qb.appendWhere(Videos.Video.COLUMN_NAME_name + "  like " + "'%"+selection+"%'");
                return qb.query(db, projection, null, selectionArgs, null, null, sortOrder);

            case "music":
                qb.setTables(Musics.Music.TABLE_NAME);
                if(selection!=null)
                    qb.appendWhere(Musics.Music.COLUMN_NAME_name + "  like " + "'%"+selection+"%'");
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

            case "text":
                qb.setTables(Texts.Text.TABLE_NAME);
                if(selection!=null)
                    qb.appendWhere(Texts.Text.COLUMN_NAME_name + "  like " + "'%"+selection+"%'");
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String tablename=uri.getPathSegments().get(0);
        String segment = uri.getPathSegments().get(1);
        int count=0;
        switch (tablename){
            case "picture":
                count = db.update(Pictures.Picture.TABLE_NAME, values, Pictures.Picture.COLUMN_NAME_id+"="+segment, selectionArgs);
                break;
            case "video":
                count = db.update(Videos.Video.TABLE_NAME, values, Videos.Video.COLUMN_NAME_id+"="+segment, selectionArgs);
                break;
            case "music":
                count = db.update(Musics.Music.TABLE_NAME, values, Musics.Music.COLUMN_NAME_id+"="+segment, selectionArgs);
                break;
            case "text":
                count = db.update(Texts.Text.TABLE_NAME, values, Texts.Text.COLUMN_NAME_id+"="+segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
        //getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
