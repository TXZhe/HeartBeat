package adminzhezhe.heartbeat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

/**
 * Created by AdminZhezhe on 2016/8/29.
 */
public class Player implements OnBufferingUpdateListener,OnCompletionListener,OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Timer mTimer =new Timer();

    //initial
    public Player(SeekBar seekBar){
        super();
        this.seekBar=seekBar;
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        mTimer.schedule(timerTask,0,1000);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if(mediaPlayer.isPlaying() && seekBar.isPressed()==false){
                handler.sendEmptyMessage(0);
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg){
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if(duration>0){
                long pos = seekBar.getMax()*position/duration;
                seekBar.setProgress((int)pos);
            }
        }
    };

    public  void play(){
        mediaPlayer.start();
    }

    public void playUrl(String videoUrl)
    {
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    public void start()
    {
        mediaPlayer.start();
    }

    public void stop()
    {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.e("mediaPlayer", "onCompletion");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress = seekBar.getMax()
                * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.e(currentProgress + "% play", percent + " buffer");
    }

}