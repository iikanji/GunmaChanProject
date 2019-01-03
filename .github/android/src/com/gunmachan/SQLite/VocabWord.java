package com.gunmachan.SQLite;

import android.provider.BaseColumns;

/**
 * The VocabWord class establishes a container that is used to define the
 * SQLite Table layout with a table name and corresponding column names.
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public class VocabWord {
    public static final String TABLE_NAME = "Vocab";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JPN = "JPN_Spelling";
    public static final String COLUMN_ENG = "ENG_Spelling";

    public static final String SQLITE_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_JPN + " TEXT," +
                    COLUMN_ENG + " TEXT)";

    public static final String SQLITE_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private int id;
    private String jpnSpelling;
    private String engSpelling;

    public VocabWord() {
    }

    public VocabWord(int id, String jpnSpelling, String engSpelling) {
        this.id = id;
        this.jpnSpelling = jpnSpelling;
        this.engSpelling = engSpelling;
    }

    public int getId() {
        return id;
    }

    public String getJpnSpelling() {
        return jpnSpelling;
    }

    public void setJpnSpelling(String jpnSpelling) {
        this.jpnSpelling = jpnSpelling;
    }

    public String getEngSpelling() {
        return engSpelling;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEngSpelling(String engSpelling) {
        this.engSpelling = engSpelling;
    }

}
