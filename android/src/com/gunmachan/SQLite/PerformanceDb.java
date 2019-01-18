package com.gunmachan.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

import com.badlogic.gdx.assets.AssetManager;

import asu.gunma.DbContainers.StudentMetric;

import static com.gunmachan.SQLite.SqlHelper.getsInstance;

/**
 * PerformanceDb class
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public class PerformanceDb {

    protected SqlHelper pDbHelper;

    /**
     * Constructor that always keeps the same table active given the application context.
     *
     * @param context
     */
    public PerformanceDb(Context context) {
        pDbHelper = getsInstance(context);
    }

    /**
     * Inserts tuple into table and returns the corresponding row id.
     *
     * @param sMetric
     * @return newRowId
     */
    public long dbInsertMetric(StudentMetric sMetric) {
        SQLiteDatabase db = pDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sMetric.COLUMN_FN, sMetric.getStudentFirstName());
        contentValues.put(sMetric.COLUMN_LN, sMetric.getStudentLastName());
        contentValues.put(sMetric.COLUMN_MODULE, sMetric.getStudentModule());
        contentValues.put(sMetric.COLUMN_GRADE, sMetric.getStudentGrade());
        long newRowId =
                db.insert(sMetric.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    /**
     * Selects tuple into table and makes a query that is returned as a Cursor.
     * Checks if Cursor is null and closes the dbRead instance.
     *
     * @return dbCursor
     */
    public StudentMetric getMetric(long id) {
        SQLiteDatabase db = pDbHelper.getReadableDatabase();
        Cursor cursor = db.query(StudentMetric.TABLE_NAME,
                new String[]{StudentMetric.COLUMN_ID, StudentMetric.COLUMN_FN, StudentMetric.COLUMN_LN,
                        StudentMetric.COLUMN_MODULE, StudentMetric.COLUMN_GRADE},
                StudentMetric.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        StudentMetric studentMetric = new StudentMetric(
                cursor.getInt(cursor.getColumnIndex(StudentMetric.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_FN)),
                cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_LN)),
                cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_MODULE)),
                cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_GRADE)));

        cursor.close();
        return studentMetric;
    }

    /**
     * Returns a list that contains every item from the table.
     * Queries the table using dbSelectRecords and retrieves the index from the ID column.
     *
     * @return itemIds
     */
    public List<StudentMetric> viewDb() {
        List<StudentMetric> metricItems = new ArrayList<StudentMetric>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + StudentMetric.TABLE_NAME + " ORDER BY " +
                StudentMetric.COLUMN_LN + " DESC";
        SQLiteDatabase db = pDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                StudentMetric studentMetric = new StudentMetric();
                studentMetric.setId(cursor.getInt(cursor.getColumnIndex(StudentMetric.COLUMN_ID)));
                studentMetric.setStudentFirstName(cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_FN)));
                studentMetric.setStudentLastName(cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_LN)));
                studentMetric.setStudentModule(cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_MODULE)));
                studentMetric.setStudentGrade(cursor.getString(cursor.getColumnIndex(StudentMetric.COLUMN_GRADE)));
                metricItems.add(studentMetric);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // close db connection
        db.close();

        // return student performance metric list
        return metricItems;
    }

    public int getStudentCount() {
        String countQuery = "SELECT  * FROM " + StudentMetric.TABLE_NAME;
        SQLiteDatabase db = pDbHelper.getReadableDatabase();
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
     * @param sMetric
     * @return indexes for updated tuples in db.
     */
    public int dbUpdateMetric(StudentMetric sMetric) {
        SQLiteDatabase db = pDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentMetric.COLUMN_FN, sMetric.getStudentFirstName());
        contentValues.put(StudentMetric.COLUMN_LN, sMetric.getStudentLastName());
        contentValues.put(StudentMetric.COLUMN_MODULE, sMetric.getStudentModule());
        contentValues.put(StudentMetric.COLUMN_GRADE, sMetric.getStudentGrade());

        //unfinished
        return db.update(StudentMetric.TABLE_NAME, contentValues, StudentMetric.COLUMN_ID + " = ?",
                new String[]{String.valueOf(sMetric.getId())});
    }

    /**
     * Selects a row in the table and uses the delete function to remove the
     * selected row from the table.
     *
     * @param sMetric
     */
    public void dbDeleteMetric(StudentMetric sMetric) {
        SQLiteDatabase db = pDbHelper.getWritableDatabase();
        db.delete(StudentMetric.TABLE_NAME, StudentMetric.COLUMN_ID + " = ?",
                new String[]{String.valueOf(sMetric.getId())});
        db.close();
    }
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
     */
    public void importCSV(String fileName) {
        AssetManager assetManager = new AssetManager();
        SQLiteDatabase db = pDbHelper.getWritableDatabase();
        InputStream inStream = null;
        try {
            inStream = assetManager.get(fileName);
        }  catch (Exception e) {
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
                ContentValues contentValues = new ContentValues(4);
                contentValues.put(StudentMetric.COLUMN_ID, columns[0].trim());
                contentValues.put(StudentMetric.COLUMN_FN, columns[1].trim());
                contentValues.put(StudentMetric.COLUMN_LN, columns[2].trim());
                contentValues.put(StudentMetric.COLUMN_MODULE, columns[3].trim());
                contentValues.put(StudentMetric.COLUMN_GRADE, columns[4].trim());
                db.insert(StudentMetric.TABLE_NAME, null, contentValues);
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

        File file = new File(exportDir, "SPM.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = pDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + StudentMetric.TABLE_NAME,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to export
                String arrStr[] = new String[]{curCSV.getString(1),curCSV.getString(2),
                        curCSV.getString(3), curCSV.getString(4)};
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
}
