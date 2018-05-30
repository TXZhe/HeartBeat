package adminzhezhe.heartbeat;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AdminZhezhe on 2016/8/31.
 */
public class MediaUtil {

    public List<Mp3Info> getMp3Infos(){
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        for(int i=0;i<8;i++){
            Mp3Info mp3Info = new Mp3Info();
            String title = songs[i][1];
            Log.i("songs",title);
            String url = songs[i][0];
            String artist = songs[i][2];
            mp3Info.setArtist(artist);
            mp3Info.setTitle(title);
            mp3Info.setUrl(url);
            mp3Info.setFans(fans[1][i]);
            mp3Info.setFriend(fans[0][i]);
            mp3Infos.add(mp3Info);
        }
        return mp3Infos;
    }

    public List<Mp3Info> getMp3Infos(List<Integer> songslist){
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        for(int i=0;i<songslist.size();i++){
            Mp3Info mp3Info = new Mp3Info();
            String title = songs[songslist.get(i)][1];
            Log.i("songs",title);
            String url = songs[songslist.get(i)][0];
            String artist = songs[songslist.get(i)][2];
            mp3Info.setArtist(artist);
            mp3Info.setTitle(title);
            mp3Info.setUrl(url);
            mp3Info.setFans(fans[1][songslist.get(i)]);
            mp3Info.setFriend(fans[0][songslist.get(i)]);
            mp3Infos.add(mp3Info);
        }
        return mp3Infos;
    }

    //http://m2.music.126.net/sgN1Pmz4adgRwQYgGWF-4g==/7880199837360436.mp3七里香

    String songs[][]={
            {"http://wl.baidu190.com/1428702600/691f75227b6a645659da4128f7478145.mp3","Viva La Vida","Coldplay"},
            {"http://m2.music.126.net/j6vDiG4tYg-TJSIcP2_fmw==/5860396976108012.mp3","Take Me To Church","Hozier"},
            {"http://www.ikoumi.com/yunpan/ckwqARx8ea4MB/3925.mp3","RPG","SEKAI NO OWARI"},
            {"http://www.ikoumi.com/yunpan/ckw6GjPdtnIHd/1479.mp3","七里香","周杰伦"},
            {"http://m2.music.126.net/JM-iQ1rdg8QIDHSYANfOFQ==/7729566743320109.mp3","青春的约定","Snh48"},
            {"http://m2.music.126.net/oeghXhKKZ1NEBj9YBNs7aQ==/7969260279666041.mp3","宠爱","TFBoys"},
            {"http://m2.music.126.net/bIe5pwRSZpleydZVXbu7fg==/7697680908203334.mp3","给你","Snh48易嘉爱"},
            {"http://www.ikoumi.com/yunpan/ckwPMgU33MmyL/281b.mp3","恋するフォーチュンクッキー","AKB48"}
    };
    int fans[][]={{1021,237,602,517,903,603,117,85},{1,2,1,5,4,5,4,3}};
}
