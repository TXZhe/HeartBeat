package adminzhezhe.heartbeat;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FirstActivity extends AppCompatActivity{
    private TextView musicTitle = null;
    private TextView musicArtist = null;
    private TextView likenum_s = null;
    private TextView likenum_b = null;
    private ImageView previousBtn; // 上一首
    private ImageView playBtn;     // 播放（播放、暂停）
    private ImageView nextBtn;     // 下一首
    private SeekBar music_progressBar;  //歌曲进度
    private TextView currentProgress;   //当前进度消耗的时间
    private TextView finalProgress;     //歌曲时间
    private ImageView liked;


    private String title;       //歌曲标题
    private String artist;      //歌曲艺术家
    private String url;         //歌曲路径
    private int listPosition;   //播放歌曲在mp3Infos的位置
    private int currentTime;    //当前歌曲播放时间
    private int duration;       //歌曲长度
    private int flag;           //播放标识
    private int fans;
    private int friend;

    private ArrayList<Integer> playlist =  new ArrayList<Integer>();

    private int repeatState;
    private final int isCurrentRepeat = 1; // 单曲循环
    private final int isAllRepeat = 2;      // 全部循环
    private final int isNoneRepeat = 3;     // 无重复播放
    private boolean isPlaying;              // 正在播放
    private boolean isPause;                // 暂停
    private boolean isNoneShuffle;           // 顺序播放
    private boolean isShuffle;          // 随机播放

    private List<Mp3Info> mp3list;


    private PlayerReceiver playerReceiver;
    public static final String UPDATE_ACTION = "adminzhezhe.heartbeat.UPDATE_ACTION";  //更新动作
    public static final String CTL_ACTION = "adminzhezhe.heartbeat.CTL_ACTION";        //控制动作
    public static final String MUSIC_CURRENT = "adminzhezhe.heartbeat.MUSIC_CURRENT";  //音乐当前时间改变动作
    public static final String MUSIC_DURATION = "adminzhezhe.heartbeat.MUSIC_DURATION";//音乐播放长度改变动作
    public static final String MUSIC_PLAYING = "adminzhezhe.heartbeat.MUSIC_PLAYING";  //音乐正在播放动作

    private AnimationDrawable animationDrawable;
    private LinearLayout musicplay;

    MyUser bmobUser = new MyUser();

    private LinearLayout mHiddenLayout;
    private LinearLayout mIv;
    private float mDensity;

    private int mHiddenViewMeasuredHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        musicplay = (LinearLayout) findViewById(R.id.musicplay);
        musicplay.setBackgroundResource(R.drawable.framelist);
        animationDrawable = (AnimationDrawable) musicplay.getBackground();
        animationDrawable.start();



        musicTitle = (TextView) findViewById(R.id.songs_tile_playing);
        musicArtist = (TextView) findViewById(R.id.songs_info_playing);
        likenum_b=(TextView) findViewById(R.id.likenum_b);

        findViewById();
        setViewOnclickListener();

        MediaUtil mu = new MediaUtil();
        mp3list = mu.getMp3Infos();
        playerReceiver = new PlayerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        registerReceiver(playerReceiver, filter);

        mIv = (LinearLayout) findViewById(R.id.b_likenum);
        mHiddenLayout = (LinearLayout) findViewById(R.id.linear_hidden);
        mDensity = getResources().getDisplayMetrics().density;
        mHiddenViewMeasuredHeight = (int) (mDensity * 120 + 0.5);
        mHiddenLayout.setVisibility(View.GONE);
        likenum_s=(TextView)findViewById(R.id.likenum_s);

    }

    /**
     * 从界面上根据id获取按钮
     */
    private void findViewById() {
        previousBtn = (ImageView) findViewById(R.id.imageView_past);
        playBtn = (ImageView) findViewById(R.id.imageView_play);
        nextBtn = (ImageView) findViewById(R.id.imageView_next);
        music_progressBar = (SeekBar) findViewById(R.id.seekbar);
        liked = (ImageView) findViewById(R.id.golike);

        previousBtn.setImageResource(R.drawable.button_past);
        playBtn.setImageResource(R.drawable.button_play);
        nextBtn.setImageResource(R.drawable.button_next);

        previousBtn.setClickable(true);
        playBtn.setClickable(true);
        nextBtn.setClickable(true);
        /*currentProgress = (TextView) findViewById(R.id.current_progress);
        finalProgress = (TextView) findViewById(R.id.final_progress);*/
    }


    /**
     * 给每一个按钮设置监听器
     */
    private void setViewOnclickListener() {
        ViewOnclickListener ViewOnClickListener = new ViewOnclickListener();
        previousBtn.setOnClickListener(ViewOnClickListener);
        playBtn.setOnClickListener(ViewOnClickListener);
        nextBtn.setOnClickListener(ViewOnClickListener);
        music_progressBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    /**
     * 在OnResume中初始化和接收Activity数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString("title");
        artist = bundle.getString("artist");
        url = bundle.getString("url");
        fans=bundle.getInt("fans");
        Log.i("fans_f",""+fans);
        friend=bundle.getInt("friend");
        listPosition = bundle.getInt("listPosition");
        playlist =(ArrayList<Integer>) getIntent().getIntegerArrayListExtra("songs");
        Log.i("check_intent1",":"+playlist.toString()+listPosition);
        flag = bundle.getInt("MSG");
        currentTime = bundle.getInt("currentTime");
        duration = bundle.getInt("duration");
        initView();
    }

    /**
     * 初始化界面
     */
    public void initView() {
        musicTitle.setText(title);
        musicArtist.setText(artist);
        music_progressBar.setProgress(currentTime);
        music_progressBar.setMax(duration);

        //setFriends(friend);
        //Log.i("friend_ini",""+friend);
        setnewFriend(playlist.get(listPosition));

        if(flag == AppConstant.PlayerMsg.PLAYING_MSG) { //如果播放信息是正在播放
            Toast.makeText(FirstActivity.this, "正在播放--" + title,Toast.LENGTH_SHORT).show();
        }
        else if(flag == AppConstant.PlayerMsg.PLAY_MSG) { //如果是点击列表播放歌曲的话
            play();
        }
        playBtn.setImageResource(R.drawable.button_pause);
        isPlaying = true;
        isPause = false;

        bmobUser = BmobUser.getCurrentUser(MyUser.class);
        //bmobUser = (MyUser) MyUser.getCurrentUser();
        List<Integer> likelist =  bmobUser.getliked();
        if(likelist!=null && likelist.contains(playlist.get(listPosition))) {
            liked.setImageResource(R.drawable.like1);
            likenum_s.setText(Integer.toString(fans+1));
            likenum_b.setText(Integer.toString(fans+1));
        }
        else {
            liked.setImageResource(R.drawable.like);
            likenum_s.setText(Integer.toString(fans));
            likenum_b.setText(Integer.toString(fans));
        }
    }

    /**
     * 反注册广播
     */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(playerReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 控件点击事件
     * @author
     *
     */
    private class ViewOnclickListener implements OnClickListener {
        Intent intent = new Intent();
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.imageView_play:
                    if (isPlaying) {
                        playBtn.setImageResource(R.drawable.button_play);
                        intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
                        intent.setClass(FirstActivity.this,PlayerService.class);
                        intent.setPackage(getPackageName());
                        intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
                        startService(intent);
                        isPlaying = false;
                        isPause = true;

                    } else if (isPause) {
                        playBtn.setImageResource(R.drawable.button_pause);
                        intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
                        intent.setClass(FirstActivity.this,PlayerService.class);
                        intent.setPackage(getPackageName());
                        intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
                        startService(intent);
                        isPause = false;
                        isPlaying = true;
                    }
                    break;
                case R.id.imageView_past:       //上一首歌曲
                    imageView_past();
                    break;
                case R.id.imageView_next:           //下一首歌曲
                    imageView_next();
                    break;
                case R.id.backbutton:
                    /*playBtn.setImageResource(R.drawable.button_pause);
                    intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
                    intent.setClass(FirstActivity.this,PlayerService.class);
                    intent.setPackage(getPackageName());
                    intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
                    startService(intent);
                    isPause = false;
                    isPlaying = true;*/
                    finish();
            }
        }
    }

    /**
     * 实现监听Seekbar的类
     * @author
     *
     */
    private class SeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if(fromUser) {
                audioTrackChange(progress); //用户控制进度的改变
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }

    /**
     * 播放音乐
     */
    public void play() {
        //开始播放的时候为顺序播放
        repeat_all();
        Intent intent = new Intent();
        intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
        intent.setClass(this,PlayerService.class);
        intent.setPackage(getPackageName());
        intent.putExtra("url", url);
        intent.putExtra("listPosition", listPosition);
        intent.putExtra("MSG", flag);
        intent.putIntegerArrayListExtra("songs", playlist);
        startService(intent);

        /*Intent mIntent = new Intent();
        mIntent.setAction("XXX.XXX.XXX");//你定义的service的action
        mIntent.setPackage(getPackageName());//这里你需要设置你应用的包名
        context.startService(mIntent);*/
    }



    public void audioTrackChange(int progress) {
        Intent intent = new Intent();
        intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
        intent.setClass(this,PlayerService.class);
        intent.setPackage(getPackageName());
        intent.putExtra("url", url);
        intent.putExtra("listPosition", listPosition);
        if(isPause) {
            intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
        }
        else {
            intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
        }
        intent.putExtra("progress", progress);
        startService(intent);
    }



    /**
     * 全部循环
     */
    public void repeat_all() {
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", 2);
        sendBroadcast(intent);
    }


    /**
     * 上一首
     */
    public void imageView_past() {
        playBtn.setImageResource(R.drawable.button_pause);
        listPosition = listPosition - 1;
        if(listPosition >= 0) {
            Mp3Info mp3Info = mp3list.get(playlist.get(listPosition));   //上一首MP3
            musicTitle.setText(mp3Info.getTitle());
            musicArtist.setText(mp3Info.getArtist());
            setnewFriend(playlist.get(listPosition));
            Log.i("friend_1",""+friend);
            url = mp3Info.getUrl();
            Intent intent = new Intent();
            intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
            intent.setClass(this,PlayerService.class);
            intent.setPackage(getPackageName());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("MSG", AppConstant.PlayerMsg.PRIVIOUS_MSG);
            startService(intent);
            gosetlike(mp3Info.getFans());
        }
        else {
            listPosition++;
            Toast.makeText(FirstActivity.this, "没有上一首了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下一首
     */
    public void imageView_next() {
        playBtn.setImageResource(R.drawable.button_pause);
        listPosition = listPosition + 1;
        if(listPosition <= playlist.size() - 1) {
            Mp3Info mp3Info = mp3list.get(playlist.get(listPosition));
            url = mp3Info.getUrl();
            musicTitle.setText(mp3Info.getTitle());
            musicArtist.setText(mp3Info.getArtist());
            setnewFriend(playlist.get(listPosition));
            Log.i("friend_2",""+friend);
            Intent intent = new Intent();
            intent.setAction("adminzhezhe.heartbeat.MUSIC_SERVICE");
            intent.setClass(FirstActivity.this,PlayerService.class);
            intent.setPackage(getPackageName());
            intent.putExtra("url", mp3Info.getUrl());
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);
            startService(intent);
            gosetlike(mp3Info.getFans());
        } else {
            listPosition--;
            Toast.makeText(FirstActivity.this, "没有下一首了", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 用来接收从service传回来的广播的内部类
     * @author
     *
     */
    public class PlayerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MUSIC_CURRENT)) {
                currentTime = intent.getIntExtra("currentTime", -1);
               // currentProgress.setText(MediaUtil.formatTime(currentTime));
                music_progressBar.setProgress(currentTime);
            } else if(action.equals(MUSIC_DURATION)) {
                int duration = intent.getIntExtra("duration", -1);
                music_progressBar.setMax(duration);
               // finalProgress.setText(MediaUtil.formatTime(duration));
            } else if(action.equals(UPDATE_ACTION)) {
                //获取Intent中的current消息，current代表当前正在播放的歌曲
                listPosition = intent.getIntExtra("current", -1);
                if(listPosition >= playlist.size()) {
                    listPosition = 0;
                }
                url = mp3list.get(playlist.get(listPosition)).getUrl();
                musicTitle.setText(mp3list.get(playlist.get(listPosition)).getTitle());
                musicArtist.setText(mp3list.get(playlist.get(listPosition)).getArtist());
                setnewFriend(playlist.get(listPosition));
                Log.i("friend_next",""+mp3list.get(playlist.get(listPosition)).getFriend());
                gosetlike(mp3list.get(playlist.get(listPosition)).getFans());
                play();
                /*if(listPosition == 0) {
                   // finalProgress.setText(MediaUtil.formatTime(mp3list.get(listPosition).getDuration()));
                    playBtn.setImageResource(R.drawable.button_play);
                    isPause = true;
                }*/
            }
        }

    }

    private ImageView cat;
    private ImageView dog;
    private ImageView guitar;
    private ImageView sight;

    public void setnewFriend(int i)
    {
        cat = (ImageView) findViewById(R.id.catty);
        dog = (ImageView) findViewById(R.id.doggy);
        guitar = (ImageView) findViewById(R.id.guitar);
        sight = (ImageView) findViewById(R.id.happy);
        int type=(i%4)+1;
        switch (type)
        {
            case 1:
                cat.setVisibility(View.VISIBLE);
                dog.setVisibility(View.GONE);
                guitar.setVisibility(View.VISIBLE);
                sight.setVisibility(View.VISIBLE);
                break;
            case 2:
                cat.setVisibility(View.VISIBLE);
                dog.setVisibility(View.GONE);
                guitar.setVisibility(View.VISIBLE);
                sight.setVisibility(View.GONE);
                break;
            case 3:
                cat.setVisibility(View.VISIBLE);
                dog.setVisibility(View.VISIBLE);
                guitar.setVisibility(View.GONE);
                sight.setVisibility(View.GONE);
                break;
            case 4:
                cat.setVisibility(View.VISIBLE);
                dog.setVisibility(View.VISIBLE);
                guitar.setVisibility(View.VISIBLE);
                sight.setVisibility(View.VISIBLE);
                break;
            case 5:
                cat.setVisibility(View.VISIBLE);
                dog.setVisibility(View.GONE);
                guitar.setVisibility(View.VISIBLE);
                sight.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void goBack(View v){
        finish();
    }
    public void goliked(View v)
    {
        int heartnum = Integer.parseInt(likenum_s.getText().toString());

        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        //MyUser bmobUser = (MyUser) MyUser.getCurrentUser();
        List<Integer> likelist =  new ArrayList<Integer>();
        if (bmobUser.getliked()!=null)
            likelist = bmobUser.getliked();
        if(likelist!=null && likelist.contains(playlist.get(listPosition)))
        {
            for(int i=0;i<likelist.size();i++)
            {
                if(likelist.get(i)==playlist.get(listPosition))
                {
                    likelist.remove(i);
                    break;
                }
            }
            liked.setImageResource(R.drawable.like);
            likenum_s.setText(Integer.toString(heartnum-1));
            likenum_b.setText(Integer.toString(heartnum-1));
        }
        else
        {
            likelist.add(playlist.get(listPosition));
            liked.setImageResource(R.drawable.like1);
            likenum_s.setText(Integer.toString(heartnum+1));
            likenum_b.setText(Integer.toString(heartnum+1));
            Toast.makeText(getApplicationContext(), "已添加至我喜欢的音乐", Toast.LENGTH_SHORT).show();
        }

        //更新喜欢列表
        bmobUser.setLiked(likelist);
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
    }

    public void gosetlike(int fans){
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        //MyUser bmobUser = (MyUser) MyUser.getCurrentUser();
        List<Integer> likelist =  new ArrayList<Integer>();
        likelist = bmobUser.getliked();
        if(likelist!=null && likelist.contains(playlist.get(listPosition)))
        {
            liked.setImageResource(R.drawable.like1);
            likenum_s.setText(Integer.toString(fans+1));
            likenum_b.setText(Integer.toString(fans+1));
        }
        else
        {
            liked.setImageResource(R.drawable.like);
            likenum_s.setText(Integer.toString(fans));
            likenum_b.setText(Integer.toString(fans));
        }
    }

    public void likenum(View v) {
        if (mHiddenLayout.getVisibility() == View.GONE) {
            animateOpen(mHiddenLayout);
            animationIvOpen();
        } else {
            animateClose(mHiddenLayout);
            animationIvClose();
        }
    }

    private void animateOpen(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                mHiddenViewMeasuredHeight);
        animator.start();

    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }

    private void animationIvOpen() {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        //mIv.startAnimation(animation);
        likenum_s.setVisibility(View.GONE);

    }

    private void animationIvClose() {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        //mIv.startAnimation(animation);
        likenum_s.setVisibility(View.VISIBLE);
    }

}
