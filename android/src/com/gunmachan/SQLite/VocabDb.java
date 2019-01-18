package com.gunmachan.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.badlogic.gdx.assets.AssetManager;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.VocabWord;

import static com.gunmachan.SQLite.SqlHelper.getsInstance;

/**
 * VocabDb class
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public final class VocabDb {
    protected SqlHelper vDbHelper;

    /**
     * Constructor that always keeps the same table active given the application context.
     *
     * @param context
     */
    public VocabDb(Context context) {
        vDbHelper = getsInstance(context);
    }

    /**
     * Inserts tuple into table and returns the corresponding row id.
     *
     * @param vWord
     * @return newRowId
     */
    public long dbInsertVocab(VocabWord vWord) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(vWord.COLUMN_JPN, vWord.getJpnSpelling());
        contentValues.put(vWord.COLUMN_ENG, vWord.getEngSpelling());
        long newRowId =
                db.insert(vWord.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    /**
     * Selects tuple into table and makes a query that is returned as a Cursor.
     * Checks if Cursor is null and closes the dbRead instance.
     *
     * @return dbCursor
     */
    public VocabWord getVocabWord(long id) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        Cursor cursor = db.query(VocabWord.TABLE_NAME,
                new String[]{VocabWord.COLUMN_ID, VocabWord.COLUMN_JPN, VocabWord.COLUMN_ENG},
                VocabWord.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        VocabWord vocabWord = new VocabWord(
                cursor.getInt(cursor.getColumnIndex(VocabWord.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_JPN)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_ENG)));

        cursor.close();
        return vocabWord;
    }

    /**
     * Returns a list that contains every item from the table.
     * Queries the table using dbSelectRecords and retrieves the index from the ID column.
     *
     * @return itemIds
     */
    public List<VocabWord> viewDb() {
        List<VocabWord> vocabItems = new ArrayList<VocabWord>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + VocabWord.TABLE_NAME + " ORDER BY " +
                VocabWord.COLUMN_ENG + " DESC";
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VocabWord vocabWord = new VocabWord();
                vocabWord.setId(cursor.getInt(cursor.getColumnIndex(VocabWord.COLUMN_ID)));
                vocabWord.setJpnSpelling(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_JPN)));
                vocabWord.setEngSpelling(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_ENG)));

                vocabItems.add(vocabWord);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // close db connection
        db.close();

        // return vocab list
        return vocabItems;
    }

    public int getVocabCount() {
        String countQuery = "SELECT  * FROM " + VocabWord.TABLE_NAME;
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Returns the index values for the rows of the table that were updated
     * given an update query.
     *
     * @param vWord
     * @return indexes for updated tuples in db.
     */
    public int dbUpdateVocab(VocabWord vWord) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VocabWord.COLUMN_JPN, vWord.getJpnSpelling());
        contentValues.put(VocabWord.COLUMN_ENG, vWord.getEngSpelling());

        //unfinished
        return db.update(VocabWord.TABLE_NAME, contentValues, VocabWord.COLUMN_ID + " = ?",
                new String[]{String.valueOf(vWord.getId())});
    }

    /**
     * Selects a row in the table and uses the delete function to remove the
     * selected row from the table.
     *
     * @param vWord
     */
    public void dbDeleteVocab(VocabWord vWord) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        db.delete(VocabWord.TABLE_NAME, VocabWord.COLUMN_ID + " = ?",
                new String[]{String.valueOf(vWord.getId())});
        db.close();
    }

    //
    // SEARCH FUNCTION BASED ON WHETHER ENG = NULL OR JPN = NULL
    //


    /**
     * Uses an asset manager to open the locally stored CSV and completes
     * a database transaction that parses the comma separated items into
     * the corresponding vocabulary table.
     * If it cannot open the file or the file is empty, the IO exception
     * is caught and the stacktrace is printed.
     * If the table is not properly formatted with the correct number of
     * columns, then the extra columns are skipped and a log message is displayed.
     *
     * @param fileName
     * */
    public void importCSV(String fileName) {
        AssetManager assetManager = new AssetManager();
        SQLiteDatabase db = vDbHelper.getWritableDatabase();

        InputStream inStream = null;
        try {
            inStream = assetManager.get(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 3) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues(2);
                contentValues.put(VocabWord.COLUMN_ID, columns[0].trim());
                contentValues.put(VocabWord.COLUMN_JPN, columns[1].trim());
                contentValues.put(VocabWord.COLUMN_ENG, columns[2].trim());
                db.insert(VocabWord.TABLE_NAME, null, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void exportDB() {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "VocabList.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = vDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + VocabWord.TABLE_NAME,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to export
                String arrStr[] = new String[]{curCSV.getString(1),curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public SqlHelper getvDbHelper() {
        return vDbHelper;
    }
}
