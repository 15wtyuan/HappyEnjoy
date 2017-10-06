package com.example.bill.happyenjoy.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.activity.homePage.HomePageActivity;
import com.example.bill.happyenjoy.model.UserData;
import com.example.bill.happyenjoy.model.UserDataJson;
import com.example.bill.happyenjoy.model.UserLoginData;
import com.example.bill.happyenjoy.networkTools.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends BaseActivity {

    Button login_Button;
    Button register_button;
    UserLoginData userLoginData = new UserLoginData();
    UserData userData = new UserData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LitePal.getDatabase();

        register_button = (Button)findViewById(R.id.regist_Button);
        login_Button = (Button) findViewById(R.id.login_Button);

        List<UserLoginData> userLoginDatas = DataSupport.findAll(UserLoginData.class);
        for(UserLoginData temp:userLoginDatas){
            userLoginData = temp;
        }
        List<UserData> userDatas = DataSupport.findAll(UserData.class);
        if (userDatas != null){
            for (UserData temp:userDatas){
                userData = temp;
            }
        }

        if (userLoginData.getPassword() == null){
            setButton();
        }else {
            RequestBody requestBody = new FormBody.Builder()
                    .add("phoneNumber",userLoginData.getPhoneNumber())
                    .add("password",userLoginData.getPassword())
                    .build();
            HttpUtil.sendOkHttpRequest("http://139.199.202.23/School/public/index.php/index/User/login/",requestBody,new okhttp3.Callback(){
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("test","链接成功");
                    String responseData = response.body().string();
                    parseJSON(responseData);
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    linkFailure();
                }
            });
        }

    }

    private void linkFailure(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToase("链接失败");
                goHome();
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

                    goHome();
                }else {
                    setButton();
                }
            }
        });
    }

    private void goHome(){
        final Intent intent = new Intent(MainActivity.this,HomePageActivity.class);
        //通过一个时间控制函数Timer，在实现一个活动与另一个活动的跳转。
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                finish();

            }
        }, 1000);//这里停留时间为1000=1s。
    }

    private void setButton(){
        login_Button.setVisibility(View.VISIBLE);
        register_button.setVisibility(View.VISIBLE);
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}
