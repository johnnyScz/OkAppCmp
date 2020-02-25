package com.xinyu.newdiggtest.bean;

import java.io.Serializable;

public class CompanyBean implements Serializable {


    private static final long serialVersionUID = -172773442590850202L;
    /**
     * user_id : 453
     * session_id : db0d1391-6e48-40a2-b338-54ee3f7efd32
     * company_id : 1
     * logo : http://121.40.177.191:8880/api2e/upload/c7a785153a55c0f591172a19219c59c7.png
     * name : 芯与科技
     * update_url : http://120.55.187.58:8085/xversion/ESBServlet?command=vms.getChangedListAction
     * download_url : http://120.55.187.58:8085/xversion/xvfiles
     * app_name : ok_company_mobile
     * load_logo : http://121.40.177.191:8880/api2e/upload/xinyu.png.png
     */

    private String user_id;
    private String session_id;
    private String company_id;
    private String logo;
    private String name;
    private String update_url;
    private String download_url;
    private String app_name;
    private String load_logo;


    private int isSelect = 0;//是否是选中的公司


    public CompanyBean(String cname, String clogo) {

        this.name = cname;
        this.logo = clogo;

    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getLoad_logo() {
        return load_logo;
    }

    public void setLoad_logo(String load_logo) {
        this.load_logo = load_logo;
    }


}
