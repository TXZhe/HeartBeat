package adminzhezhe.heartbeat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SplashActivity extends AppCompatActivity {

 /*   private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("splash","ok");
        Bmob.initialize(this, "0eebed2c34d7070b3e5a33779f542103");

        //setContentView(R.layout.activity_splash);
       /* Person p2 = new Person();
        p2.setName("lucky");
        p2.setAddress("北京海淀");
        p2.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(), "创建数据成功：" + objectId, Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });*/



        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if(bmobUser != null){
            // 允许用户使用应用
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainMapActivity.class));
                    SplashActivity.this.finish();
                }
            },1000);
        }
        else{
            //缓存用户对象为空时， 可打开用户注册界面…
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    SplashActivity.this.finish();
                }
            },1000);
        }

    }

}

