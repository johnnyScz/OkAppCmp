package com.xinyu.newdiggtest.bean;

public class ImItemMsgBean {
    private String msg_from;
    private String name;
    private String user_id;
    private String read_msg_time;
    private String valid_flag;
    private String msg_time = "";
    private String msg_id;
    private String msg_status;
    private String msg_type;

    private String original_url;

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }


    private Object msg_content;

    private Object image;

    private String nickname;


    private String head;
    private String chat_room_id;
    private String sender = "0";//发送人
    private String length;//文件的录音时长
    private String url;//录音文件文件上传之后的url
    private String type;

    private String msg_to;
    private String receiver_nickname;
    private String receiver_head;

    public String getMsg_to() {
        return msg_to;
    }

    public void setMsg_to(String msg_to) {
        this.msg_to = msg_to;
    }

    public String getReceiver_nickname() {
        return receiver_nickname;
    }

    public void setReceiver_nickname(String receiver_nickname) {
        this.receiver_nickname = receiver_nickname;
    }

    public String getReceiver_head() {
        return receiver_head;
    }

    public void setReceiver_head(String receiver_head) {
        this.receiver_head = receiver_head;
    }


    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(String chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(String msg_time) {
        this.msg_time = msg_time;
    }

    public Object getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public String getMsg_from() {
        return msg_from;
    }

    public void setMsg_from(String msg_from) {
        this.msg_from = msg_from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
