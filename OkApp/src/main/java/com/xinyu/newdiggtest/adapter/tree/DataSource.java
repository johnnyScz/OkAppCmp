package com.xinyu.newdiggtest.adapter.tree;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static List<NoteItem> getFiles(){
        List<NoteItem> files = new ArrayList<>();
        files.add(new NoteItem("aa","1",""));
        files.add(new NoteItem("bb","2","1"));
        files.add(new NoteItem("cc","3","1"));
        files.add(new NoteItem("dd","4","2"));
        files.add(new NoteItem("ff","5","2"));
        files.add(new NoteItem("gg","6","1"));
        files.add(new NoteItem("hh","7",""));
        files.add(new NoteItem("jj","8",""));
        files.add(new NoteItem("qq","9","3"));
        files.add(new NoteItem("ww","10","9"));
        files.add(new NoteItem("jj","11","3"));
        files.add(new NoteItem("jj","12","4"));
        files.add(new NoteItem("jj","13","4"));
        files.add(new NoteItem("jj","14","4"));
        files.add(new NoteItem("jj","15","4"));
        files.add(new NoteItem("jj","16","7"));
        files.add(new NoteItem("jj","17","7"));
        files.add(new NoteItem("jj","18","7"));

        return files;
    }
}
