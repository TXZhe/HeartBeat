package adminzhezhe.heartbeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ProfileActivity extends AppCompatActivity {

    private EditText nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nick=(EditText) findViewById(R.id.set_nick);
        nick.setHint(bmobUser.getNick());
    }

    public void save_profile(View v)
    {

        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        bmobUser.setNick(nick.getText().toString().trim());
        bmobUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("update","更新用户信息成功");
                }else{
                    Log.i("update","更新用户信息失败:" + e.getMessage());
                }
            }
        });
        finish();
    }

}
