package com.example.administrator.myapplication.dao;

import android.net.Uri;
import android.provider.BaseColumns;

public class Texts {
    public static final String AUTHORITY = "cn.edu.bistu.cs.se.noteprovider";//URI授权者
    public Texts() {  }
    public static abstract class Text implements BaseColumns {
        public static final String TABLE_NAME="texts";//表名
        public static final String COLUMN_NAME_id="id";//列：id
        public static final String COLUMN_NAME_name="name";//列：文档文件名
        public static final String COLUMN_NAME_path="path";//列：文档文件路径
        public static final String COLUMN_NAME_time="time";//列：文档创建时间
        public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
        public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
        public static final String MINE_ITEM = "vnd.bistu.cs.se.text";
        public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
        public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;
        public static final String PATH_MULTIPLE = "text";//多条数据的路径
        public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    }
}
