package com.example.administrator.myapplication.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.dao.Pictures;
import com.example.administrator.myapplication.daomain.NoteDBHelper;
import com.example.administrator.myapplication.fragment.dummy.DummyContent;
import com.example.administrator.myapplication.fragment.dummy.DummyContent.DummyItem;
import com.example.administrator.myapplication.provide.MyContentProvider;

import java.io.File;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class pictureFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MypictureRecyclerViewAdapter myview;
    private NoteDBHelper mDbHelper;
    private MyContentProvider provider;

    private DummyItem num;
    private MypictureRecyclerViewAdapter.ViewHolder viewHolder;
    private Uri uri;
    private View  view;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public pictureFragment() {
        mDbHelper=new NoteDBHelper(getContext());
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static pictureFragment newInstance(int columnCount) {
        pictureFragment fragment = new pictureFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper=(NoteDBHelper)  getArguments().getSerializable("db");
        provider=new MyContentProvider();
        provider.setNoteDBHelper(mDbHelper);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_list, container, false);

        mDbHelper=(NoteDBHelper)  getArguments().getSerializable("db");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            String sql="select * from "+ Pictures.Picture.TABLE_NAME;
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor c=db.rawQuery(sql,null);
            DummyContent dummyContent=new DummyContent(c);
            myview=new MypictureRecyclerViewAdapter(dummyContent.getITEMS(), mListener);
            recyclerView.setAdapter(myview);
            registerForContextMenu(recyclerView);
        }
        return view;
    }
    public void all_refresh(List<DummyItem> m){
        myview.refresh(m);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.picture, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取到的是listView里的条目信息


        switch (item.getItemId()) {
            case R.id.picture_delete:
                num=myview.getPosition();
                uri=Uri.parse(Pictures.Picture.CONTENT_URI_STRING + "/" + num.id);
                if(provider.delete(uri,null,null)>0){
                    File f=new File(num.path);
                    f.delete();
                    myview.delete(num);
                }
                break;
            case R.id.picture_rename:
                num=myview.getPosition();
                uri=Uri.parse(Pictures.Picture.CONTENT_URI_STRING + "/" + num.id);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                view=inflater.inflate(R.layout.rename, null);
                EditText t=view.findViewById(R.id.rename);
                t.setText(num.name.toCharArray(),0,num.name.length());
                builder.setView(view)
                        .setTitle("重命名")

                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText t=view.findViewById(R.id.rename);
                                String name=t.getText().toString();
                                ContentValues values = new ContentValues();
                                values.put(Pictures.Picture.COLUMN_NAME_time,num.time);
                                values.put(Pictures.Picture.COLUMN_NAME_path,num.path);
                                values.put(Pictures.Picture.COLUMN_NAME_name,name);

                                if(provider.update(uri,values,null,null)>0){
                                    viewHolder=myview.getViewHolder();
                                    viewHolder.mIdView.setText(name);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                builder.show();
                break;
            default:
                //do nothing
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDbHelper.close();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteractionpicture(DummyItem item);
    }
}
