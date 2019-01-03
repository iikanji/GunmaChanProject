package com.gunmachan.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

/**
 * SQLHelper extends the SQLiteOpenHelper class which contains useful API
 * to implement the CRUD operations of the database (CREATE, READ, UPDATE
 * and DELETE) by writing the subclasses in the SQLiteOpenHelper library.
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public final class SqlHelper extends SQLiteOpenHelper {
    private static SqlHelper sInstance;

    // increment when the schema is changed
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VocabWord_db";

    public static synchronized SqlHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SqlHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public SqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VocabWord.SQLITE_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SqlHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(VocabWord.SQLITE_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
