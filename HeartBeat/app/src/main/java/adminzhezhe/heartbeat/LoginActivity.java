package adminzhezhe.heartbeat;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText pw;
   // private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*actionBar = getSupportActionBar();
        setCustomActionBar();*/
        setContentView(R.layout.activity_login);

        username=(EditText) findViewById(R.id.et_username);
        pw=(EditText) findViewById(R.id.et_pw);
    }

   /* private void setCustomActionBar() {
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

    }*/

    public void goSginup(View v){
        startActivity(new Intent(LoginActivity.this,SginupActivity.class));
        //LoginActivity.this.finish();
    }

    public void goLogin(View v){
        MyUser bu2 = new MyUser();
        bu2.setUsername(username.getText().toString().trim());
        bu2.setPassword(pw.getText().toString().trim());
        bu2.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(), "欢迎回来", Toast.LENGTH_SHORT).show();
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                    startActivity(new Intent(LoginActivity.this,MainMapActivity.class));
                    LoginActivity.this.finish();
                }else{
                    //loge(e);
                    Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
