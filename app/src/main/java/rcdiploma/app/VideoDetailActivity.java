package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoDetailActivity extends AppCompatActivity {

    VideoView videoView;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        videoView = findViewById(R.id.video_detail_videoview);
        sp = getSharedPreferences(ConstantUrl.PREF,MODE_PRIVATE);

        videoView.setVideoPath(sp.getString(ConstantUrl.VIDEO_URL,""));

        MediaController mediaController = new MediaController(VideoDetailActivity.this);
        videoView.setMediaController(mediaController);
        videoView.start();

    }
}