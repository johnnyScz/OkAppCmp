package com.xinyu.newdiggtest.agra;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.SurfaceView;
import android.view.View;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.App;


import java.util.Locale;


import io.agora.AgoraAPI;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


/**
 * Created by beryl on 2017/11/6.
 */

public class BeCallActivity extends AppCompatActivity {
    private final String TAG = "amtf";

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    private static final int PERMISSION_REQ_ID_STORAGE = PERMISSION_REQ_ID_CAMERA + 1;


    private RtcEngine mRtcEngine;

    private String mSubscriber;

    private CheckBox mCheckMute;
    private TextView mCallTitle;
    private ImageView mCallHangupBtn;
    private RelativeLayout mLayoutCallIn;

    private FrameLayout parent;
    private FrameLayout mLayoutSmallView;

    private String channelName = "channelid";


    MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        InitUI();
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)
                && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_STORAGE)) {
            initAgoraEngineAndJoinChannel();
        }
    }

    private void InitUI() {
        mCallTitle = (TextView) findViewById(R.id.meet_title);

        mCheckMute = (CheckBox) findViewById(R.id.call_mute_button);
        mCheckMute.setOnCheckedChangeListener(oncheckChangeListerener);

        mCallHangupBtn = (ImageView) findViewById(R.id.call_button_hangup);
        mLayoutCallIn = (RelativeLayout) findViewById(R.id.call_layout_callin);

        parent = findViewById(R.id.remote_video_view_container);
        mLayoutSmallView = (FrameLayout) findViewById(R.id.local_video_view_container);

        mPlayer = new MediaPlayer();//初始化mediaPlayer
        mPlayer.reset();//重置mediaPlayer播放状态为初始状态
        mPlayer = MediaPlayer.create(this, R.raw.basic_tones);//装载需要播放的音频文件
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });


    }

    private void setupData() {
        Intent intent = getIntent();

        mSubscriber = intent.getStringExtra("subscriber");
        channelName = intent.getStringExtra("channelName");

        mLayoutCallIn.setVisibility(View.VISIBLE);
        mCallHangupBtn.setVisibility(View.GONE);
        mCallTitle.setText(String.format(Locale.US, "%s is calling...", mSubscriber));


    }


    private CompoundButton.OnCheckedChangeListener oncheckChangeListerener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mRtcEngine.muteLocalAudioStream(isChecked);
        }
    };

    public void CallClickInit(View v) {
        switch (v.getId()) {

            case R.id.call_in_hangup:
                callInRefuse();
                break;

            case R.id.call_in_pickup:
                joinChannel(); // Tutorial Step 4
                App.the().getM_agoraAPI().channelInviteAccept(channelName, mSubscriber, 0, null);
                mLayoutCallIn.setVisibility(View.GONE);
                mCallHangupBtn.setVisibility(View.VISIBLE);
                mCallTitle.setVisibility(View.GONE);
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                break;

            case R.id.call_button_hangup: // call out canceled or call ended

                callOutHangup();
                break;
        }
    }


    private void callOutHangup() {
        App.the().getM_agoraAPI().channelInviteEnd(channelName, mSubscriber, 0);
        finish();
    }


    private void callInRefuse() {

        App.the().getM_agoraAPI().channelInviteRefuse(channelName, mSubscriber, 0, "{\"status\":0}");


        onEncCallClicked();
    }


    private void addSignalingCallback() {


        App.the().getM_agoraAPI().callbackSet(new AgoraAPI.CallBack() {

            @Override
            public void onInviteEndByPeer(final String channelID, String account, int uid, String s2) {
                onEncCallClicked();
            }

            @Override
            public void onMessageInstantReceive(String account, final int uid, String msg) {
                super.onMessageInstantReceive(account, uid, msg);
                if (msg.equals("-2")) {
                    Log.e(TAG, "onMessageInstantReceive:收到消息");
                    finish();
                }
            }

            /**
             * end call local receiver callback
             * @param channelID
             * @param account
             * @param uid
             */
            @Override
            public void onInviteEndByMyself(String channelID, String account, int uid) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onEncCallClicked();
                    }
                });
            }
        });
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addSignalingCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRtcEngine != null) {
            mRtcEngine.stopPreview();
            mRtcEngine.leaveChannel();
        }
        mRtcEngine = null;

        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        mPlayer.release();
        mPlayer = null;
        RtcEngine.destroy();
    }


    // Tutorial Step 8
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    // Tutorial Step 6
    public void onEncCallClicked() {
        finish();
    }

    // Tutorial Step 1
    private void initializeAgoraEngine() {


        String appID = getString(R.string.agora_app_id);

        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), appID, mRtcEventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupVideoProfile();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }

    // Tutorial Step 3
    private void setupLocalVideo() {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        mLayoutSmallView.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        mLayoutSmallView.setVisibility(View.VISIBLE);
        int ret = mRtcEngine.startPreview();
        Log.i(TAG, "setupLocalVideo startPreview enter << ret :" + ret);
    }

    // Tutorial Step 4
    private void joinChannel() {
        int ret = mRtcEngine.joinChannel(null, channelName, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
        Log.i(TAG, "joinChannel enter ret :" + ret);
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {

        parent.removeAllViews();
        parent.setTag(uid + "");
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        parent.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        parent.setVisibility(View.VISIBLE);
    }


    // Tutorial Step 7
    private void onRemoteUserLeft(int uid) {
        finish();

    }


    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {

    }


    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    onEncCallClicked();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_STORAGE);
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA);
                    onEncCallClicked();
                }
                break;
            }
            case PERMISSION_REQ_ID_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    onEncCallClicked();
                }
                break;
            }
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        setupData();
    }

    int romoteUid = 0;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onUserOffline(int uid, int reason) {

            Log.e(TAG, "被叫onUserOffline:" + uid);

        }


        @Override
        public void onUserJoined(final int uid, int elapsed) {

            romoteUid = uid;

            Log.e(TAG, "被叫onUserJoined远端有加入:" + uid);
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoMuted(uid, muted);
                }
            });

        }

        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);

            Log.e(TAG, "被叫onJoinChannelSuccess本地加入:" + uid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    setupRemoteVideo(romoteUid);

                    setupLocalVideo();

                }
            });


        }

    };


}
