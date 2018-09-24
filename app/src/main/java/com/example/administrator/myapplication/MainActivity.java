package com.example.administrator.myapplication;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import android.widget.Toast;

import com.example.administrator.myapplication.ViewAdapter.BottomViewAdapter;
import com.example.administrator.myapplication.ViewAdapter.PicturetActivity;
import com.example.administrator.myapplication.dao.Musics;
import com.example.administrator.myapplication.dao.Pictures;
import com.example.administrator.myapplication.dao.Texts;
import com.example.administrator.myapplication.dao.Videos;
import com.example.administrator.myapplication.daomain.NoteDBHelper;
import com.example.administrator.myapplication.fragment.dummy.DummyContent;
import com.example.administrator.myapplication.fragment.musicFragment;
import com.example.administrator.myapplication.fragment.pictureFragment;
import com.example.administrator.myapplication.fragment.textFragment;
import com.example.administrator.myapplication.fragment.videoFragment;
import com.example.administrator.myapplication.provide.MyContentProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MainActivity extends AppCompatActivity implements videoFragment.OnListFragmentInteractionListener,
        musicFragment.OnListFragmentInteractionListener,textFragment.OnListFragmentInteractionListener,pictureFragment.OnListFragmentInteractionListener
{


    private ViewPager viewPager;
    private NoteDBHelper mDbHelper;
    private MyContentProvider provider;
    private String path;//文件路径
    private String name;//文件名
    private View view;
    private final videoFragment  f1= new videoFragment();
    private final pictureFragment f2 =new pictureFragment();
    private final musicFragment f3= new musicFragment();
    public static final textFragment f4= new textFragment();
    private int now;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_video:
                    now=0;
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_picture:
                    now=1;
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_more:
                   // pop.showMoreWindow(parent);
                    showPopwindow();
                    return true;
                case R.id.navigation_music:
                    now=2;
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_text:
                    now=3;
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper=new NoteDBHelper(this);
        provider=new MyContentProvider();
        provider.setNoteDBHelper(mDbHelper);
        now=0;

         findViewById(R.id.message);
        viewPager=findViewById(R.id.viewpager);
        //viewpage
        Bundle bundle = new Bundle();
        bundle.putSerializable("db",mDbHelper);

        List<Fragment> list_fragment = new ArrayList<>(4);
        f1.setArguments(bundle);
        f2.setArguments(bundle);
        f3.setArguments(bundle);
        f4.setArguments(bundle);
        list_fragment.add(f1);
        list_fragment.add(f2);
        list_fragment.add(f3);
        list_fragment.add(f4);
        BottomViewAdapter adapter = new BottomViewAdapter(getSupportFragmentManager(), list_fragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);




        //底部
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(1);//文字可见
        navigation.setItemHorizontalTranslationEnabled(false);

    }


    //新建弹出菜单
    private void showPopwindow() {
         View parent =  this.findViewById(R.id.navigation);
        View popView = View.inflate(this, R.layout.pop, null);

        ImageView t1=popView.findViewById(R.id.img_video);
        ImageView t2=popView.findViewById(R.id.img_picture);
        ImageView t3=popView.findViewById(R.id.img_music);
        ImageView t4=popView.findViewById(R.id.img_text);

        int width = getResources().getDisplayMetrics().widthPixels;

        final PopupWindow popWindow = new PopupWindow(popView,width,100);
       // popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);// 设置同意在外点击消失

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_video:
                        dispatchTakeVideoIntent();
                        break;
                    case R.id.img_picture:
                        dispatchTakePictureIntent();
                        break;
                    case R.id.img_music:
                        dispatchTakeMusicIntent();
                        break;
                    case R.id.img_text:
                        Intent taketotext=new Intent(MainActivity.this,TextActivity.class);
                        taketotext.putExtra("code","new");

                        startActivity(taketotext);
                        break;
                }
                popWindow.dismiss();
            }
        };
        t1.setOnClickListener(listener);
        t2.setOnClickListener(listener);
        t3.setOnClickListener(listener);
        t4.setOnClickListener(listener);


        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);

        popWindow.showAsDropDown(parent, 0, -60);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.all:
               if(now==0){
                   Uri newUri = ContentUris.withAppendedId(Videos.Video.CONTENT_URI, 0);
                   Cursor c=provider.query(newUri,null,null,null);
                   DummyContent m=new  DummyContent(c);
                   f1.all_refresh(m.getITEMS());
               }else if(now==1){
                   Uri newUri = ContentUris.withAppendedId(Pictures.Picture.CONTENT_URI, 0);
                   Cursor c=provider.query(newUri,null,null,null);
                   DummyContent m=new  DummyContent(c);
                   f2.all_refresh(m.getITEMS());
               }else if(now==2){
                   Uri newUri = ContentUris.withAppendedId(Musics.Music.CONTENT_URI, 0);
                   Cursor c=provider.query(newUri,null,null,null);
                   DummyContent m=new  DummyContent(c);
                   f3.all_refresh(m.getITEMS());
               }else if(now==3){
                   Uri newUri = ContentUris.withAppendedId(Texts.Text.CONTENT_URI, 0);
                   Cursor c=provider.query(newUri,null,null,null);
                   DummyContent m=new  DummyContent(c);
                   f4.all_refresh(m.getITEMS());
               }
               break;
           case R.id.seek:
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               LayoutInflater inflater = getLayoutInflater();
               view=inflater.inflate(R.layout.rename, null);
               EditText t=view.findViewById(R.id.rename);
               builder.setView(view)
                       .setTitle("查找")
                       .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int id) {
                               EditText t=view.findViewById(R.id.rename);
                               String name=t.getText().toString();
                               if(now==0){
                                   Uri newUri = ContentUris.withAppendedId(Videos.Video.CONTENT_URI, 0);
                                   Cursor c=provider.query(newUri,null,name,null,null);
                                   DummyContent m=new  DummyContent(c);
                                   f1.all_refresh(m.getITEMS());
                               }else if(now==1){
                                   Uri newUri = ContentUris.withAppendedId(Pictures.Picture.CONTENT_URI, 0);
                                   Cursor c=provider.query(newUri,null,name,null,null);
                                   DummyContent m=new  DummyContent(c);
                                   f2.all_refresh(m.getITEMS());
                               }else if(now==2){
                                   Uri newUri = ContentUris.withAppendedId(Musics.Music.CONTENT_URI, 0);
                                   Cursor c=provider.query(newUri,null,name,null,null);
                                   DummyContent m=new  DummyContent(c);
                                   f3.all_refresh(m.getITEMS());
                               }else if(now==3){
                                   Uri newUri = ContentUris.withAppendedId(Texts.Text.CONTENT_URI, 0);
                                   Cursor c=provider.query(newUri,null,name,null,null);
                                   DummyContent m=new  DummyContent(c);
                                   f4.all_refresh(m.getITEMS());
                               }
                           }
                       })
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });

               builder.show();
           break;
           case R.id.help:
               Toast.makeText(this,"这是帮助",Toast.LENGTH_LONG).show();
       }
        return super.onOptionsItemSelected(item);
    }

    //选择菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    static final int REQUEST_MUSIC_CAPTURE = 3;
    //  结果处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri newUri = ContentUris.withAppendedId(Pictures.Picture.CONTENT_URI, 0);
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ContentValues values = new ContentValues();
            values.put(Pictures.Picture.COLUMN_NAME_name,name);
            values.put(Pictures.Picture.COLUMN_NAME_path,path);
            values.put(Pictures.Picture.COLUMN_NAME_time,df.format(day));
            provider.insert(newUri,values);
            Cursor c=provider.query(newUri,null,null,null,null);
            DummyContent m=new  DummyContent(c);
            f2.all_refresh(m.getITEMS());
        }

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri newUri = ContentUris.withAppendedId(Videos.Video.CONTENT_URI, 0);
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ContentValues values = new ContentValues();
            values.put(Videos.Video.COLUMN_NAME_name,name);
            values.put(Videos.Video.COLUMN_NAME_path,path);
            values.put(Videos.Video.COLUMN_NAME_time,df.format(day));
            provider.insert(newUri,values);
            Cursor c=provider.query(newUri,null,null,null,null);
            DummyContent m=new  DummyContent(c);
            f1.all_refresh(m.getITEMS());
        }
        if((requestCode == REQUEST_IMAGE_CAPTURE ||requestCode == REQUEST_VIDEO_CAPTURE)&& resultCode == RESULT_CANCELED){
            File f=new File(path);
            f.delete();
        }
        if(REQUEST_MUSIC_CAPTURE==requestCode&& resultCode == RESULT_OK){
            Toast.makeText(this,"这是帮助",Toast.LENGTH_LONG).show();
        }
    }
   //拍照
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note");
            if(!f.exists())
                f.mkdir();
            File f1 = new File(f.getAbsolutePath()+"/picture");
            if(!f1.exists())
                f1.mkdir();

            File image = File.createTempFile("ppp", ".jpg", f1);
            path=image.getAbsolutePath();
            name=image.getName();

            Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "cn.edu.bistu.cs.se.noteapp.fileprovider", image);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        {
            File f=new File(path);
            f.delete();
        }
    }
    //拍摄
    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {
            File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note");
            if(!f.exists())
                f.mkdir();
            File f1 = new File(f.getAbsolutePath()+"/video");
            if(!f1.exists())
                f1.mkdir();

            File video = File.createTempFile("vvvv", ".mp4", f1);
            path=video.getAbsolutePath();
            name=video.getName();

            Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "cn.edu.bistu.cs.se.noteapp.fileprovider", video);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
        {
            File f=new File(path);
            f.delete();
        }
    }
    //录音
    private void dispatchTakeMusicIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        try {
            File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Note");
            if(!f.exists())
                f.mkdir();
            File f1 = new File(f.getAbsolutePath()+"/music");
            if(!f1.exists())
                f1.mkdir();

            File video = File.createTempFile("mmm", ".amr", f1);
            path=video.getAbsolutePath();
            name=video.getName();

            Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "cn.edu.bistu.cs.se.noteapp.fileprovider", video);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_MUSIC_CAPTURE);
        }
        else
        {
            File f=new File(path);
            f.delete();
        }
    }
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
    @Override
    public void onListFragmentInteractionpicture(DummyContent.DummyItem item) {
        Intent taketovideo=new Intent(MainActivity.this, PicturetActivity.class);
        taketovideo.putExtra("path",item.path);
        startActivity(taketovideo);
    }
    @Override
    public void onListFragmentInteractionvideo(DummyContent.DummyItem item) {
           Intent taketovideo=new Intent(MainActivity.this,PLVideoTextureActivity.class);
           taketovideo.putExtra("path",item.path);
           startActivity(taketovideo);
    }
    @Override
    public void onListFragmentInteractiontext(DummyContent.DummyItem item) {
        Intent taketotext=new Intent(MainActivity.this,TextActivity.class);
        taketotext.putExtra("code","old");
        taketotext.putExtra("path",item.path);
        taketotext.putExtra("name",item.name);
        startActivity(taketotext);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }
}


