package com.example.multimediaplayer;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {


    Button btnOpenFile, btnOpenUrl, btnPlay, btnPause, btnStop, btnRestart;
    VideoView videoView;
    TextView tvStatus;

    // Media player for audio
    MediaPlayer mediaPlayer;

    // To store selected file/video URI
    Uri mediaUri;

    // Request code for file picker
    private static final int PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI elements with Java
        btnOpenFile = findViewById(R.id.btnOpenFile);
        btnOpenUrl = findViewById(R.id.btnOpenUrl);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnRestart = findViewById(R.id.btnRestart);
        videoView = findViewById(R.id.videoView);
        tvStatus = findViewById(R.id.tvStatus);

        //  OPEN FILE (Audio from storage)
        btnOpenFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*"); // Only audio files
            startActivityForResult(intent, PICK_FILE);
        });

        //  OPEN URL (Video streaming)
        btnOpenUrl.setOnClickListener(v -> {
            // Example video URL (you can change it)
            String url = "https://www.w3schools.com/html/mov_bbb.mp4";

            mediaUri = Uri.parse(url);

            // Show VideoView
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(mediaUri);

            tvStatus.setText("Video loaded from URL");
        });

        //  PLAY
        btnPlay.setOnClickListener(v -> {
            try {
                // If video is visible → play video
                if (videoView.getVisibility() == View.VISIBLE) {
                    videoView.start();
                    tvStatus.setText("Playing Video");
                }
                // Else play audio
                else if (mediaUri != null) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, mediaUri);
                    mediaPlayer.start();
                    tvStatus.setText("Playing Audio");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //  PAUSE
        btnPause.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                tvStatus.setText("Video Paused");
            } else if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                tvStatus.setText("Audio Paused");
            }
        });

        //  STOP
        btnStop.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
                tvStatus.setText("Video Stopped");
            }

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                tvStatus.setText("Audio Stopped");
            }
        });

        // RESTART
        btnRestart.setOnClickListener(v -> {
            if (videoView.getVisibility() == View.VISIBLE) {
                videoView.seekTo(0);
                videoView.start();
                tvStatus.setText("Video Restarted");
            } else if (mediaPlayer != null) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                tvStatus.setText("Audio Restarted");
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }

    //  When user selects audio file
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null) {

            mediaUri = data.getData(); // Get selected file URI

            // Hide video view (since this is audio)
            videoView.setVisibility(View.GONE);

            tvStatus.setText("Audio file loaded");
        }
    }
}