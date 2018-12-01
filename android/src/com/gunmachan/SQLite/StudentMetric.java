package com.gunmachan.SQLite;

/**
 * The VocabWord class establishes a container that is used to define the
 * SQLite Table layout with a table name and corresponding column names.
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public class StudentMetric {
    public static final String TABLE_NAME = "StudentPerformanceMetrics";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FN = "FirstName";
    public static final String COLUMN_LN = "LastName";
    public static final String COLUMN_MODULE = "MODULE";
    public static final String COLUMN_GRADE = "GRADE";


    public static final String SQLITE_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_FN + " TEXT," +
                    COLUMN_LN + " TEXT," +
                    COLUMN_MODULE + " TEXT," +
                    COLUMN_GRADE + " TEXT)";

    public static final String SQLITE_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private int id;
    private String studentFirstName;
    private String studentLastName;
    private String studentModule;
    private String studentGrade;

    public StudentMetric() {
    }

    public StudentMetric(int id, String studentFirstName, String studentLastName,
                         String studentModule, String studentGrade) {
        this.id = id;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentModule = studentModule;
        this.studentGrade = studentGrade;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getStudentModule() {
        return studentModule;
    }

    public void setStudentModule(String studentModule) {
        this.studentModule = studentModule;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }
}