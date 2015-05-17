package edu.cecyt9.ipn.practica19_multimedia;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends Activity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = "MyActivity";
    private MediaPlayer mediaPlayer;
    private VideoView videoview;
    private EditText editText;
    private ImageButton bPlay, bPause, bStop;
    private boolean pause;
    private String path;
    private int savePos = 0;
    private MediaController mediaControls;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        videoview = (VideoView) findViewById(R.id.surfaceView);
        editText = (EditText) findViewById(R.id.path);
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        editText.setText("http://coatl.cecyt9.ipn.mx/eoropeza/avisos/video.mp4");
        bPlay = (ImageButton) findViewById(R.id.play);
        bPlay.setOnClickListener(this);
        bPause = (ImageButton) findViewById(R.id.pause);
        bPause.setOnClickListener(this);
        bStop = (ImageButton) findViewById(R.id.stop);
        bStop.setOnClickListener(this);

        try {
            //set the media controller in the VideoView
            videoview.setMediaController(mediaControls);
            //set the uri of the video to be played
            videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        //videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                //if we have a position on savedInstanceState, the video playback should start from here
                videoview.seekTo(savePos);
                if (savePos == 0) {
                    videoview.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    videoview.pause();
                }
            }
        });
    }

    private void playVideo() {
        try {
            pause = false;
            path = editText.getText().toString();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            // mMediaPlayer.prepareAsync(); Para streaming
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.seekTo(savePos);
        } catch (Exception e) {
            Log.d(TAG, "ERROR: " + e.getMessage());
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play:
                if (mediaPlayer != null) {
                    if (pause) {
                        mediaPlayer.start();
                    } else {
                        playVideo();
                    }
                }
            case R.id.pause:
                if (mediaPlayer != null)
                {
                    pause = true;
                    mediaPlayer.pause();
                }
            case R.id.stop:
                if (mediaPlayer != null) {
                    pause = false;
                    mediaPlayer.stop();
                }
        }
    }


    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);
    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        int mVideoWidth = mediaPlayer.getVideoWidth();
        int mVideoHeight = mediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            mediaPlayer.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        playVideo();
    }


    public void surfaceChanged(SurfaceHolder surfaceholder,
                               int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (mediaPlayer != null & !pause) {
            mediaPlayer.pause();
        }
    }
    @Override public void onResume() {
        super.onResume();
        if (mediaPlayer != null & !pause) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        if (mediaPlayer != null) {
            int pos = mediaPlayer.getCurrentPosition();
            guardarEstado.putString("ruta", path);
            guardarEstado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        if (recEstado != null) {
            path = recEstado.getString("ruta");
            savePos = recEstado.getInt("posicion");
        }
    }
    
}





//public class MainActivity extends Activity
//{
//        private VideoView mVideoView;
//        @Override public void onCreate(Bundle savedInstanceState){
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_main);
//            mVideoView =(VideoView)findViewById(R.id.surface_view);
//
//
//            Uri path = Uri.parse("android.resource://edu.cecyt9.ipn.practica19_multimedia/" + R.raw.video);
//            mVideoView.setVideoURI(path);
//            //permitimos que el usuario pueda controlar la reproducción del vídeo mediante el objeto MediaController.
//            mVideoView.setMediaController(new MediaController(this));
//            mVideoView.start();
//        }
//    }