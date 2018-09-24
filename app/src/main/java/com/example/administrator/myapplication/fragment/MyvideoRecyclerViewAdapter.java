package com.example.administrator.myapplication.fragment;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.fragment.videoFragment.OnListFragmentInteractionListener;
import com.example.administrator.myapplication.fragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyvideoRecyclerViewAdapter extends RecyclerView.Adapter<MyvideoRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private DummyItem position;

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    private ViewHolder viewHolder;

    public DummyItem getPosition() {
        return position;
    }

    public void setPosition(DummyItem position) {
        this.position = position;
    }
    public MyvideoRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).name);
        holder.mContentView.setText(mValues.get(position).time);
        holder.img.setImageBitmap(getVideoBitmap(mValues.get(position).path));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteractionvideo(holder.mItem);
                }
            }
        });
    }
   public void delete(DummyItem item){
        for(int i=0;i<mValues.size();i++){
            if(item.id==mValues.get(i).id){
                mValues.remove(item);
                notifyItemRemoved(i);
                notifyDataSetChanged();
            }
        }
   }
    public void refresh(List<DummyItem> m){
        mValues.removeAll(mValues);
        mValues.addAll(m);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    //获取视频缩略图
    public static Bitmap getVideoBitmap(String path) {

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            Bitmap frameAtTime = retriever.getFrameAtTime();
            return frameAtTime;
        } catch (Exception e) {
            return null;
        } finally {
            retriever.release();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final View mView;
        public final ImageView img;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            img=view.findViewById(R.id.imageView);
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                setPosition(mItem);
                setViewHolder(this);
        }
    }
}
