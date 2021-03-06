package com.example.bill.happyenjoy.activity.homePage;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.showimagesdialogdemo.component.ShowImagesDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TT on 2017/11/15.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    List<String> tupianURLs = new ArrayList<>();
    AppCompatActivity activity;

    public ImageAdapter(List<String> tupianURLs,AppCompatActivity activity){
        this.tupianURLs = tupianURLs;
        this.activity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView tupianItem;
        public ViewHolder(View view){
            super(view);
            tupianItem = view.findViewById(R.id.tupian_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_tupian_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String tupianURL = tupianURLs.get(position);
        Glide
                .with(activity)
                .load(tupianURL)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.jiazai)
                .into(holder.tupianItem);
        holder.tupianItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShowImagesDialog(activity,tupianURLs,position).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tupianURLs.size();
    }
}
