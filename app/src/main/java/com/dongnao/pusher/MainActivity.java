package com.dongnao.pusher;

import android.hardware.Camera;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.dongnao.pusher.live.LivePusher;

public class MainActivity extends AppCompatActivity {
    private LivePusher livePusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);

        livePusher = new LivePusher(this, 800, 480, 800_000, 10, Camera.CameraInfo.CAMERA_FACING_BACK);
        //  设置摄像头预览的界面
        livePusher.setPreviewDisplay(surfaceView.getHolder());
    }

    public void switchCamera(View view) {
        livePusher.switchCamera();
    }

    public void startLive(View view) {
        // 某些平台会接rtmp流，然后将rtmp协议转成http-flv协议
        // 推流地址也可以是：rtmp://ip/myapp/mystream
        // 播放地址：rtmp://ip/myapp/mystream，mystream相当于密码
        livePusher.startLive("rtmp://your ip:8081/myapp/mystream");
    }

    public void stopLive(View view) {
        livePusher.stopLive();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        livePusher.release();
    }
}
