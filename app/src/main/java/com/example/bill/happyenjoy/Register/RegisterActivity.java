package com.example.bill.happyenjoy.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.model.CheckNumberJson;
import com.example.bill.happyenjoy.model.RegisterResponseData;
import com.example.bill.happyenjoy.model.RegisterResponseJson;
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
 * Created by bill on 2017/9/24.
 */
public class RegisterActivity extends BaseActivity {
    private EditText phoneNumber_edit;
    private EditText checkNumber_edit;
    private String phoneNumber;

    private String firstPassword;
    private String secondPassword;
    private int user_id;//获取到的userId，要将其传递给下一个活动。

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
                //首先判断密码和确认密码的内容对不对 firstPassword = first_password_edit.getText().toString();
                secondPassword = second_password_edit.getText().toString();
                firstPassword = first_password_edit.getText().toString();
                if (firstPassword.equals(secondPassword)&&firstPassword!=null&&firstPassword!=""
                        &&secondPassword!=null&&secondPassword!="") {
                    same=1;
                    post_message_http();
                    same = 0;

                }else{
                    Toast.makeText(RegisterActivity.this, "密码输入不一致或为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button send_code = (Button)findViewById(R.id.register_send_code);
        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumber_edit.getText().toString();
                //Toast.makeText(getApplicationContext(),"你点了发送验证码"+phoneNumber,Toast.LENGTH_SHORT).show();
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
               final String responseData = response.body().string();
                parseCheckNumberJson(responseData);
            }
        });
    };

    //解析发送验证码的返回信息
    private void parseCheckNumberJson( final String responseData){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new GsonBuilder().serializeNulls().create();
                CheckNumberJson checkNumberJson = gson.fromJson(responseData,CheckNumberJson.class);
                if(checkNumberJson.getMessage().equals("success")){
                    Toast.makeText(RegisterActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                }
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
       //得到其他参数
       phoneNumber = phoneNumber_edit.getText().toString();
       String checkNumber = checkNumber_edit.getText().toString();

       
       if (same==1) {
           //post
           String finalPassword =firstPassword;
           OkHttpClient client = new OkHttpClient();
           RequestBody body = new FormBody.Builder()
                   .add("phoneNumber", phoneNumber)
                   .add("password", finalPassword)
                   .add("checkNumber", checkNumber)
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
                   final String responseData = response.body().string();
                   parseRegisterResponseJson(responseData);
               }
           });
       }else {
           Toast.makeText(this, "密码输入有误", Toast.LENGTH_SHORT).show();
       }

   }


   //解析返回的信息
   private void parseRegisterResponseJson( final String responseData){
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Gson gson =  new GsonBuilder().serializeNulls().create();
               RegisterResponseJson registerResponseJson = gson.fromJson(responseData,RegisterResponseJson.class);
               if (registerResponseJson.getMessage().equals("success")){
                  // Log.d("信息",registerResponseJson.getMessage()+"   "+registerResponseJson.getCode());
                   RegisterResponseData data = registerResponseJson.getData();//将返回的ia信息储存在其中
                   user_id = data.getId();//这里得到的是int
                   Log.d("信息",String.valueOf(data.getId()));//这里将user_id转化为String
                   Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(RegisterActivity.this,ChooseSexActivity.class);

                   //利用bundle传递信息
                   Bundle bundle = new Bundle();
                   bundle.putInt("user_id",user_id);
                   intent.putExtras(bundle);
                   //传递完成

                   startActivity(intent);
               }else if(registerResponseJson.getCode()==301){
                   Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
               }else if(registerResponseJson.getCode()==303){
                   Toast.makeText(RegisterActivity.this, "您已注册过，请直接登录", Toast.LENGTH_SHORT).show();
               }else if(registerResponseJson.getCode()==401){
                   Toast.makeText(RegisterActivity.this, "当前手机还没有发送过验证码", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
               }
           }
       });
   }

}