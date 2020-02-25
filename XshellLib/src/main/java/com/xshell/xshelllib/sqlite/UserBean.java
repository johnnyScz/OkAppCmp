package com.xshell.xshelllib.sqlite;

public class UserBean {
	private int userId;
	private String url;
	private String requestcontent;
	private String md5;


	public int getUserId() {
		return userId;
	}

	public String getUrl() {
		return url;
	}

	public String getRequestcontent() {
		return requestcontent;
	}

	public String getMd5() {
		return md5;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setRequestcontent(String requestcontent) {
		this.requestcontent = requestcontent;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}





}
