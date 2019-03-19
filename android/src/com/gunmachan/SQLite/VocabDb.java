package com.gunmachan.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    protected Context context;
    //private int id = 0;
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
        contentValues.put(vWord.COLUMN_KANJI, vWord.getKanjiSpelling());
        contentValues.put(vWord.COLUMN_KANA, vWord.getKanaSpelling());
        contentValues.put(vWord.COLUMN_ENG, vWord.getEngSpelling());
        contentValues.put(vWord.COLUMN_MODULE, vWord.getModuleCategory());
        contentValues.put(vWord.COLUMN_CORRECT_WORDS, vWord.getCorrectWords());
        contentValues.put(vWord.COLUMN_AUDIO, vWord.getAudio());
        long newRowId =
                db.insert(vWord.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    /**
     * Selects tuple in table and makes a query that is returned as a Cursor.
     * Checks if Cursor is null and closes the dbRead instance.
     *
     * @return dbCursor
     */
    public VocabWord getVocabWord(long id) {
        SQLiteDatabase db = vDbHelper.getReadableDatabase();
        Cursor cursor = db.query(VocabWord.TABLE_NAME,
                new String[]{VocabWord.COLUMN_KANJI, VocabWord.COLUMN_KANA,
                        VocabWord.COLUMN_ENG, VocabWord.COLUMN_MODULE, VocabWord.COLUMN_CORRECT_WORDS,VocabWord.COLUMN_AUDIO},
                VocabWord.COLUMN_ENG + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        VocabWord vocabWord = new VocabWord(
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_KANJI)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_KANA)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_ENG)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_MODULE)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_CORRECT_WORDS)),
                cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_AUDIO)));

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
                vocabWord.setKanjiSpelling(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_KANJI)));
                vocabWord.setKanaSpelling(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_KANA)));
                vocabWord.setEngSpelling(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_ENG)));
                vocabWord.setModuleCategory(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_MODULE)));
                vocabWord.setCorrectWords(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_CORRECT_WORDS)));
                vocabWord.setAudio(cursor.getString(cursor.getColumnIndex(VocabWord.COLUMN_AUDIO)));
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
        contentValues.put(VocabWord.COLUMN_KANJI, vWord.getKanjiSpelling());
        contentValues.put(VocabWord.COLUMN_KANA, vWord.getKanaSpelling());
        contentValues.put(VocabWord.COLUMN_ENG, vWord.getEngSpelling());
        contentValues.put(VocabWord.COLUMN_MODULE, vWord.getModuleCategory());
        contentValues.put(VocabWord.COLUMN_CORRECT_WORDS, vWord.getCorrectWords());
        contentValues.put(VocabWord.COLUMN_AUDIO, vWord.getAudio());

        return db.update(VocabWord.TABLE_NAME, contentValues, VocabWord.COLUMN_ENG + " =?",
                new String[]{String.valueOf(vWord.getEngSpelling())});
    }

    /**
     * Selects a row in the table and uses the delete function to remove the
     * selected row from the table.
     *
     * @param vWord
     */
    public void dbDeleteVocab(VocabWord vWord) {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        db.delete(VocabWord.TABLE_NAME, VocabWord.COLUMN_ENG + " =?",
                new String[]{String.valueOf(vWord.getEngSpelling())});
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
    public void importCSV(String fileName) throws IOException {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        InputStream inStream = null;

        try {
            //inStream = Gdx.files.local("/storage/emulated/0/Documents/" + fileName).read();
            inStream = Gdx.files.internal(fileName).read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        buffer.readLine();
        db.beginTransaction();
        String correctWordsList = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > 12 || columns.length < 4) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues(5);
                contentValues.put(VocabWord.COLUMN_KANJI, columns[0].trim());
                contentValues.put(VocabWord.COLUMN_KANA, columns[1].trim());
                contentValues.put(VocabWord.COLUMN_ENG, columns[2].trim());
                contentValues.put(VocabWord.COLUMN_MODULE,
                        fileName.substring(0, fileName.lastIndexOf('.')));
                for(int i = 3; i < columns.length-2; i++) {
                    if(columns[i] != "" && columns[i] != null){
                        correctWordsList += columns[i] + ",";
                    }
                }
                correctWordsList += columns[columns.length-2];
                contentValues.put(VocabWord.COLUMN_CORRECT_WORDS, correctWordsList);
                contentValues.put(VocabWord.COLUMN_AUDIO,columns[columns.length-1]);
                db.insert(VocabWord.TABLE_NAME, null, contentValues);
                correctWordsList = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public void importExternalCSV(String fileName) throws IOException {
        SQLiteDatabase db = vDbHelper.getWritableDatabase();
        FileInputStream inStream = null;

        try {
            File path = Environment.getExternalStorageDirectory();
            File csvInDocDirectory = new File(path, fileName);
            path.mkdirs();
            inStream = new FileInputStream(csvInDocDirectory);
            //inStream = Gdx.files.internal(fileName).read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        buffer.readLine();
        db.beginTransaction();
        String correctWordsList = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > 11 || columns.length < 4) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues(5);
                contentValues.put(VocabWord.COLUMN_KANJI, columns[0].trim());
                contentValues.put(VocabWord.COLUMN_KANA, columns[1].trim());
                contentValues.put(VocabWord.COLUMN_ENG, columns[2].trim());
                contentValues.put(VocabWord.COLUMN_MODULE,
                        fileName.substring(0, fileName.lastIndexOf('.')));
                for(int i = 3; i < columns.length-1; i++) {
                    if(columns[i] != "" && columns[i] != null){
                        correctWordsList += columns[i] + ",";
                    }
                }
                correctWordsList += columns[columns.length-1];
                contentValues.put(VocabWord.COLUMN_CORRECT_WORDS, correctWordsList);
                db.insert(VocabWord.TABLE_NAME, null, contentValues);
                correctWordsList = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void exportDB() {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "VocabList.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = vDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + VocabWord.TABLE_NAME, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = new String[]{curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public SqlHelper getvDbHelper() {
        return vDbHelper;
    }
}
