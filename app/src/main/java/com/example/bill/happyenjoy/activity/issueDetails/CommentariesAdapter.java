package com.example.bill.happyenjoy.activity.issueDetails;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.model.CommentariesData;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TT on 2017/12/16.
 */

public class CommentariesAdapter extends RecyclerView.Adapter<CommentariesAdapter.ViewHolder> {

    private List<CommentariesData> commentariesDataList;
    private IssueDetails activity;

    public CommentariesAdapter(List<CommentariesData> commentariesDataList,IssueDetails activity){
        this.activity = activity;
        this.commentariesDataList = commentariesDataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView commentaries_touxiang;
        TextView name;
        TextView time;
        TextView neirong;

        public ViewHolder(View view){
            super(view);
            commentaries_touxiang = (CircleImageView)view.findViewById(R.id.commentaries_touxiang);
            name = (TextView)view.findViewById(R.id.name);
            time = (TextView)view.findViewById(R.id.time);
            neirong = (TextView)view.findViewById(R.id.neirong);
        }
    }

    private static String TimeStamp2Date(String timestampString, String formats) {//将Unix时间截转换成能看懂的字符串
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats, Locale.CHINA).format(new java.util.Date(timestamp));
        return date;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_details_commentaries_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentariesData commentariesData = commentariesDataList.get(position);

        String temp = "";//防止头像的url为空
        try{
            temp = commentariesData.getUser1_image();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Glide//加载头像
                .with(activity)
                .load(temp)
                .centerCrop()
                .placeholder(R.drawable.jiazai)
                .into(holder.commentaries_touxiang);

        holder.name.setText(commentariesData.getUser1_flowerName());//名字

        holder.neirong.setText(commentariesData.getContext());//内容

        holder.time.setText(TimeStamp2Date(commentariesData.getCommentTime(),"yyyy-MM-dd HH:mm"));

    }

    @Override
    public int getItemCount() {
        return commentariesDataList.size();
    }

}
