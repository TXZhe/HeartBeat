package adminzhezhe.heartbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bmob.v3.BmobUser;


public class LikedActivity extends AppCompatActivity {

    private ListView likedmusic;
    private ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String, Object>>();
    MediaUtil mu = new MediaUtil();
    private List<Mp3Info> mp3Infos = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //列表内容************************
        likedmusic = (ListView) findViewById(R.id.liked_list);
        getData();
        SimpleAdapter adapter = new SimpleAdapter(this, listitem, R.layout.songs_adapter, new String[]{"songs_index","songs_title", "songs_info"}, new int[]{R.id.songs_index,R.id.songs_title, R.id.songs_info});

        likedmusic.setAdapter(adapter);
        //在这添加点击事件**********************************
        setListViewHeightBasedOnChildren(likedmusic);

        likedmusic.setOnItemClickListener(new MusicListItemClickListener());
    }

    List<Integer> likelist =  new ArrayList<Integer>();
    ArrayList<Integer> playlist =  new ArrayList<Integer>();
    //获取歌曲数据
    public void getData() {
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        //MyUser bmobUser = (MyUser) MyUser.getCurrentUser();

        likelist = bmobUser.getliked();
        mp3Infos = mu.getMp3Infos(likelist);
        for (int i = 0; i < likelist.size(); i++) {
            Mp3Info mp3Info = mp3Infos.get(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("songs_index", i+1);
            map.put("songs_title", mp3Info.getTitle());
            map.put("songs_info", mp3Info.getArtist());
            listitem.add(map);
            playlist.add(likelist.get(i));
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度

        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
       params.width=dMetrics.widthPixels;

        listView.setLayoutParams(params);
    }


    //private String likelistcode = new String();

    private class MusicListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if(listitem != null) {
                //mp3Infos = mu.getMp3Infos(likelist);
                Mp3Info mp3Info = mp3Infos.get(position);
                Intent intent = new Intent(LikedActivity.this, FirstActivity.class);

                intent.putExtra("title", mp3Info.getTitle());
                intent.putExtra("url", mp3Info.getUrl());
                intent.putExtra("artist", mp3Info.getArtist());
                intent.putExtra("listPosition",position);
                intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
                intent.putExtra("fans", mp3Info.getFans());
                intent.putIntegerArrayListExtra("songs", playlist);
                //intent.putExtra("songs", likelistcode);
                startActivity(intent);
            }
        }
    }
}
