package adminzhezhe.heartbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobUser;

public class MainMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView spot1;
    private ImageView spot2;
    private ImageView spot3;
    private TextView username_d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View drawview = navigationView.inflateHeaderView(R.layout.nav_header_main_map);
       username_d = (TextView)drawview.findViewById(R.id.username_d);
         MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        String nickname = bmobUser.getNick();
        Log.i("nn",nickname);
        username_d.setText(nickname);*/

        spot1 = (ImageView)findViewById(R.id.MusicSpot1);
        spot2 = (ImageView)findViewById(R.id.MusicSpot2);
        spot3 = (ImageView)findViewById(R.id.MusicSpot3);
    }

    private List<Mp3Info> mp3Infos = null;


    public  void FindMusic(View v){
       /* Intent FMintent = new Intent();
        FMintent.setClass(this,FirstActivity.class);
        startActivity(FMintent);*/
        int listPosition = 0;
        ArrayList<Integer> playlist =  new ArrayList<Integer>();
        if (v==spot3)
        {
            Toast.makeText(getApplicationContext(), "太远啦，再走几步吧", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.i("f","in");
            if (v == spot1) {
                for(int i=0;i<3;i++)
                {
                    playlist=getRandoms(0,3,3);
                }
            } else if (v == spot2) {
                playlist=getRandoms(4,7,3);
            }
            MediaUtil mu = new MediaUtil();
            mp3Infos = mu.getMp3Infos();
            Mp3Info mp3Info = mp3Infos.get(playlist.get(listPosition));
            Intent intent = new Intent(MainMapActivity.this, FirstActivity.class);
            intent.putExtra("title", mp3Info.getTitle());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("artist", mp3Info.getArtist());
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("fans", mp3Info.getFans());
            Log.i("fans_f",""+mp3Info.getFans());
            Log.i("go_intent",""+mp3Info.getUrl());
            intent.putExtra("friend", mp3Info.getFriend());
            intent.putIntegerArrayListExtra("songs", playlist);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View drawview = navigationView.inflateHeaderView(R.layout.nav_header_main_map);
        username_d = (TextView)drawview.findViewById(R.id.username_d);
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        String nickname = bmobUser.getNick();
        Log.i("nn",nickname);
        username_d.setText(nickname);
        getMenuInflater().inflate(R.menu.main_map, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.****************************
        int id = item.getItemId();

        if (id == R.id.nav_liked) {
            // Handle the liked list
            Intent intentlike = new Intent();
            intentlike.setClass(this,LikedActivity.class);
            startActivity(intentlike);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainMapActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addfre) {
            Intent intent = new Intent(MainMapActivity.this, FriendManagerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_set) {

        } else if (id == R.id.nav_logout){
            BmobUser.logOut();   //清除缓存用户对象
            BmobUser currentUser = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了
            Intent intentlike = new Intent();
            intentlike.setClass(this,LoginActivity.class);
            startActivity(intentlike);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
               /* BmobUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了*/
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt( max - min + 1 ) + min;
    }

    public ArrayList<Integer> getRandoms(int min1, int max, int count1){
        ArrayList<Integer> randoms = new ArrayList<Integer>();
        List<Integer> listRandom = new ArrayList<Integer>();
        randoms.add(min1);
        int min=min1+1;
        int count=count1-1;

        if( count > ( max - min + 1 )){
            return null;
        }
        // 将所有的可能出现的数字放进候选list
        for(int i = min; i <= max; i++){
            listRandom.add(i);
        }
        // 从候选list中取出放入数组，已经被选中的就从这个list中移除
        for(int i = 0; i < count; i++){
            int index = getRandom(0, listRandom.size()-1);
            randoms.add(listRandom.get(index));
            listRandom.remove(index);
        }

        return randoms;
    }
}
