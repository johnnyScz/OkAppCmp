package com.xinyu.newdiggtest.ui.chat;

import org.json.JSONObject;

/**
 * 作者：Rance on 2016/12/14 14:13
 * 邮箱：rance935@163.com
 */
public class MessageInfo {


    private int type;
    private Object content;
    private String filepath;
    private int sendState;
    private String time;
    private String header;
    private String imageUrl;

    private long voiceTime = 0;
    private String msgId;
    private String fileUrl;//录音文件的上传url
    private JSONObject serble;//分享跳转需要

    private String userId;
    private String name;
    private String MsgType = "0";
    private int fromShare = 0;
    private String original_url;//原始Url

    private int playState = 0;//(0初始未播放或已结束，1播放中 2.暂停 )

    private int mySend = 0;//是我自己发送的


    public int getFromShare() {
        return fromShare;
    }

    public void setFromShare(int fromShare) {
        this.fromShare = fromShare;
    }

    public JSONObject getSerble() {
        return serble;
    }

    public void setSerble(JSONObject serble) {
        this.serble = serble;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }


    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getMySend() {
        return mySend;
    }

    public void setMySend(int mySend) {
        this.mySend = mySend;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }


    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", filepath='" + filepath + '\'' +
                ", sendState=" + sendState +
                ", time='" + time + '\'' +
                ", header='" + header + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceTime=" + voiceTime +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
