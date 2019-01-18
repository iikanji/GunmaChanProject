package com.gunmachan.SQLite;

public class Instructor {
    public static final String TABLE_NAME = "Vocab";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FN = "FirstName";
    public static final String COLUMN_LN = "LastName";
    public static final String COLUMN_INSTRUCTOR_ID = "INSTRUCTOR_ID";

    public static final String SQLITE_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_FN + " TEXT," +
                    COLUMN_LN + " TEXT," +
                    COLUMN_INSTRUCTOR_ID + " TEXT)";

    public static final String SQLITE_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private int id;
    private String instructorFName;
    private String instructorLName;
    private int instructorID;

    public Instructor() {
    }

    public Instructor(int id, String instructorFName, String instructorLName, int instructorID) {
        this.id = id;
        this.instructorFName = instructorFName;
        this.instructorLName = instructorLName;
        this.instructorID = instructorID;
    }

    public int getId() {
        return id;
    }

    public String getInstructorFName() {
        return instructorFName;
    }

    public void setInstructorFName(String instructorFName) {
        this.instructorFName = instructorFName;
    }

    public String getInstructorLName() {
        return instructorLName;
    }

    public void setInstructorLName(String instructorLName) {
        this.instructorLName = instructorLName;
    }

    public int getInstructorID(){ return instructorID;}

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }

    public void setId(int id) {
        this.id = id;
    }
}
