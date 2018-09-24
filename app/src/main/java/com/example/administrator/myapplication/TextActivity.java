package com.example.administrator.myapplication;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.administrator.myapplication.dao.Texts;
import com.example.administrator.myapplication.daomain.NoteDBHelper;
import com.example.administrator.myapplication.fragment.dummy.DummyContent;
import com.example.administrator.myapplication.provide.MyContentProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextActivity extends AppCompatActivity {
    private NoteDBHelper mDbHelper;
    private MyContentProvider provider;
    private String path;
    private String name;
    private  EditText text;
    private String code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mDbHelper = new NoteDBHelper(this);
        provider = new MyContentProvider();
        provider.setNoteDBHelper(mDbHelper);

        text = findViewById(R.id.editText);
        text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        text.setSingleLine(false);
        text.setHorizontallyScrolling(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("新建文本");

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        if (code.equals("old")) {
            path = intent.getStringExtra("path");
            name = intent.getStringExtra("name");
            actionBar.setTitle(name);
            String s = read(path);
            text.setText(s.toCharArray(), 0, s.length());
        }
    }

    //选择菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.text1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.text_save:
                String t = text.getText().toString();
                if (code.equals("new")) {
                    if (write(t,null)) {
                        Uri newUri = ContentUris.withAppendedId(Texts.Text.CONTENT_URI, 0);
                        Date day = new Date();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        ContentValues values = new ContentValues();
                        values.put(Texts.Text.COLUMN_NAME_name, name);
                        values.put(Texts.Text.COLUMN_NAME_path, path);
                        values.put(Texts.Text.COLUMN_NAME_time, df.format(day));
                        if (provider.insert(newUri, values) != null) {
                            Toast.makeText(TextActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                            Cursor c=provider.query(newUri,null,null,null,null);
                            DummyContent m=new  DummyContent(c);
                            MainActivity.f4.all_refresh(m.getITEMS());
                        }
                    }
                }else if(code.equals("old")){
                   if(write(t,path)){
                       Toast.makeText(TextActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                   }
                }
                return true;
        }
     return true;
    }
    public boolean write(String t,String p){
        BufferedOutputStream out=null;
        try {
            if(p==null) {
                File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note");
                if(!f.exists())
                    f.mkdir();
                File f1 = new File(f.getAbsolutePath()+"/text");
                if(!f1.exists())
                    f1.mkdir();
                Date day=new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                path=f1.getAbsolutePath()+"/"+df.format(day)+".txt";
                name=df.format(day)+".txt";
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                out=new BufferedOutputStream(fileOutputStream);

            }
            else {
                FileOutputStream fileOutputStream = new FileOutputStream(p);
                out=new BufferedOutputStream(fileOutputStream);
            }
            try {
                out.write(t.getBytes(StandardCharsets.UTF_8));
            }
            finally {
                if(out!=null)
                    out.close();
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public String read(String p){
        BufferedInputStream in=null;
        StringBuilder stringBuilder=new StringBuilder("");
        try {
            FileInputStream fileInputStream=new FileInputStream(p);
            in=new BufferedInputStream(fileInputStream);
            int c;
            try{
                while ((c=in.read())!=-1) {
                    stringBuilder.append((char)c);
                }
            }
            finally {
                if(in!=null)
                    in.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }
}

