package com.example.bill.happyenjoy.activity.login;

/**
 * Created by bill on 2017/10/6.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.view.ToolBarHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChooseCollegeActivity extends AppCompatActivity
{
    // 列表数据
    private String[] province = new String[] { "请选择", "广东",
            "广东", "广东", };
    private String[] city = new String[] { "请选择", "汕头",
            "汕头", "汕头", };
    private String[] college = new  String[]{"请选择","汕头大学","汕头大学","汕头大学","汕头大学"};

    // 点击此文本出现下拉popWindow
    private TextView provinceText;
    private TextView cityText;
    private TextView collegeText;
    private ImageButton chooseProvince;
    private ImageButton chooseCity;
    private ImageButton chooseCollege;
    private Button ensure_button;
    public String test;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_college_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.white_toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("所在校区");
        toolbar = toolbarHelper.getToolbar();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Intent GetIntent = getIntent();
        Bundle GetBundle = GetIntent.getExtras();
        //将bundle中key为user_id的值传给本活动中的user_id
        user_id = GetBundle.getInt("user_id");



        //给确定按钮添加点击事件
        ensure_button = (Button)findViewById(R.id.choose_college_ensure_button);
        ensure_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                postCollageHttp();
                Toast.makeText(ChooseCollegeActivity.this,test, Toast.LENGTH_SHORT).show();
                Toast.makeText(ChooseCollegeActivity.this, "请返回登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChooseCollegeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        provinceText = (TextView) findViewById(R.id.choose_province_text);
        // 初始化显示
        provinceText.setText(province[0]);

        cityText = (TextView) findViewById(R.id.choose_city_text);
        cityText.setText(city[0]);

        collegeText = (TextView)findViewById(R.id.choose_college_text);
        collegeText.setText(college[0]);

        chooseProvince = (ImageButton)findViewById(R.id.choose_province_button);
        chooseCity  = (ImageButton)findViewById(R.id.choose_city_button);
        chooseCollege = (ImageButton)findViewById(R.id.choose_college_button);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //设置province_popWindow的宽度，这几个数字是根据布局中的textView权重得出的4.2表示这一行的总权重，3.1表示textView的权重
        int with = (int) ((540 ) );

        // 找到需要填充到pop的布局
        View view = LayoutInflater.from(this).inflate(R.layout.province_list, null);
        // 根据此布局建立pop
        final PopupWindow province_popWindow = new PopupWindow(view);
        // <<<<<<<<<<<<<<<<<<<极其重要>>>>>>>>>>>>>>>>>>>>>
        province_popWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // province_popWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        province_popWindow.setWidth(with);

        //这样设置pop才可以缩回去
        province_popWindow.setBackgroundDrawable(new BitmapDrawable());
        province_popWindow.setOutsideTouchable(true);
        province_popWindow.setFocusable(true);




        // 找到需要填充到pop的布局
        View view2 = LayoutInflater.from(this).inflate(R.layout.city_list, null);
        // 根据此布局建立pop
        final PopupWindow city_popWindow = new PopupWindow(view2);
        // <<<<<<<<<<<<<<<<<<<极其重要>>>>>>>>>>>>>>>>>>>>
        city_popWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // city_popWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        city_popWindow.setWidth(with);

        //这样设置pop才可以缩回去
        city_popWindow.setBackgroundDrawable(new BitmapDrawable());
        city_popWindow.setOutsideTouchable(true);
        city_popWindow.setFocusable(true);

        View view3 = LayoutInflater.from(this).inflate(R.layout.college_list, null);
        final PopupWindow college_popWindow = new PopupWindow(view3);
        college_popWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // college_popWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        college_popWindow.setWidth(with);
        college_popWindow.setBackgroundDrawable(new BitmapDrawable());
        college_popWindow.setOutsideTouchable(true);
        college_popWindow.setFocusable(true);



        // 填充此布局上的列表
        ListView province_listView = (ListView) view.findViewById(R.id.province_lv);
        ArrayAdapter<String> province_adapter = new ArrayAdapter<>(this,
                R.layout.list_content, province);
        province_listView.setAdapter(province_adapter);
        // 填充此布局上的列表
        ListView city_listView = (ListView) view2.findViewById(R.id.city_lv);
        ArrayAdapter<String> city_adapter = new ArrayAdapter<>(this,
                R.layout.list_content2, city);
        city_listView.setAdapter(city_adapter);

        ListView college_listView = (ListView) view3.findViewById(R.id.college_lv);
        ArrayAdapter<String> college_adapter = new ArrayAdapter<>(this,
                R.layout.list_content3, college);
        college_listView.setAdapter(college_adapter);




        // 当listView受到点击时替换provinceText当前显示文本
        province_listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                provinceText.setText(province[arg2]);
                province_popWindow.dismiss();
            }
        });

        city_listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                cityText.setText(city[arg2]);
                city_popWindow.dismiss();
            }
        });



        college_listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                collegeText.setText(college[arg2]);
                college_popWindow.dismiss();
            }
        });

        // 当provinceText受到点击时显示pop
        provinceText.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                province_popWindow.showAsDropDown(v);
            }
        });

        chooseProvince.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                province_popWindow.showAsDropDown(v);
            }
        });



        cityText.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                city_popWindow.showAsDropDown(v);
            }
        });

        chooseCity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                city_popWindow.showAsDropDown(v);
            }
        });



        collegeText.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                college_popWindow.showAsDropDown(v);
            }
        });
        chooseCollege.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                college_popWindow.showAsDropDown(v);
            }
        });

    }


    private void postCollageHttp(){
    String provincePost = provinceText.getText().toString();
    String cityPost = cityText.getText().toString();
    String collagePost = collegeText.getText().toString();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("id",String.valueOf(user_id))
                .add("province",provincePost)
                .add("city",cityPost)
                .add("university",collagePost)
                .build();
        Log.d("信息",provincePost);
        test = provincePost;
        Request request = new Request.Builder()
                .url("http://139.199.202.23/School/public/index.php/index/User/changeUserInformation/")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
               // Log.d("信息",responseData);

            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm != null && fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}