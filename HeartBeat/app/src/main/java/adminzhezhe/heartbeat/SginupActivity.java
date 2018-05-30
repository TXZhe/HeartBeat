package adminzhezhe.heartbeat;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SginupActivity extends AppCompatActivity {

    private EditText nick;
    private EditText username;
    private EditText pw;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setTitle("注册");
        setContentView(R.layout.activity_sginup);

        nick=(EditText) findViewById(R.id.et_nick);
        username=(EditText) findViewById(R.id.et_username);
        pw=(EditText) findViewById(R.id.et_pw);
    }


    public void DecideSgin(View v)
    {
        MyUser bu = new MyUser();
        bu.setUsername(username.getText().toString().trim());
        bu.setPassword(pw.getText().toString().trim());
        bu.setNick(nick.getText().toString().trim());
        List<Integer> likelist =  new ArrayList<Integer>();
        bu.setLiked(likelist);
        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if(e==null){
                    //toast("注册成功:" +s.toString());
                    Toast.makeText(getApplicationContext(), "注册成功!请完成登录", Toast.LENGTH_SHORT).show();
                    SginupActivity.this.finish();
                   /* startActivity(new Intent(SginupActivity.this,MainMapActivity.class));
                    */
                }
                else{
                    //loge(e);
                    Toast.makeText(getApplicationContext(), "注册失败，请更换用户名", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
