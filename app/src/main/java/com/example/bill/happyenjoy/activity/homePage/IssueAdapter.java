package com.example.bill.happyenjoy.activity.homePage;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

    private static final int TYPE_HAVETUPIAN = 1;
    private static final int TYPE_NORMAL = 2;

    private List<IssueDate> issueDates = new ArrayList<>();
    private AppCompatActivity activity;

    public IssueAdapter(List<IssueDate> issueDates,AppCompatActivity activity){
        this.issueDates = issueDates;
        this.activity = activity;
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

        if(holder instanceof TupianViewHolder){
            List<String> tupianURLs = new ArrayList<>();
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
            TupianViewHolder tupianViewHolder = (TupianViewHolder)holder;
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
