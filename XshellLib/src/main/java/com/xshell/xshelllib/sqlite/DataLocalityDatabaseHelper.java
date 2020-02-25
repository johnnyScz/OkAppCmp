package com.xshell.xshelllib.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zzy on 2016/8/28.
 * 数据本地化的帮助类
 */
public final class DataLocalityDatabaseHelper extends SQLiteOpenHelper {


    public DataLocalityDatabaseHelper(final Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);

    }


    /**
     * 1-->2 add header table
     * 2-->3 update info
     * 3--> update info haha
     */
    public static final int DB_VERSION = 1;
   // public static final String DB_NAME = "dataLocality";

    /**
     * Creates database the first time we try to open it.
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {

        onUpgrade(db, 0, DB_VERSION);
    }


    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldV, final int newV) {
        for (int version = oldV + 1; version <= newV; version++) {
            upgradeTo(db, version);
        }
    }

    /**
     * Upgrade database from (version - 1) to version.
     */
    private void upgradeTo(SQLiteDatabase db, int version) {
        switch (version) {
            case 1:
                createDataLocalityTable(db);
                break;
//            case 2:
//                createHeadersTable(db);
//                break;
//            case 3:
//                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_IS_PUBLIC_API,
//                        "INTEGER NOT NULL DEFAULT 0");
//                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOW_ROAMING,
//                        "INTEGER NOT NULL DEFAULT 0");
//                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_ALLOWED_NETWORK_TYPES,
//                        "INTEGER NOT NULL DEFAULT 0");
//                break;
//            case 103:
//                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI,
//                        "INTEGER NOT NULL DEFAULT 1");
//                makeCacheDownloadsInvisible(db);
//                break;
//            case 4:
//                addColumn(db, DB_TABLE, Downloads.Impl.COLUMN_BYPASS_RECOMMENDED_SIZE_LIMIT,
//                        "INTEGER NOT NULL DEFAULT 0");
//                break;
            default:
                throw new IllegalStateException("Don't know how to upgrade to " + version);
        }
    }


    private void createDataLocalityTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists t_timestamp(" +
                "_id integer primary key autoincrement," +
                "f_timestamp char(13)," +
                "f_datatype varchar)");

    }
}