package com.nucleustech.mymentor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.University;

import java.util.ArrayList;
/**
 * @author ritwik.rai
 */
public class UniversitiesAdapter extends RecyclerView.Adapter<UniversitiesAdapter.ViewHolder> {

    private ArrayList<University> universities = new ArrayList<>();

    private Context ctx;


    // Provide a suitable constructor (depends on the kind of dataset)
    public UniversitiesAdapter(Context context, ArrayList<University> items) {

        universities = items;
        ctx = context;
        Log.e("Size","Size SIze: "+universities.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView collegeName;
        public TextView collegeAddress;

        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            collegeName = (TextView) v.findViewById(R.id.tv_collegeName);
            collegeAddress = (TextView) v.findViewById(R.id.tv_collegeAddress);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, University obj, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    @Override
    public UniversitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_universities, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final University c = universities.get(position);

        holder.collegeName.setText(universities.get(position).universityName);
        holder.collegeAddress.setText(universities.get(position).address);
       /* Picasso.with(ctx).load(c.getPhoto()).resize(100, 100).transform(new CircleTransform()).into(holder.image);

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);*/

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, c, position);
                }
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.e("Size","Size: "+universities.size());
        return universities.size();
    }


}