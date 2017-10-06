package com.example.bill.happyenjoy.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.activity.homePage.HomePageActivity;
import com.example.bill.happyenjoy.model.UserData;
import com.example.bill.happyenjoy.model.UserDataJson;
import com.example.bill.happyenjoy.model.UserLoginData;
import com.example.bill.happyenjoy.networkTools.HttpUtil;
import com.example.bill.happyenjoy.view.ToolBarHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bill on 2017/9/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final int SHOW_RESPONSE=0;//用于更新操作
    public TextView responseText;
    private EditText login_edit_account;
    private EditText login_edit_password;
    UserLoginData userLoginData = new UserLoginData();
    UserData userData = new UserData();

    //用于处理和发送消息的Hander


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.white_toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("登录");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        Button login = (Button) findViewById(R.id.login_login_Button);
        TextView forget_text = (TextView)findViewById(R.id.textView03) ;
        login_edit_account = (EditText)findViewById(R.id.login_edit_account);
        login_edit_password = (EditText)findViewById(R.id.login_edit_password);
        //responseText=(TextView)findViewById(R.id.response_text);
        login.setOnClickListener(this);
        forget_text.setOnClickListener(this);

        List<UserLoginData> userLoginDatas = DataSupport.findAll(UserLoginData.class);
        if (userLoginDatas != null){
            for (UserLoginData temp:userLoginDatas){
                userLoginData = temp;
            }
        }
        List<UserData> userDatas = DataSupport.findAll(UserData.class);
        if (userDatas != null){
            for (UserData temp:userDatas){
                userData = temp;
            }
        }
    }


    //设置组件的点击属性
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_login_Button){
            userLoginData.setPhoneNumber(login_edit_account.getText().toString());
            userLoginData.setPassword(login_edit_password.getText().toString());
            RequestBody requestBody = new FormBody.Builder()
                    .add("phoneNumber",userLoginData.getPhoneNumber())
                    .add("password",userLoginData.getPassword())
                    .build();
            HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/User/login/",requestBody,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    parseJSON(responseData);
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    linkFailure();
                }
            });
        }

        if(v.getId()==R.id.textView03){
            Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(intent);
        }
    }

    private void linkFailure(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToase("链接失败");
            }
        });
    }

    private void parseJSON(final String responseData){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new GsonBuilder().serializeNulls().create();
                UserDataJson userDataJson = gson.fromJson(responseData, UserDataJson.class);
                if (userDataJson.getMessage().equals("success")){

                    userLoginData.save();

                    UserData userDataTemp = userDataJson.getData();
                    userData.setPassword(userDataTemp.getPassword());
                    userData.setPhoneNumber(userDataTemp.getPhoneNumber());
                    userData.setCheckNumber(userDataTemp.getCheckNumber());
                    userData.setCity(userDataTemp.getCity());
                    userData.setFlowerName(userDataTemp.getFlowerName());
                    userData.setImage(userDataTemp.getImage());
                    userData.setProvince(userDataTemp.getProvince());
                    userData.setQq(userDataTemp.getQq());
                    userData.setSex(userDataTemp.getSex());
                    userData.setTrueName(userDataTemp.getTrueName());
                    userData.setUid(userDataTemp.getUid());
                    userData.setUniversity(userDataTemp.getUniversity());
                    userData.setWechat(userDataTemp.getWechat());
                    userData.save();

                    Intent intent = new Intent(LoginActivity.this,HomePageActivity.class);
                    startActivity(intent);
                }else {
                    showToase("账号或密码错误！");
                }
            }
        });
    }

//设置组件的点击属性
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
