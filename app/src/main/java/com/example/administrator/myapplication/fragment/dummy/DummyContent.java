package com.example.administrator.myapplication.fragment.dummy;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public  List<DummyItem> ITEMS = new ArrayList<DummyItem>();


   public DummyContent(Cursor c){
       DummyItem itme=null;
       if (c!=null) {
           while (c.moveToNext()) {
              itme=new DummyItem(c.getInt(0),c.getString(1),c.getString(2),c.getString(3));
              ITEMS.add(itme);
           }
       }
   }
   public List<DummyItem> getITEMS(){
       return ITEMS;
   }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final int id;
        public final String name;
        public final String path;
        public final String time;

        public DummyItem(int id,String name, String path, String time) {
            this.id=id;
            this.name=name;
            this.path=path;
            this.time=time;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
