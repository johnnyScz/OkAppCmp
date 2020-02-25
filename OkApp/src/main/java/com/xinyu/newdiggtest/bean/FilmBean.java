package com.xinyu.newdiggtest.bean;

import java.util.List;

public class FilmBean {

    private OpBean op;
    
    private List<FilmListBean> data;

    public OpBean getOp() {
        return op;
    }

    public void setOp(OpBean op) {
        this.op = op;
    }

    public List<FilmListBean> getData() {
        return data;
    }

    public void setData(List<FilmListBean> data) {
        this.data = data;
    }


    public static class FilmListBean {
        /**
         * f_img : http://testok.xinyusoft.com/ok/upload/timg.jpg
         * f_director : 宫崎骏
         * f_movie_name : 千与千寻
         * f_uuid : 12313123
         */

        private String f_img;
        private String f_director;
        private String f_movie_name;
        private String f_uuid;

        public String getF_img() {
            return f_img;
        }

        public void setF_img(String f_img) {
            this.f_img = f_img;
        }

        public String getF_director() {
            return f_director;
        }

        public void setF_director(String f_director) {
            this.f_director = f_director;
        }

        public String getF_movie_name() {
            return f_movie_name;
        }

        public void setF_movie_name(String f_movie_name) {
            this.f_movie_name = f_movie_name;
        }

        public String getF_uuid() {
            return f_uuid;
        }

        public void setF_uuid(String f_uuid) {
            this.f_uuid = f_uuid;
        }
    }
}
