package asu.gunma.DbContainers;

public class Instructor {
    public static final String TABLE_NAME = "InstructorDB";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FN = "FirstName";
    public static final String COLUMN_LN = "LastName";
    public static final String COLUMN_INSTRUCTOR_ID = "INSTRUCTOR_ID";
    public static final String COLUMN_EMAIL = "INSTRUCTOR_EMAIL";

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
    private String instructorID;
    private String instructorEmail;

    public Instructor() {
    }

    public Instructor(int id, String instructorFName, String instructorLName, String instructorID, String instructorEmail) {
        this.id = id;
        this.instructorFName = instructorFName;
        this.instructorLName = instructorLName;
        this.instructorID = instructorID;
        this.instructorEmail = instructorEmail;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public String getInstructorID(){ return instructorID;}

    public void setInstructorID(String instructorID) {
        this.instructorID = instructorID;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }
    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }
}
