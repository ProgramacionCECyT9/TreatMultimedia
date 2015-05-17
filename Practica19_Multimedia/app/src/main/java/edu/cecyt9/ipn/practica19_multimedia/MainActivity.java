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
import android.widget.VideoView;
import android.view.SurfaceHolder;
import android.view.View;

public class MainActivity extends Activity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, View.OnClickListener {

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
            videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pozos));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        //videoview.requestFocus();
        videoview.setOnPreparedListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        // close the progress bar and play the video
        videoview.seekTo(savePos);
        videoview.start();

    }


    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play:
                if (mediaPlayer != null) {
                    if (pause) {
                        mediaPlayer.start();
                    } else {
                        //playVideo();
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

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
        videoview.pause();
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }


    @Override
    protected void onSaveInstanceState(Bundle guardarEstado) {
        super.onSaveInstanceState(guardarEstado);
        Log.d(TAG, "onSaveInstanceState");
        int pos = videoview.getCurrentPosition();
        guardarEstado.putString("ruta", path);
        guardarEstado.putInt("posicion", pos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle recEstado) {
        super.onRestoreInstanceState(recEstado);
        Log.d(TAG, "onRestoreInstanceState");
        path = recEstado.getString("ruta");
        savePos = recEstado.getInt("posicion");
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