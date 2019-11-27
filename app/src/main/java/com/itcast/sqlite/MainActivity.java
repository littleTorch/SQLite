package com.itcast.sqlite;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyHelper myHelper;
    /**
     * 姓名
     */
    private EditText mName;
    /**
     * 电话
     */
    private EditText mPhone;
    /**
     * 增加
     */
    private Button mAdd;
    /**
     * 查询
     */
    private Button mOpen;
    /**
     * 修改
     */
    private Button mUpdate;
    /**
     * 删除
     */
    private Button mDelete;
    private TextView mTextView;
    private TableLayout mTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myHelper = new MyHelper(this);
        initView();
        increase();
    }

    private void initView() {
        mName = (EditText) findViewById(R.id.name);
        mPhone = (EditText) findViewById(R.id.phone);
        mAdd = (Button) findViewById(R.id.add);
        mAdd.setOnClickListener(this);
        mOpen = (Button) findViewById(R.id.open);
        mOpen.setOnClickListener(this);
        mUpdate = (Button) findViewById(R.id.update);
        mUpdate.setOnClickListener(this);
        mDelete = (Button) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mTableLayout = findViewById(R.id.tl);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        String name, phone = null;
        switch (v.getId()) {
            default:
                break;
            case R.id.add:
                if (mName.getText().toString().equals("")) {
                    Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (mName.getText().length()>=9) {
                    Toast.makeText(this, "姓名长度不能大于9位", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (mPhone.getText().toString().equals("")) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (mPhone.getText().length()>=14) {
                    Toast.makeText(this, "手机号长度不能大于14位", Toast.LENGTH_SHORT).show();
                    break;
                }
                name = mName.getText().toString();
                phone = mPhone.getText().toString();
                myHelper.add(this, name, phone);
                Toast.makeText(this, "信息已添加", Toast.LENGTH_SHORT).show();
                increase();
                break;
            case R.id.open:
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    //首先判断否获取了权限
                    if (ActivityCompat.shouldShowRequestPermissionRationale( this,Manifest.permission.CALL_PHONE)) {
                        //让用户手动授权
                        Toast.makeText(this, "请授权！", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }else{
                        ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.CALL_PHONE},1);
                    }
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + mPhone.getText()));
                    startActivity(intent);
                }
                break;
            case R.id.update:
                phone=mPhone.getText().toString();
                name=mName.getText().toString();
                myHelper.update(this,phone,name);
                Toast.makeText(this, "信息已修改", Toast.LENGTH_SHORT).show();
                increase();
                break;
            case R.id.delete:
                myHelper.detete(this);
                Toast.makeText(this, "信息已删除", Toast.LENGTH_SHORT).show();
                increase();
                break;
        }
    }
    public void increase(){
        int a=1;
        mTableLayout.removeAllViews();
        List<NameAndPhone> nameAndPhoneLists=new MyHelper(this).qurey(this);
        for(NameAndPhone nameAndPhone:nameAndPhoneLists){
            View view=View.inflate(this,R.layout.table,null);
            TextView tv1=view.findViewById(R.id.tv1);
            TextView tv2=view.findViewById(R.id.tv2);
            TextView tv3=view.findViewById(R.id.tv3);
            tv1.setText(""+a++);
            tv2.setText(nameAndPhone.getName()+"");
            tv3.setText(nameAndPhone.getPhone()+"");
            mTableLayout.addView(view);
        }
    }
}


