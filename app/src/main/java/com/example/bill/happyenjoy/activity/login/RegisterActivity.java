package com.example.bill.happyenjoy.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.model.UserDataJson;
import com.example.bill.happyenjoy.networkTools.HttpUtil;
import com.example.bill.happyenjoy.view.ToolBarHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bill on 2017/9/24.
 */
public class RegisterActivity extends BaseActivity {
    private EditText phoneNumber_edit;
    private EditText checkNumber_edit;
    private String phoneNumber;
    private String checkNumber;
    private String finalPassword;
    private String firstPassword;
    private String secondPassword;
    int same = 0;//信号变量，两个密码是否相等
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.white_toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("注册账号");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        phoneNumber_edit = (EditText)findViewById(R.id.register_edit_account);
        final EditText first_password_edit = (EditText)findViewById(R.id.register_edit_password);
        //first_password 定位到密码输入框
        final EditText second_password_edit = (EditText)findViewById(R.id.register_confirm_password);
        //定位确认密码框
        checkNumber_edit = (EditText)findViewById(R.id.register_edit_code);




//给确定按钮添加点击事件
        Button ensure = (Button) findViewById(R.id.register_ensure_button);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先判断密码和确认密码的内容对不对
                Toast.makeText(RegisterActivity.this, "确定", Toast.LENGTH_SHORT).show();
                post_message_http();
                //Toast.makeText(getApplicationContext(),"你点了确定",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(RegisterActivity.this,ChooseCollegeActivity.class);
                //startActivity(intent);
            }
        });

        Button send_code = (Button)findViewById(R.id.register_send_code);
        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumber_edit.getText().toString();


               /*OkHttpClient client = new OkHttpClient();
                RequestBody  body  = new FormBody.Builder()
                        .add("phoneNumber",phoneNumber)
                        .build();

            Request request = new  Request.Builder()
                    .url("http://139.199.202.23/School/public/index.php/index/User/send/")
                    .post(body)
                    .build();
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    Log.d("androixx.cn",result);
                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                Toast.makeText(getApplicationContext(),"你点了发送验证码"+phoneNumber,Toast.LENGTH_SHORT).show();
                post_phoneNumber_http();
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

    private void post_phoneNumber_http() {
        phoneNumber = phoneNumber_edit.getText().toString();
        OkHttpClient mOkHttpClient=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("phoneNumber", phoneNumber)
                .build();
        Request request = new Request.Builder()
                .url("http://139.199.202.23/School/public/index.php/index/User/send/")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


   private void post_message_http(){
       final EditText first_password_edit = (EditText)findViewById(R.id.register_edit_password);
       //first_password 定位到密码输入框
       final EditText second_password_edit = (EditText)findViewById(R.id.register_confirm_password);
       //定位确认密码框
       firstPassword = first_password_edit.getText().toString();
       secondPassword = second_password_edit.getText().toString();
       finalPassword = firstPassword;
       //得到其他参数
       phoneNumber = phoneNumber_edit.getText().toString();
       checkNumber = checkNumber_edit.getText().toString();

       //post
       OkHttpClient client = new OkHttpClient();
       RequestBody body = new FormBody.Builder()
               .add("phoneNumber",phoneNumber)
               .add("password",finalPassword)
               .add("checkNumber",checkNumber)
               .build();
       Request request = new Request.Builder()
               .url("http://139.199.202.23/School/public/index.php/index/User/register/")
               .post(body)
               .build();
       Call call = client.newCall(request);
       call.enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
           }
           @Override
           public void onResponse(Call call, Response response) throws IOException {
               String str = response.body().string();
               Log.i("wangshu", str);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                   }
               });
           }
       });
   }

}


