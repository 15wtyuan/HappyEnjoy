package com.example.bill.happyenjoy.activity.homePage;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.model.IssueDate;
import com.example.bill.happyenjoy.model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TT on 2017/11/3.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder>{

    private static final int TYPE_HAVETUPIAN = 1;
    private static final int TYPE_NORMAL = 2;

    private List<IssueDate> issueDates = new ArrayList<>();
    private AppCompatActivity activity;
    private List<UserData> userDatas = new ArrayList<>();

    public IssueAdapter(List<IssueDate> issueDates,List<UserData> userDatas,AppCompatActivity activity){
        this.issueDates = issueDates;
        this.activity = activity;
        this.userDatas = userDatas;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView touxiang;
        TextView user_name;
        TextView time;
        TextView kind_name;
        ImageView kind;
        TextView biaoti;
        TextView neirong;
        TextView pinlun_num;
        TextView zan_num;
        ImageView zan;
        TextView price;

        public ViewHolder(View view){
            super(view);
            touxiang = (CircleImageView)view.findViewById(R.id.touxiang);
            user_name = (TextView)view.findViewById(R.id.user_name);
            time = (TextView)view.findViewById(R.id.time);
            kind_name = (TextView)view.findViewById(R.id.kind_name);
            kind = (ImageView)view.findViewById(R.id.kind);
            biaoti = (TextView)view.findViewById(R.id.biaoti);
            neirong = (TextView)view.findViewById(R.id.neirong);
            pinlun_num = (TextView)view.findViewById(R.id.pinlun_num);
            zan_num = (TextView)view.findViewById(R.id.zan_num);
            zan = (ImageView)view.findViewById(R.id.zan);
            price = (TextView)view.findViewById(R.id.price);
        }
    }

    public class NormalViewHolder extends ViewHolder{

        public NormalViewHolder(View view){
            super(view);
        }
    }

    public class TupianViewHolder extends ViewHolder{

        RecyclerView tupian;
        public TupianViewHolder(View view){
            super(view);
            tupian = view.findViewById(R.id.tupian);
        }
    }

    @Override
    public int getItemViewType(int position) {
        IssueDate issueDate = issueDates.get(position);
        if (issueDate.getPicture1().equals("")||issueDate.getPicture1()==null){
            return TYPE_NORMAL;
        }else {
            return TYPE_HAVETUPIAN;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HAVETUPIAN:
                viewHolder = new TupianViewHolder(inflater.inflate(R.layout.issue_have_tupian, parent, false));
                break;
            case TYPE_NORMAL:
                viewHolder = new NormalViewHolder(inflater.inflate(R.layout.issue, parent, false));
                break;
        }
        return viewHolder;
    }
    public static String TimeStamp2Date(String timestampString, String formats) {//将Unix时间截转换成能看懂的字符串
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats, Locale.CHINA).format(new java.util.Date(timestamp));
        return date;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IssueDate issueDate = issueDates.get(position);
        UserData userData = userDatas.get(position);
        holder.biaoti.setText(issueDate.getTitle());
        holder.neirong.setText(issueDate.getBrief());
        holder.price.setText(issueDate.getPrice());
        holder.zan_num.setText(Integer.toString(issueDate.getZan()));
        holder.pinlun_num.setText(Integer.toString(issueDate.getPingLun()));
        //holder.kind_name.setText(issueDate.getLabel());
        holder.time.setText(TimeStamp2Date(issueDate.getIssueTime(),"yyyy-MM-dd HH:mm:ss"));

        String temp = "";//防止头像的url为空
        try{
            temp = userData.getImage();
        }catch (Exception e) {
            e.printStackTrace();
        }
        Glide
                .with(activity)
                .load(temp)
                .centerCrop()
                .placeholder(R.drawable.jiazai)
                .into(holder.touxiang);

        holder.user_name.setText(userData.getFlowerName());//设置标签
        if (issueDate.getLabel().equals("11")){
            holder.kind_name.setText("拼车");
            holder.kind.setImageResource(R.mipmap.pinche);
        }else if (issueDate.getLabel().equals("12")){
            holder.kind_name.setText("跑腿");
            holder.kind.setImageResource(R.mipmap.paotui);
        }else if (issueDate.getLabel().equals("13")){
            holder.kind_name.setText("寻物");
            holder.kind.setImageResource(R.mipmap.xunwu);
        }else if (issueDate.getLabel().equals("14")){
            holder.kind_name.setText("其他");
            holder.kind.setImageResource(R.mipmap.qita);
        }else if (issueDate.getLabel().equals("21")){
            holder.kind_name.setText("英语");
            holder.kind.setImageResource(R.mipmap.yingyu);
        }else if (issueDate.getLabel().equals("22")){
            holder.kind_name.setText("社科");
            holder.kind.setImageResource(R.mipmap.sheke);
        }else if (issueDate.getLabel().equals("31")){
            holder.kind_name.setText("数码");
            holder.kind.setImageResource(R.mipmap.shuma);
        }else if (issueDate.getLabel().equals("32")){
            holder.kind_name.setText("居家");
            holder.kind.setImageResource(R.mipmap.jujia);
        }else if (issueDate.getLabel().equals("33")){
            holder.kind_name.setText("图书");
            holder.kind.setImageResource(R.mipmap.tushu);
        }else if (issueDate.getLabel().equals("34")){
            holder.kind_name.setText("其他");
            holder.kind.setImageResource(R.mipmap.xianzhi_qita);
        }


        if(holder instanceof TupianViewHolder){//设置图片的列表
            final List<String> tupianURLs = new ArrayList<>();
            if (!issueDate.getPicture1().equals("")){
                tupianURLs.add(issueDate.getPicture1());
            }
            if (!issueDate.getPicture2().equals("")){
                tupianURLs.add(issueDate.getPicture2());
            }
            if (!issueDate.getPicture3().equals("")){
                tupianURLs.add(issueDate.getPicture3());
            }
            if (!issueDate.getPicture4().equals("")){
                tupianURLs.add(issueDate.getPicture4());
            }
            if (!issueDate.getPicture5().equals("")){
                tupianURLs.add(issueDate.getPicture5());
            }
            if (!issueDate.getPicture6().equals("")){
                tupianURLs.add(issueDate.getPicture6());
            }
            if (!issueDate.getPicture7().equals("")){
                tupianURLs.add(issueDate.getPicture7());
            }
            if (!issueDate.getPicture8().equals("")){
                tupianURLs.add(issueDate.getPicture8());
            }
            if (!issueDate.getPicture9().equals("")){
                tupianURLs.add(issueDate.getPicture9());
            }
            final TupianViewHolder tupianViewHolder = (TupianViewHolder)holder;
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            tupianViewHolder.tupian.setLayoutManager(layoutManager);
            ImageAdapter imageAdapter = new ImageAdapter(tupianURLs,activity);
            tupianViewHolder.tupian.setAdapter(imageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return issueDates.size();
    }
}
