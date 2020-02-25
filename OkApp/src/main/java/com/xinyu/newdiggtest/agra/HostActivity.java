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

public class HostActivity extends AppCompatActivity {
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

    private RelativeLayout mLayoutBigView;
    private RelativeLayout mLayoutSmallView;

    private String channelName = "channelid";


    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host1);

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

        mLayoutBigView = findViewById(R.id.remote_video_view_container);
        mLayoutSmallView = findViewById(R.id.local_video_view_container);

        mPlayer = new MediaPlayer();//初始化mediaPlayer
        mPlayer.reset();//重置mediaPlayer播放状态为初始状态
        mPlayer = MediaPlayer.create(this, R.raw.basic_ring);//装载需要播放的音频文件

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });

        mPlayer.start();

    }

    private void setupData() {
        Intent intent = getIntent();

        mSubscriber = intent.getStringExtra("subscriber");
//        Constant.PhoneCount.add(mSubscriber);
        channelName = intent.getStringExtra("channelName");

        mLayoutCallIn.setVisibility(View.GONE);
        mCallHangupBtn.setVisibility(View.VISIBLE);
        mCallTitle.setText(String.format(Locale.US, "%s is be called...", mSubscriber));
//        setupLocalVideo(); // Tutorial Step 3
        joinChannel(); // Tutorial Step 4

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mSubscriber = intent.getStringExtra("subscriber");

    }


    private CompoundButton.OnCheckedChangeListener oncheckChangeListerener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mRtcEngine.muteLocalAudioStream(isChecked);
        }
    };

    public void CallClickInit(View v) {
        switch (v.getId()) {

            case R.id.call_button_hangup: // call out canceled or call ended

                callOutHangup();
                break;
        }
    }

    private void callOutHangup() {

        String beCall = getIntent().getStringExtra("beCall");

        App.the().getM_agoraAPI().channelInviteEnd(channelName, /*mSubscriber*/beCall, 0);
        App.the().getM_agoraAPI().messageInstantSend(beCall, 0, "-2", "");

        finish();

    }


    private void addSignalingCallback() {


        App.the().getM_agoraAPI().callbackSet(new AgoraAPI.CallBack() {

            /**
             * other receiver call accept callback
             * @param channelID
             * @param account
             * @param uid
             * @param s2
             */
            @Override
            public void onInviteAcceptedByPeer(String channelID, String account, int uid, String s2) {
                Log.e(TAG, "onInviteAcceptedByPeer  channelID = " + channelID + "  account = " + account);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }
                        mCallTitle.setVisibility(View.GONE);
                    }
                });

            }


            /**
             * other receiver call refuse callback
             * @param channelID
             * @param account
             * @param uid
             * @param s2
             */

            @Override
            public void onInviteRefusedByPeer(final String channelID, final String account, final int uid, final String s2) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }

                        finish();
                    }
                });
            }


            /**
             * end call remote receiver callback
             * @param channelID
             * @param account
             * @param uid
             * @param s2
             */
            @Override
            public void onInviteEndByPeer(final String channelID, final String account, int uid, String s2) {
                Log.e(TAG, "onInviteEndByPeer channelID = " + channelID + " account = " + account);
                finish();
            }

            /**
             * end call local receiver callback
             * @param channelID
             * @param account
             * @param uid
             */
            @Override
            public void onInviteEndByMyself(String channelID, String account, int uid) {

                finish();
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

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.release();
        mPlayer = null;

        if (mRtcEngine != null) {
            mRtcEngine.stopPreview();
            mRtcEngine.leaveChannel();
        }
        mRtcEngine = null;

        RtcEngine.destroy();

    }


    // Tutorial Step 8
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
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

    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);

//        mRtcEngine.enableAudio();
//        mRtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_DEFAULT, 0);

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

    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {

        mLayoutBigView.removeAllViews();
//        mLayoutBigView.setTag(uid + "");
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        mLayoutBigView.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mLayoutBigView.setVisibility(View.VISIBLE);

    }


    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        RelativeLayout container = findViewById(R.id.remote_video_view_container);

        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
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
                    finish();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_STORAGE);
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA);
                    finish();
                }
                break;
            }
            case PERMISSION_REQ_ID_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    finish();
                }
                break;
            }
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();

        setupData();

    }


    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    setupRemoteVideo(uid);

                    setupLocalVideo();

                }
            });


        }

        @Override
        public void onUserOffline(int uid, int reason) {

            Log.e(TAG, "onUserOffline:" + uid);
        }


        @Override
        public void onUserJoined(int uid, int elapsed) {

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
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);

            Log.e(TAG, "onUserOffline:" + uid);


        }

    };


}
