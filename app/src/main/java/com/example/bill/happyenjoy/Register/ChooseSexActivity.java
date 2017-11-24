package com.example.bill.happyenjoy.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.view.ToolBarHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 雨晴 on 2017/11/22.
 */

public class ChooseSexActivity extends BaseActivity {
    private Button male;
    private Button female;
    private int user_id;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_sex_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.white_toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("选择性别");
        toolbar = toolbarHelper.getToolbar();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Intent GetIntent = getIntent();
        Bundle GetBundle = GetIntent.getExtras();
        //将bundle中key为user_id的值传给本活动中的user_id
        user_id = GetBundle.getInt("user_id");

        male = (Button)findViewById(R.id.male_button);
        female = (Button)findViewById(R.id.female_button);
        male.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                post_male_message();
                Intent intent = new Intent(ChooseSexActivity.this,ChooseCollegeActivity.class);
                //利用bundle传递信息
                Bundle bundle = new Bundle();
                bundle.putInt("user_id",user_id);
                intent.putExtras(bundle);
                //传递完成
                startActivity(intent);
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_female_message();
                Intent intent = new Intent(ChooseSexActivity.this,ChooseCollegeActivity.class);
                //利用bundle传递信息
                Bundle bundle = new Bundle();
                bundle.putInt("user_id",user_id);
                intent.putExtras(bundle);
                //传递完成
                startActivity(intent);
            }
        });


    }





    private void post_male_message(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id",String.valueOf(user_id))
                .add("sex","1")
                .build();
        //Log.d("信息",String.valueOf(user_id));
        Request request =new Request.Builder()
                .url("http://139.199.202.23/School/public/index.php/index/User/changeUserInformation/")
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChooseSexActivity.this, "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                //
                // Log.d("信息",responseData);

            }
        });
    }



    private void post_female_message(){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("",String.valueOf(user_id))
                .add("sex","0")
                .build();
        Request request =new Request.Builder()
                .url("http://139.199.202.23/School/public/index.php/index/User/changeUserInformation/")
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChooseSexActivity.this, "连接失败，请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("信息",responseData);
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
