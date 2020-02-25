package com.xshell.xshelllib.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huang on 2017/8/13.
 * 数据库管理器
 */
public class DataLocalityManager {

    public static final String TABLE_VALUE = "f_value";
    public static final String TABLE_KEY = "f_key";

    public static final String TAG = "DataLocalityManager";

    public SQLiteDatabase database;
    //解决多线程并发
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private static DataLocalityManager manager;
    private String table = "t_c_map";
    private String Messagetable = "t_Message";//用户行为信息
    private String tablearr = "t_ttablearr";//删除数组的（旧需求）
    private Context mContext;
    private String dbName;
    private DataLocalityDatabaseHelper helper;

    private DataLocalityManager(Context context, String dbName) {
        this.mContext = context;
        this.dbName = dbName;
        helper = new DataLocalityDatabaseHelper(mContext, dbName);
    }

//    public void openSQLData() {
//        helper = new DataLocalityDatabaseHelper(mContext, dbName);
//     //   database = helper.getWritableDatabase();
//    }

    /**
     * 打开数据库对象
     * @return
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            database = helper.getWritableDatabase();
        }
        return database;
    }

    /**
     * 多线程下关闭
     */
    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            database.close();

        }
    }

    public static DataLocalityManager getInstance(Context context, String dbName) {
        if (manager == null) {
           // synchronized (TAG) {
                if (manager == null) {
                    manager = new DataLocalityManager(context, dbName);
                }
         //   }
        }
        return manager;
    }

    public boolean exits(String table) {
        boolean exits = false;
        String sql = "select * from sqlite_master where name=" + "'" + table + "'";
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.getCount() != 0) {
            exits = true;
        }
        return exits;
    }


    //用户行为记录写入
    public  void insertMessageData(ArrayList<String> dataList) throws JSONException {
        if (null == dataList || dataList.size() <= 0) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            database.beginTransaction();
            for(int i = 0; i < dataList.size(); i++) {
                //直接插入一条记录：
                String sql_add = "insert into " + Messagetable + "(f_value) values (?);";
                database.execSQL(sql_add, new String[]{dataList.get(i)});
            }

            //设置事务标志为成功，当结束事务时就会提交事务
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据行为：" + e.toString());
        } finally {
            try {
                if (null != database) {
                    //结束事务
                    database.endTransaction();
                 //   database.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //用户行为记录取出数据
    public  JSONArray getMessageData() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = null;
        try {
            db = helper.getReadableDatabase();
            //开启事务
            database.beginTransaction();
            Cursor cursor = database.query(Messagetable, null, null, null, null, null, "f_value " + "asc");
            //遍历游标，将数据存储在
            while (cursor.moveToNext()) {
                String valueTab = cursor.getString(cursor.getColumnIndex("f_value"));
                // String f_time = cursor.getString(cursor.getColumnIndex("f_time"));
                //  String value = valueTab + f_time;
                //JSONObject jsonObject = new JSONObject(valueTab);
                jsonArray.put(valueTab);
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            database.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "用户行为记录取出数据：" + e.toString());
            return null;
        } finally {
            try {
                if (null != database) {
                    //结束事务
                    database.endTransaction();
                   // database.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //删除用户行为记入数据
    public  void deleteMessageById(String value) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            database.beginTransaction();
            // 执行删除操作
           int count = database.delete(Messagetable, "f_value=?", new String[]{value + ""});
//            String sql = "delete from "+Messagetable+" where f_value in (" + value + ")";
//            database.execSQL(sql);
            //设置事务标志为成功，当结束事务时就会提交事务
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "删除用户行为记入数据：" + e.toString());
        } finally {
            try {
                if (null != database) {
                    //结束事务
                    database.endTransaction();
                 //   database.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void createTableSql() {
        database.execSQL("create table if not exists " + table + "(" +
                "id" + " integer primary key autoincrement, " +
                TABLE_KEY + " varchar, " +
                TABLE_VALUE + " varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
    }


    public void createTableMessageSql() {
      //  SQLiteDatabase writableDatabase = helper.getWritableDatabase();
//        database.execSQL("create table if not exists " + Messagetable + "(" +
//                "id" + " integer primary key autoincrement, f_value varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
        database.execSQL("create table if not exists " + Messagetable + "(" +
                "id" + " integer primary key autoincrement, f_value varchar) ");
     //   writableDatabase.close();
    }

    public void createArrayTableSql(String table1) {
        // String table = "t_" + table1;
        String table = tablearr;
        database.execSQL("CREATE TABLE IF NOT EXISTS " + table +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, id VARCHAR, f_key VARCHAR, f_value VARCHAR, f_time timestamp not null default (datetime('now', 'localtime'))) ");
    }

    //新的需求每根据一个key建一个表
    public void createArrayKeyTableSql(String table1) {
        String table = "t_c_" + table1;
        database.execSQL("create table if not exists " + table + "(" +
                "id" + " integer primary key autoincrement, f_id varchar, f_value varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
    }
}