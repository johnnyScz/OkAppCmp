package com.xshell.xshelllib.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.xshell.xshelllib.application.AppContext;

public class PlaySoundsUtil {
	private static final Object lockObj = new Object();
	private static PlaySoundsUtil instance;
	private SoundPool soundPool;
	private AudioManager mgr;// 获得声音设备和设备音量
	private int streamVolume;

	public static PlaySoundsUtil getInstance() {
		if (instance == null) {
			synchronized (lockObj) {
				if (instance == null) {
					instance = new PlaySoundsUtil();
				}
			}
		}
		return instance;
	}

	public PlaySoundsUtil() {
		initSounds();
	}

	@SuppressLint("UseSparseArrays")
	private void initSounds() {
		// 初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,
		//第三个参数是声音的品质
		soundPool = new SoundPool(30, AudioManager.STREAM_MUSIC, 0);
		mgr = (AudioManager) AppContext.CONTEXT
				.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	public int loadSound(Context context, int raw) {
		// 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
		return soundPool.load(context, raw, 1);
	}

	public int loadSound(String path) {
		// 把资源中的音效加载到指定的ID(播放的时候就对应到这个ID播放就行了)
		int id = -1;
		try {
			id = soundPool.load(path, 1);
		} catch (Exception e) {
		}
		return id;
	}

	public void play(int sound, int uLoop) {
		soundPool.play(sound, streamVolume, streamVolume, 5, uLoop, 1f);
	}
}
