package com.gunmachan.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.badlogic.gdx.assets.AssetManager;

import static com.gunmachan.SQLite.SqlHelper.getsInstance;

/**
 * InstructorDb class
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public class InstructorDb {

    protected SqlHelper iDbHelper;

    /**
     * Constructor that always keeps the same table active given the application context.
     *
     * @param context
     */
    public InstructorDb(Context context) {
        iDbHelper = getsInstance(context);
    }

    /**
     * Inserts tuple into table and returns the corresponding row id.
     *
     * @param instructor
     * @return newRowId
     */
    public long dbInsertInstructor(Instructor instructor) {
        SQLiteDatabase db = iDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(instructor.COLUMN_FN, instructor.getInstructorFName());
        contentValues.put(instructor.COLUMN_LN, instructor.getInstructorLName());
        contentValues.put(instructor.COLUMN_INSTRUCTOR_ID, instructor.getInstructorID());
        contentValues.put(instructor.COLUMN_EMAIL, instructor.getInstructorEmail());
        long newRowId =
                db.insert(instructor.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }

    /**
     * Selects tuple into table and makes a query that is returned as a Cursor.
     * Checks if Cursor is null and closes the dbRead instance.
     *
     * @return dbCursor
     */
    public Instructor getInstructor(long id) {
        SQLiteDatabase db = iDbHelper.getReadableDatabase();
        Cursor cursor = db.query(Instructor.TABLE_NAME,
                new String[]{Instructor.COLUMN_ID, Instructor.COLUMN_FN, Instructor.COLUMN_LN,
                        Instructor.COLUMN_INSTRUCTOR_ID, Instructor.COLUMN_EMAIL},
                Instructor.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Instructor instructor = new Instructor(
                cursor.getInt(cursor.getColumnIndex(Instructor.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_FN)),
                cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_LN)),
                cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_INSTRUCTOR_ID)),
                cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_EMAIL)));

        cursor.close();
        return instructor;
    }

    /**
     * Returns a list that contains every item from the table.
     * Queries the table using dbSelectRecords and retrieves the index from the ID column.
     *
     * @return itemIds
     */
    public List<Instructor> viewDb() {
        List<Instructor> instructorItems = new ArrayList<Instructor>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Instructor.TABLE_NAME + " ORDER BY " +
                Instructor.COLUMN_LN + " DESC";
        SQLiteDatabase db = iDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Instructor instructor = new Instructor();
                instructor.setId(cursor.getInt(cursor.getColumnIndex(Instructor.COLUMN_ID)));
                instructor.setInstructorFName(cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_FN)));
                instructor.setInstructorLName(cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_LN)));
                instructor.setInstructorID(cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_INSTRUCTOR_ID)));
                instructor.setInstructorEmail(cursor.getString(cursor.getColumnIndex(Instructor.COLUMN_EMAIL)));
                instructorItems.add(instructor);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // close db connection
        db.close();

        // return instructor list
        return instructorItems;
    }

    public int getInstructorCount() {
        String countQuery = "SELECT  * FROM " + Instructor.TABLE_NAME;
        SQLiteDatabase db = iDbHelper.getReadableDatabase();
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
     * @param instructor
     * @return indexes for updated tuples in db.
     */
    public int dbUpdateMetric(Instructor instructor) {
        SQLiteDatabase db = iDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Instructor.COLUMN_FN, instructor.getInstructorFName());
        contentValues.put(Instructor.COLUMN_LN, instructor.getInstructorLName());
        contentValues.put(Instructor.COLUMN_INSTRUCTOR_ID, instructor.getInstructorID());
        contentValues.put(Instructor.COLUMN_INSTRUCTOR_ID, instructor.getInstructorEmail());

        //unfinished
        return db.update(Instructor.TABLE_NAME, contentValues, Instructor.COLUMN_ID + " = ?",
                new String[]{String.valueOf(instructor.getId())});
    }

    /**
     * Selects a row in the table and uses the delete function to remove the
     * selected row from the table.
     *
     * @param instructor
     */
    public void dbDeleteInstructor(Instructor instructor) {
        SQLiteDatabase db = iDbHelper.getWritableDatabase();
        db.delete(Instructor.TABLE_NAME, Instructor.COLUMN_ID + " = ?",
                new String[]{String.valueOf(instructor.getId())});
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
        SQLiteDatabase db = iDbHelper.getWritableDatabase();
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
                ContentValues contentValues = new ContentValues(4);
                contentValues.put(Instructor.COLUMN_ID, columns[0].trim());
                contentValues.put(Instructor.COLUMN_FN, columns[1].trim());
                contentValues.put(Instructor.COLUMN_LN, columns[2].trim());
                contentValues.put(Instructor.COLUMN_INSTRUCTOR_ID, columns[3].trim());
                contentValues.put(Instructor.COLUMN_EMAIL, columns[4].trim());
                db.insert(Instructor.TABLE_NAME, null, contentValues);
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

        File file = new File(exportDir, "Instructors.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = iDbHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM " + Instructor.TABLE_NAME, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to export
                String arrStr[] = new String[]{curCSV.getString(1), curCSV.getString(2),
                        curCSV.getString(3)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
    public boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase db = iDbHelper.getWritableDatabase();
        String Query;
        Cursor cursor = db.rawQuery("Select * from " + Instructor.TABLE_NAME + " where "
                + Instructor.COLUMN_EMAIL + " =?", new String[]{fieldValue});
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

