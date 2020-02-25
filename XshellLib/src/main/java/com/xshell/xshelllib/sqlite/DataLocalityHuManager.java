package com.xshell.xshelllib.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.xshell.xshelllib.utils.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by huang on 2017/8/13.
 * 数据库管理器
 */
public class DataLocalityHuManager {

    public static final String TABLE_VALUE = "f_value";
    public static final String TABLE_KEY = "f_key";

    public static final String TAG = "DataLocalityManager";

    //public SQLiteDatabase database;
    private static DataLocalityHuManager manager;
    private String table = "t_c_map";
    private String Messagetable = "t_Message";//用户行为信息
    private String tablearr = "t_ttablearr";//删除数组的（旧需求）
    private Context mContext;
    private String dbName;
    private Handler handler;
    boolean isFlay = true;
    private DataLocalityDatabaseHelper helper;

    private DataLocalityHuManager(Context context, String dbName) {
        this.mContext = context;
        this.dbName = dbName;
    }

    public void openSQLData() {
        synchronized (TAG) {
            helper = new DataLocalityDatabaseHelper(mContext, dbName);
            //  database = helper.getWritableDatabase();
        }
    }

    public static DataLocalityHuManager getInstance(Context context, String dbName) {
        // if (manager == null) {
        //   synchronized (TAG) {
        if (manager == null) {
            manager = new DataLocalityHuManager(context, dbName);
        }
        //  }
        //   }
        return manager;
    }

//    public SQLiteDatabase getDatabase() {
//        return database;
//    }

    //取出map集合数据库
    public synchronized String getData(String key) {
        String value = "";
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
//            db = helper.getWritableDatabase();
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + TABLE_VALUE + " from " + table + " where " + TABLE_KEY + "=" + "'" + key + "'";//根据key查value
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    value = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                    cursor.moveToNext();
                }
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    //map集合存入数据库
    public synchronized void insertData(String key, String value) throws JSONException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            //判断是否有key
            String sql = "select " + TABLE_VALUE + " from " + table + " where " + TABLE_KEY + "=" + "'" + key + "'";//根据key查询value
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() != 0) {  //有的话，更新
                ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_VALUE, value);
                int update = db.update(table, contentValues, TABLE_KEY + "=?", new String[]{key});
           //     Log.e("huang", "更新：" + update);
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TABLE_KEY, key);
                contentValues.put(TABLE_VALUE, value);
                long insert = db.insert(table, null, contentValues);
            //    Log.e("huang", "insert：" + insert);
            }
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //数组写入
    public synchronized void insertArrayData(String key, String id, String value) throws JSONException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            //  String table = "t_" + table1;
            //直接插入一条记录：
            //String sql_add = "insert into " + tablearr + "(f_id,f_key,f_value) values (?,?,?);";
            //database.execSQL(sql_add, new String[]{id, key, value});
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", id);
            contentValues.put("f_key", key);
            contentValues.put("f_value", value);
            db.insert(tablearr, null, contentValues);
            cursor.close();
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //数组取出数据 正asc,倒desc
    public synchronized JSONArray getArrayData(String key, int pageNum, int dataNum, String descAndAsc) throws JSONException {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
//            db = helper.getWritableDatabase();
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + "f_value" + " from " + tablearr + " where " + "f_key" + "=" + "'" + key + "'";//根据key查value
            Cursor cursor1 = db.rawQuery(sql, null);
            if (cursor1 != null && cursor1.moveToFirst()) {
                while (!cursor1.isAfterLast()) {
                    JSONArray jsonArray = new JSONArray();
                    if (pageNum == 0 && dataNum == 0) {
                        cursor = db.query(tablearr, null, null, null, null, null, "id " + descAndAsc);
                        //遍历游标，将数据存储在
                        while (cursor.moveToNext()) {
                            String valueTab = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                            JSONObject jsonObject = new JSONObject(valueTab);
                            jsonArray.put(jsonObject);
                        }
                        cursor1.moveToNext();
                        cursor1.close();
                        cursor.close();
                        return jsonArray;
                    } else {
                        int num = (pageNum - 1) * dataNum;//算出分页第几个开始
                        cursor = db.query(tablearr, null, null, null, null, null,
                                "id " + descAndAsc + " limit " + dataNum + " offset " + num);//分页查询数据num-是分页数，pageNum-几条数据
                        //遍历游标，将数据存储在
                        while (cursor.moveToNext()) {
                            String valueTab = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                            JSONObject jsonObject = new JSONObject(valueTab);
                            jsonArray.put(jsonObject);
                        }
                        cursor1.moveToNext();
                        cursor1.close();
                        cursor.close();
                        return jsonArray;
                    }
                }
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    //根据key（表），存入id和value到数据库中
    public synchronized void insertIdArrayData(String table1, ArrayList<String> valuelist, ArrayList<String> f_id) {
        String table = "t_c_" + table1;
        if (null == valuelist || valuelist.size() <= 0 || null == table1) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            for (int i = 0; i < valuelist.size(); i++) {
                int id = Integer.valueOf(f_id.get(i));
                String value = valuelist.get(i);
                //判断是否有key
                String sql = "select " + "f_value" + " from " + table + " where " + "f_id" + "=" + "'" + id + "'";//根据key查询value
                Cursor cursor = db.rawQuery(sql, null);
                if (cursor.getCount() != 0) {  //有的话，更新
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("f_value", value);
                    int update = db.update(table, contentValues, "f_id" + "=?", new String[]{f_id.get(i)});
                //    Log.e("huang", "新需求更新：" + update);
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("f_id", id);
                    contentValues.put("f_value", value);
                    long insert = db.insert(table, null, contentValues);
              //      Log.e("huang", "新需求insert：" + id);
                }
                cursor.close();
            }

            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //1.根据数组（id）来查询对应的value
//    public synchronized JSONArray getIdData(String table1, String[] idArr) {
    public  JSONArray getIdData(String table1, String idArr) {
        String table = "t_c_" + table1;
        JSONArray jsonArray = new JSONArray();
        if (null == idArr || idArr.length() <= 0 || null == table1) {
            return null;
        }
        SQLiteDatabase db = null;
        try {
//            db = helper.getWritableDatabase();
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            String sql = "select " + "f_value" + " from " + table + " where " + "f_id in (" + idArr + ") order by f_id";
//            for (int i = 0; i < idArr.length; i++) {
//                String id = idArr[i];
            // String sql = "select " + "f_value" + " from " + table + " where " + "f_id" + "=" + "'" + id + "'";//根据id查value
            // Log.i("huang", "sql:" + sql);
            Cursor cursor = db.rawQuery(sql, null);
         //   Log.e("huang", "cursor:" + cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                    JSONObject jsonObject = new JSONObject(value);
                    jsonArray.put(jsonObject);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            // }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "根据id找value：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        db.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //2根据单个id来查询此id的前或后的数量
    public JSONArray getZDiDData(String table1, String idm, String direction, String num) throws JSONException {
        String table = "t_c_" + table1;
        int id = Integer.valueOf(idm);
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
//            db = helper.getWritableDatabase();
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            if ("a".equals(direction)) {
                String sql = "select " + "f_value" + " from " + table
                        + " where " + "f_id" + ">" + id + " order by" + " "
                        + "f_id" + " asc" + " limit " + num;//根据key查value
                Log.i("huang", "后sql:" + sql);
                cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String value = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                        JSONObject jsonObject = new JSONObject(value);
                        jsonArray.put(jsonObject);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            } else if ("b".equals(direction)) {
                String sql = "select " + "f_value" + " from " + table + " where "
                        + "f_id" + "<" + id + " order by" + " "
                        + "f_id" + " desc" + " limit " + num;//根据key查value
                Log.i("huang", "前sql:" + sql);
                cursor = db.rawQuery(sql, null);
                if (cursor != null && cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        String value = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));//f_value
                        JSONObject jsonObject = new JSONObject(value);
                        jsonArray.put(jsonObject);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (Exception e) {
            Log.e("huang", "存入数据：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //根据key（表名）查询对应的数据
    public synchronized JSONArray getKeyAllData(String table1, String sort) {
        Log.e("huanghu", sort);
        String table = "t_c_" + table1;
        if (null == table1) {
            return null;
        }
        SQLiteDatabase db = null;
        JSONArray jsonArray = null;
        try {
//            db = helper.getWritableDatabase();
            db = helper.getReadableDatabase();
            //开启事务
            db.beginTransaction();
            jsonArray = new JSONArray();
//
//            Cursor cursor = db.query(table, null, null, null, null, null, "f_id " + sort);
//            //遍历游标，将数据存储在
//            while (cursor.moveToNext()) {
//                String valueTab = cursor.getString(cursor.getColumnIndex("f_value"));
//                // String f_time = cursor.getString(cursor.getColumnIndex("f_time"));
//                //  String value = valueTab + f_time;
//                JSONObject jsonObject = new JSONObject(valueTab);
//                jsonArray.put(jsonObject);
//            }
//            cursor.close();


            String sql = "select " + "f_value" + " from " + table + " order by f_id " + sort;
            //Log.e("huanghu", sql);
//            for (int i = 0; i < idArr.length; i++) {
//                String id = idArr[i];
            // String sql = "select " + "f_value" + " from " + table + " where " + "f_id" + "=" + "'" + id + "'";//根据id查value
            // Log.i("huang", "sql:" + sql);
            Cursor cursor = db.rawQuery(sql, null);
       //     Log.e("huang", "cursor:" + cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex(TABLE_VALUE));
                    JSONObject jsonObject = new JSONObject(value);
                    jsonArray.put(jsonObject);
                  //  Log.e("huanghu", jsonObject.getString("recommendid"));
                    cursor.moveToNext();
                }
                cursor.close();
            }

            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
            return jsonArray;
        } catch (JSONException e) {
            Log.e("huang", "根据key（表名）查询对应：" + e.toString());
            return null;
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //删除key（表）对应的所有数据
    public synchronized void deleteKeydata(String table1) {
        String table = "t_c_" + table1;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            //开启事务
            db.beginTransaction();
            //先查询数组里面的图片数组
            Cursor cursor = db.query(table, null, null, null, null, null, "f_id " + "asc");
            //遍历游标，将数据存储在
            while (cursor.moveToNext()) {
                String valueTab = cursor.getString(cursor.getColumnIndex("f_value"));
                // String f_time = cursor.getString(cursor.getColumnIndex("f_time"));
                //  String value = valueTab + f_time;
                JSONObject jsonObject = new JSONObject(valueTab);
                if (jsonObject.has("imgarr")) {
                    JSONArray imgarr = jsonObject.getJSONArray("imgarr");
                    for (int i = 0; i < imgarr.length(); i++) {
                        final String imgPath = imgarr.getString(i);
                        // new Thread(new Runnable() {
                        //      @Override
                        //      public void run() {
                        //   Log.e("huang", "图片删除路径：" + imgPath);
                        File file = new File(imgPath);
                        if (null != file) {
                            TimeUtil.deleteFile(file);
                        }
                        //    }
                        //     }).start();
                    }
                }
            }

            Log.e("huang", "删除表==========" + table);
            // db.execSQL("delete from " + table);
            int delete = db.delete(table, null, null);
            Log.e("huang", "删除表==========xiamia" + delete);
            cursor.close();
            //删除表
            //  Log.e("huang", "删除表=========="+table);
            //设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("huang", "删除表：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    if (db.isOpen()) {
                        if ("t_c_1151_gtb_recommendinfo_list".equals(table)) {
                            db.close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //删除数据库指定日期数据
    public synchronized void deleteData(String data, final String deleteAll, String timeStr) {
        data = data + " 23:59:59";
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();//开启事物
            //查询数据库中所有表
            String sql = "select name from sqlite_master where type='table' order by name";//
            Cursor cursor = db.rawQuery(sql, null);
            Log.e("huang", cursor.getCount() + "");
            if (cursor != null && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    //遍历出表名
                    final String name = cursor.getString(0);
                    String substring = name.substring(0, 4);
                    if ("t_c_".equals(substring)) {
                        Log.e("huang", name);
                        final String finalData = data;
                        if ("part".equals(deleteAll)) {
                            //删除表里指定时间的数据
                            //String sqlde="delete from "+name+" where f_time<="+"datetime("+"'"+data+"'"+")";
                            String sqlde = "DELETE FROM " + name + " WHERE f_time BETWEEN " + "'" + finalData + "'" + " AND " + "'" + timeStr + "'";
                            Log.e("huang", "shuju：" + sqlde);
                            db.execSQL(sqlde);
                        } else if ("all".equals(deleteAll)) {
                            db.execSQL("delete from " + name);
                            Log.e("huang", "所有删除表");
                        }
                    }
                }
            }
            cursor.close();
            db.setTransactionSuccessful();//提交事务
        } catch (Exception e) {
            Log.e("huang", "指定日期数据：" + e.toString());
        } finally {
            try {
                if (null != db) {
                    //结束事务
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createTableSql() {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.execSQL("create table if not exists " + table + "(" +
                "id" + " integer primary key autoincrement, " +
                TABLE_KEY + " varchar, " +
                TABLE_VALUE + " varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
        writableDatabase.close();
    }


    public void createTableMessageSql() {
//        database.execSQL("create table if not exists " + Messagetable + "(" +
//                "id" + " integer primary key autoincrement, f_value varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
//        database.execSQL("create table if not exists " + Messagetable + "(" +
//                "id" + " integer primary key autoincrement, f_value varchar) ");
    }

    public void createArrayTableSql(String table1) {
        // String table = "t_" + table1;
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        String table = tablearr;
        writableDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + table +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, id VARCHAR, f_key VARCHAR, f_value VARCHAR, f_time timestamp not null default (datetime('now', 'localtime'))) ");
        writableDatabase.close();
    }

    //新的需求每根据一个key建一个表
    public synchronized void createArrayKeyTableSql(String table1) {
        String table = "t_c_" + table1;
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        writableDatabase.execSQL("create table if not exists " + table + "(" +
                "id" + " integer primary key autoincrement, f_id integer, f_value varchar, f_time timestamp not null default (datetime('now', 'localtime'))) ");
//        if (database.isOpen()) {
//            database.close();
//        }
        writableDatabase.close();
    }
}