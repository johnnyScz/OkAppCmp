package com.xshell.xshelllib.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{
	
	public static String DBNAME = "xinyusoft";
	
	public static int VERSION = 1;

	private String tablename = "";
	
	public static String CREATETAB = "";
			/*"create table if not exists datamanage ("
      + "userId integer primary key autoincrement,"       
      + "url  varchar not null,"
      + "requestcontent  varchar not null,"
      + "md5  varchar not null"
      + ")";*/
	
	public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		
	}
	
	public DBOpenHelper(Context context,String tablename) {
		// TODO Auto-generated const	ructor stub
		super(context, DBNAME, null, VERSION);
		this.tablename = tablename;
		CREATETAB = "create table if not exists "+this.tablename+" ("
				+ "userId integer primary key autoincrement,"
				+ "url  varchar not null,"
				+ "requestcontent  varchar not null,"
				+ "md5  varchar not null"
				+ ")";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATETAB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
