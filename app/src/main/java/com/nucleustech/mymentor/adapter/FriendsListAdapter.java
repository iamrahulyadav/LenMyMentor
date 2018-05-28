package com.nucleustech.mymentor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.Student;

import java.util.ArrayList;
/**
 * @author ritwik.rai
 */
public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    private ArrayList<Student> students = new ArrayList<>();

    private Context ctx;


    // Provide a suitable constructor (depends on the kind of dataset)
    public FriendsListAdapter(Context context, ArrayList<Student> items) {

        students = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView image;
        public LinearLayout lyt_parent;
        public TextView tv_unreadcount;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
            tv_unreadcount = (TextView) v.findViewById(R.id.tv_unreadcount);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Student obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friends, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Student c = students.get(position);

        holder.name.setText(students.get(position).name);

       /* Picasso.with(ctx).load(c.getPhoto()).resize(100, 100).transform(new CircleTransform()).into(holder.image);

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);*/

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, c, position);
                    if(students.get(position).unreadMsgCount>0)
                        holder.tv_unreadcount.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (students.get(position).unreadMsgCount > 0) {
            holder.tv_unreadcount.setVisibility(View.VISIBLE);
            holder.tv_unreadcount.setText(""+students.get(position).unreadMsgCount);
        }
        Glide.with(holder.image.getContext()).load(students.get(position).profileImgURL).centerCrop().transform(new CircleTransform(holder.image.getContext())).override(50, 50).into(holder.image);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return students.size();
    }


}