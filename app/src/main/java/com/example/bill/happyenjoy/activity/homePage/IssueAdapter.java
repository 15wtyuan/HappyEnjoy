package com.example.bill.happyenjoy.activity.homePage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.model.IssueDate;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TT on 2017/11/3.
 */

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder>{

    private static final int TYPE_PINCHE = 1;
    private static final int TYPE_PAOTUI = 2;
    private static final int TYPE_XUNWU = 3;
    private static final int TYPE_QITA = 4;
    private static final int TYPE_YINGYU = 5;
    private static final int TYPE_SHEKE = 6;
    private static final int TYPE_SHUMA = 7;
    private static final int TYPE_JUJIA = 8;
    private static final int TYPE_TUSHU = 9;
    private static final int TYPE_XIANZHI_QITA = 10;

    private List<IssueDate> issueDates = new ArrayList<>();

    public IssueAdapter(List<IssueDate> issueDates){
        this.issueDates = issueDates;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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

    @Override
    public int getItemViewType(int position) {
        IssueDate issueDate = issueDates.get(position);

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IssueDate issueDate = issueDates.get(position);
        holder.biaoti.setText(issueDate.getTitle());
        holder.neirong.setText(issueDate.getBrief());
        holder.price.setText(issueDate.getPrice());
        holder.zan_num.setText(Integer.toString(issueDate.getZan()));
        holder.pinlun_num.setText(Integer.toString(issueDate.getPingLun()));
        holder.kind_name.setText(issueDate.getLabel());
        holder.time.setText(issueDate.getIssueTime());
    }

    @Override
    public int getItemCount() {
        return issueDates.size();
    }
}
