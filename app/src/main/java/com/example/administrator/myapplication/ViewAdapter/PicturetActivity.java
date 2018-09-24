package com.example.administrator.myapplication.ViewAdapter;

import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;

import java.io.File;

public class PicturetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturet);
        String path=getIntent().getStringExtra("path");
        File file=new File(path);
        Uri photoURI = FileProvider.getUriForFile(PicturetActivity.this, "cn.edu.bistu.cs.se.noteapp.fileprovider",file);
        ImageView img=findViewById(R.id.imageView2);
        img.setImageURI(photoURI);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
    }
}
