package adminzhezhe.heartbeat;

import java.util.ArrayList;

/**
 * Created by AdminZhezhe on 2016/8/31.
 */
public class Mp3Info {

    //一首歌的属性
    private String title;
    private String url;
    private String artist;
    private int fans;
    private int friend;

    //把属性 变成字符串，用于测试
    @Override
    public String toString() {
        return "Mp3Info [ url=" + url + ", title=" + title + ", artist="  +artist+ "]";
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getFans() {
        return fans;
    }
    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getFriend() {
        return friend;
    }
    public void setFriend(int friend) {
        this.fans = friend;
    }



    //构造函数
    public Mp3Info() {
        super();
    }

    //构造函数
    public Mp3Info(String title, String url, String artist,int fans,int friend)
    {
        super();
        this.title=title;
        this.artist=artist;
        this.url=url;
        this.fans=fans;
        this.friend=friend;
    }


}