package com.example.tejon.musicplayer;
// Pedro Lopez
// 015892940
// made with tutorial from codingwithsara.com

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        //Media Player
        mp = MediaPlayer.create(this, R.raw.mercury_redstone);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);

        //Volume Bar
        volumeBar  = (SeekBar) findViewById(R.id.volumeBar);


    }
    public void playBtnClick(View view){
        //if not playing
        if(!mp.isPlaying()){
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }else{
            //if playing
            mp.stop();
            playBtn.setBackgroundResource(R.drawable.play);

        }

    }

}
