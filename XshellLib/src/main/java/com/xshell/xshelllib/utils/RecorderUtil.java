package com.xshell.xshelllib.utils;

import android.media.MediaRecorder;

public class RecorderUtil {
	private MediaRecorder recorder;

	/** 开始录音 */
	public void startRecorder(String paht) {
		if (recorder == null) {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置音频来源为mic
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);// 设置音频输出格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 设置编码器格式
			recorder.setOutputFile(paht);
			try {
				recorder.prepare();
				recorder.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	
	/** 停止录音 */
	public void stopRecorder() {
		try {
			if (recorder != null) {
				recorder.setOnErrorListener(null);
				recorder.setOnInfoListener(null);
				recorder.setPreviewDisplay(null);

				recorder.stop();
				recorder.release();
				recorder = null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	/** 重置录音机 */
	public void resetRecorder() {
		recorder.reset();
	}

	/** 释放资源 */
	public void release() {
		if (recorder != null) {
			recorder.release();
			recorder = null;
		}
	}
}
