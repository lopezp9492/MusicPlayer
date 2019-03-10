package com.example.tejon.musicplayer;
// Pedro Lopez
// Made with tutorial from codingwithsara.com

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import java.net.*;




/* song list
    astronaut_beach_house
    extra_vehicular_leisure
    go_for_liftoff
    mercury_redstone
    moon_dance
    ticker_tape_parade
*/
public class MainActivity extends AppCompatActivity {

    // Resource ID's to pass to the media player
    final int[] resID = {
            R.raw.astronaut_beach_house,
            R.raw.extra_vehicular_leisure,
            R.raw.go_for_liftoff,
            R.raw.mercury_redstone,
            R.raw.moon_dance,
            R.raw.ticker_tape_parade
    };

    // This list is not used except to get a count of the number of songs in the list
    // List could be used to display the name of the song.
    ArrayList<String> songs;

    Button playBtn;
    Button previousBtn;
    Button nextBtn;
    Button testBtn;
    private static final int SERVER_PORT = 2048;
    private static final int CLIENT_PORT = 994;
    private static final String TAG = "MyActivity"; //For error logging



    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;
    int album_size;
    int track;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This list is not used except to get a count of the number of songs in the list
        // List could be used to display the name of the song.
        songs = new ArrayList<>();
        songs.add("astronaut_beach_house");
        songs.add("extra_vehicular_leisure");
        songs.add("go_for_liftoff");
        songs.add("mercury_redstone");
        songs.add("moon_dance");
        songs.add("ticker_tape_parade");
        album_size = songs.size();
        track = 0;


        playBtn = (Button) findViewById(R.id.playBtn);
        previousBtn = (Button) findViewById(R.id.previousBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        testBtn = (Button) findViewById(R.id.btnTest);

        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        // Media Player
        mp = MediaPlayer.create(this, resID[track]); //R.raw.mercury_redstone
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // Position Bar
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        //Volume Bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Thread (update positionBar and timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            positionBar.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };


    public String createTimeLabel(int time){
        String timeLabel = "!";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if(sec < 10 ) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;

    }

    // Play Button
    public void playBtnClick(View view) {
        //if not playing
        if (!mp.isPlaying()) {
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        } else {
            //if playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);

        }
    }

    // Previous Button
    public void previousBtnClick(View view) {
        if (mp.isPlaying()) {
            mp.stop();
            track = (track-1);
            if(track < 0 ){track = album_size;}

            // Media Player
            mp = MediaPlayer.create(this, resID[track]);
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f, 0.5f);
            totalTime = mp.getDuration();
            //mp.selectTrack(R.raw.astronaut_beach_house);
            mp.start();
        }
    }

    // Next Button
    public void nextBtnClick(View view) {

        if (mp.isPlaying()) {
            mp.stop();
            track = (track+1)%album_size;


            // Media Player
            mp = MediaPlayer.create(this, resID[track]);
            mp.setLooping(true);
            mp.seekTo(0);
            mp.setVolume(0.5f, 0.5f);
            totalTime = mp.getDuration();
            //mp.selectTrack(R.raw.astronaut_beach_house);
            mp.start();
        }
    }

    // Next Button
    public void testBtnClick(View view) {
        //Toast
        CharSequence testText = "Button Clicked";
        int long_duration = Toast.LENGTH_LONG;
        int short_duration = Toast.LENGTH_SHORT;

        Toast clicked_toast = Toast.makeText(this, testText, short_duration);
        clicked_toast.show();
        Log.v(TAG, "Button Clicked");


        DatagramSocket aSocket = null;
        try {
            // DEBUG
            testText = "Trying";
            clicked_toast = Toast.makeText(this, testText, short_duration);
            clicked_toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
            clicked_toast.show();
            Log.v(TAG, "Trying...");

            //Log.v(TAG, "Client Socket:" + CLIENT_PORT);
            aSocket = new DatagramSocket();
            int aqw = aSocket.getLocalPort();
            Log.v(TAG, "Client Socket:" + aqw);
            Log.v(TAG, "Socket Created...");


            // Message and InetAddress
            String mStr = "Hello from Client";
            byte [] m =  mStr.getBytes(); // Message
            // aHost = InetAddress.getByName("127.0.0.1"); // Server Address //IPV4
            InetAddress aHost = InetAddress.getByName("192.168.56.1"); // Server Address //IPV4
            //InetAddress aHost = InetAddress.getByName("10.39.56.132"); // Server Address //IPV4
            //InetAddress aHost = InetAddress.getByName("e80::90b9:4412:1ce2:cec4%5"); // Server Address // IPV6
            Log.v(TAG, "InetAddress Created...");


            // SEND
            DatagramPacket request = new DatagramPacket (m, m.length, aHost, SERVER_PORT);
            aSocket.send(request);
            Log.v(TAG, "DatagramPacket Request Sent...");


            // RECEIVE
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            Log.v(TAG, "DatagramPacket Received.");


            //Toast
            CharSequence text = "Empty";
            Charset  charset = Charset.defaultCharset();
            text = new String(reply.getData(), charset);
            Log.v(TAG, text.toString().substring(0, reply.getLength()));

            Toast toast = Toast.makeText(this, text, short_duration);
            toast.show();

            //System.out.println("Reply: " + new String(reply.getData()));
            Log.v(TAG, "Done.");


        }catch (SocketException e ) {
            Log.v(TAG, "Socket: " + e.getMessage());
        }catch (IOException e) {
            Log.v(TAG, "IO: " + e.getMessage());
        }finally {
            if(aSocket != null){
                aSocket.close();
                Log.v(TAG, "Socket closed.");
            }
            else
            {
                Log.v(TAG, "Socket was null.");
            }

        }

    }

}

