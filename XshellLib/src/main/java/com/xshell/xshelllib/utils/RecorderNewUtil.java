package com.xshell.xshelllib.utils;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;
import java.util.Calendar;

public class RecorderNewUtil {

    // 以文件的形式保存
    private File recordFile;

    private static Context mContext;
    static RecorderNewUtil instance;


    private int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_YEAR);
    }

    private RecorderNewUtil() {
        checkSavePath();
    }


    public static RecorderNewUtil getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new RecorderNewUtil();
        }

        return instance;
    }

    MediaRecorder mMediaRecorder;

    public void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void recordOperation() {
        //创建MediaRecorder对象
        mMediaRecorder = new MediaRecorder();
        //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
        recordFile = new File(mContext.getFilesDir().getAbsolutePath() + File.separator + "recordFile", "recorder" + getCurrentSecord() + ".amr");
        //创建父文件夹
        try {
            //创建文件
            if (recordFile == null || !recordFile.exists()) {
                recordFile.createNewFile();
            }
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(recordFile.getAbsolutePath());
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkSavePath() {
        String recordFolder = mContext.getFilesDir().getAbsolutePath() + File.separator + "recordFile";
        File folderFile = new File(recordFolder);
        if (folderFile.exists()) {
            if (PreferenceXshellUtil.getInstance().getRecorderSavetime() != 0
                    && PreferenceXshellUtil.getInstance().getRecorderSavetime() < getCurrentDay()) {
                PreferenceXshellUtil.getInstance().setRecorderSavetime(getCurrentDay());
                try {
                    deleteFolderFiles(folderFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            folderFile.mkdirs();
        }

    }

    private String getCurrentSecord() {
        StringBuffer sBuffer = new StringBuffer();
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return sBuffer.append(hour + "_").append(minute + "_").append(second).toString();
    }

    private void deleteFolderFiles(File folderFile) {
        if (folderFile.isDirectory()) {
            String[] filelist = folderFile.list();
            if (filelist.length == 0) {
                return;
            }
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(folderFile.getAbsolutePath() + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    readfile.delete();
                }
            }
        }


    }


    public String getRecordFile() {
        if (recordFile != null && recordFile.exists()) {
            return recordFile.getAbsolutePath();

        } else {
            return "no file";
        }
    }


    public void stopRecording() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
    }


}