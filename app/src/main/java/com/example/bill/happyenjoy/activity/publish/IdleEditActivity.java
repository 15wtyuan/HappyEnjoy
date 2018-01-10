package com.example.bill.happyenjoy.activity.publish;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bill.happyenjoy.R;
import com.example.bill.happyenjoy.activity.BaseActivity;
import com.example.bill.happyenjoy.model.ChooseItem;
import com.example.bill.happyenjoy.model.UserData;
import com.example.bill.happyenjoy.view.ToolBarHelper;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bill on 2017/10/7.
 */

public class IdleEditActivity extends BaseActivity {



    private GridView gridView1;              //网格显示缩略图
    private Button buttonPublish;            //发布按钮
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    private Spinner spinner;
    private MyAdapter adapter;
    private EditText title_edit;//标题输入框
    private String title;//标题文字
    private EditText content_edit;//内容输入框
    private String content;//内容文字
    private String idle_type;//闲置类别
    private EditText contact_edit;//电话输入框
    private String contact;//电话文字
    private EditText price_edit;//价格输入框
    private String price;//价格
    private String[] postImagePath = new String[5];
    private Button ensure ;
    private String userId;
    int label;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        //从本地存储获取到userId
        List<UserData> userDatas = DataSupport.findAll(UserData.class);
        UserData userData = new UserData();
        for (UserData temp:userDatas){
            userData = temp;
        }


        userId = String.valueOf(userData.getUid());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.idle_edit_layout);
        spinner = (Spinner) findViewById(R.id.spinner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.white_toolbar);
        ToolBarHelper toolbarHelper = new ToolBarHelper(toolbar);
        toolbarHelper.setTitle("闲置发布");
        toolbar = toolbarHelper.getToolbar();
        setSupportActionBar(toolbar);

        List<ChooseItem> list = new ArrayList<>();


        ChooseItem commodity1 = new ChooseItem();
        ChooseItem commodity2 = new ChooseItem();
        ChooseItem commodity3 = new ChooseItem();
        ChooseItem commodity4 = new ChooseItem();
        commodity1.setIcon(R.drawable.xiala_shuma);
        commodity1.setName("数码");
        list.add(commodity1);
        commodity2.setIcon(R.drawable.xiala_jujia);
        commodity2.setName("居家");
        list.add(commodity2);
        commodity3.setIcon(R.drawable.xiala_tushu);
        commodity3.setName("图书");
        list.add(commodity3);
        commodity4.setIcon(R.drawable.xiala_qita);
        commodity4.setName("其他");
        list.add(commodity4);

        //上传相关
        title_edit = (EditText)findViewById(R.id.title_editText);
        content_edit = (EditText)findViewById(R.id.content_editText);
        contact_edit = (EditText)findViewById(R.id.contact_edit);
        price_edit = (EditText)findViewById(R.id.price_edit);

        //上传相关


//为spinner设置adapter
        adapter = new MyAdapter(this, list);
        spinner.setAdapter(adapter);


        //解决软键盘挡住输入框的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);

        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//锁定屏幕为竖直方向
        //获取控件对象
        gridView1 = (GridView) findViewById(R.id.gridView1);

        /*
         * 载入默认图片添加图片加号
         * 通过适配器实现
         * SimpleAdapter参数imageItem为数据源 R.layout.griditem_addpic为布局
         */
        //获取资源图片加号
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.add_photo);
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

        /*
         * 监听GridView点击事件
         * 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
         */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 10) { //第一张为默认图片
                    Toast.makeText(IdleEditActivity.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                } else if (position == 0) { //点击图片位置为+ 0对应0张图片
                    Toast.makeText(IdleEditActivity.this, "添加图片", Toast.LENGTH_SHORT).show();
                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    //通过onResume()刷新数据
                } else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第"+(position + 1)+" 号图片",
                    //      Toast.LENGTH_SHORT).show();
                }
            }
        });

        ensure = (Button)findViewById(R.id.edit_ensure_button);
        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = title_edit.getText().toString();
                content = content_edit.getText().toString();
                contact = contact_edit.getText().toString();
                price = price_edit.getText().toString();

                switch(label){
                    case 0 :
                        idle_type = "31";
                        break;
                    case 1:
                        idle_type = "32";
                        break;
                    case 2:
                        idle_type = "33";
                        break;
                    case 3:
                        idle_type = "34";
                }


                //showToase(userId);
                //Toast.makeText(IdleEditActivity.this,String.valueOf(label), Toast.LENGTH_SHORT).show();
                showToase(userId);
                //Toast.makeText(LifeEditActivity.this,String.valueOf(label), Toast.LENGTH_SHORT).show();
                if (title==null||title.equals("")||content==null||content.equals("")||contact==null
                        ||contact.equals("")||price==null||price.equals("")){
                    showToase("请完善信息");
                }else{

                    postMassageHttp();
                    showToase(title);
                }
                // showToase(title);
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



//自定义Spinner的适配器
    private class MyAdapter extends BaseAdapter {

        private Context mContext;
        private List<ChooseItem> mList;

        public MyAdapter(Context context, List<ChooseItem> list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            label = position;//从这里获取到用户点击时哪个子项
            return position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            IdleEditActivity.ViewHolder holder = null;

            if (convertView == null) {
                convertView = View.inflate(IdleEditActivity.this, R.layout.menu_item, null);

                holder = new IdleEditActivity.ViewHolder();

                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.tv = (TextView) convertView.findViewById(R.id.tv);

                convertView.setTag(holder);
            } else {
                holder = (IdleEditActivity.ViewHolder) convertView.getTag();
            }

            holder.iv.setImageResource(mList.get(position).getIcon());
            holder.tv.setText(mList.get(position).getName());

            return convertView;
        }
    }
        public class ViewHolder {
            private ImageView iv;
            private TextView tv;
        }

//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }  //end if 打开图片
    }

    @Override
    public void onResume() {
        int number;
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            Bitmap smallBmp = setScaleBitmap(addbmp,6);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", smallBmp);//可以用一个map来存储路径和图片
            imageItem.add(map);
            number = imageItem.size();

            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            postImagePath[number-1]=pathImage;
            pathImage = null;
        }
    }

    //将图片缩小的函数
    private Bitmap setScaleBitmap(Bitmap photo,int SCALE) {
        if (photo != null) {
            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
            //这里缩小了1/2,但图片过大时仍然会出现加载不了,但系统中一个BITMAP最大是在10M左右,我们可以根据BITMAP的大小
            //根据当前的比例缩小,即如果当前是15M,那如果定缩小后是6M,那么SCALE= 15/6
            Bitmap smallBitmap = zoomBitmap(photo, photo.getWidth() / SCALE,
                    photo.getHeight() / SCALE);
            //释放原始图片占用的内存，防止out of memory异常发生
            photo.recycle();
            return smallBitmap;
        }
        return null;
    }

    //利用矩阵压缩图片，不会造成内存溢出
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IdleEditActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void postMassageHttp() {
        OkHttpClient client = new OkHttpClient();


        if (postImagePath[0]!=null) {
            MediaType type= MediaType.parse("image/*");
            //File file = new File(postImagePath[0]);



            File file = new File(postImagePath[0]);
            Log.d("路径",postImagePath[0]);
            RequestBody fileBody = RequestBody.create(type, file);

            //混合请求体
            RequestBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userId)
                    .addFormDataPart("title", title)
                    .addFormDataPart("label", "11")
                    .addFormDataPart("brief", content)
                    .addFormDataPart("piece", "1")
                    .addFormDataPart("price", price)
                    .addFormDataPart("picture1", file.getName(), fileBody)
                    .build();
            Log.d("文件名", file.getName());
            final Request request = new Request.Builder()
                    .url("http://139.199.202.23/School/public/index.php/index/Issue/newIssue/")
                    .post(multipartBody)
                    .build();


            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseData = response.body().string();
                    System.out.print(responseData);
                    Log.d("信息", responseData);

                }
            });
        }
        else{

            //混合请求体
            RequestBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userId)
                    .addFormDataPart("title", title)
                    .addFormDataPart("label", "11")
                    .addFormDataPart("brief", content)
                    .addFormDataPart("piece", "1")
                    .addFormDataPart("price", price)
                    .build();
            final Request request = new Request.Builder()
                    .url("http://139.199.202.23/School/public/index.php/index/Issue/newIssue/")
                    .post(multipartBody)
                    .build();


            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseData = response.body().string();
                    System.out.print(responseData);
                    Log.d("信息", responseData);

                }
            });
        }
    }

}
