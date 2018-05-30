package adminzhezhe.heartbeat;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by AdminZhezhe on 2016/9/1.
 */
public class MyUser extends BmobUser {
    private  String nick;
    private List<Integer> liked;

    public  String getNick(){
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public  List<Integer> getliked(){
        return this.liked;
    }

    public void setLiked(List<Integer> liked) {
        this.liked = liked;
    }

}
