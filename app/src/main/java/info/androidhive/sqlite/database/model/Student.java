package info.androidhive.sqlite.database.model;

public class Student {
    public static final String TABLE_NAME = "Course Registration Waiting List";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NEW_STUDENTS = "student";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_EDIT = "Student Information";

    private int id; // Declare id as an int variable
    private String student; // Declare student as a string variable
    private String priority; // Declare priority as a string variable
    private String student_information; // Declare student_information as a string variable

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NEW_STUDENTS + " TEXT,"
                    + COLUMN_PRIORITY + " TEXT"
                    + COLUMN_EDIT + " TEXT)";

    public Student() {
    }

    public Student(int id, String student, String priority, String student_information) {
        this.id = id;
        this.student = student;
        this.priority = priority;
        this.student_information = student_information;
    }

    // getId method gets student ID
    public int getId() {
    // Return id
        return id;
    }

    // setStudent method sets the student
    public void setStudent(String student) {
    // Refers to this student
        this.student = student;
    }

    // getStudent method gets student
    public String getStudent() {
    // Return student
        return student;
    }

    // getPriority method gets priority
    public String getPriority() {
    // Return priority
        return priority;
    }

    // setId method sets the id
    public void setId(int id) {
    // Refers to this id
        this.id = id;
    }

    // setStudentInformation method sets student_information
    public void setStudentInformation(String student_information) {
    // Refers to this student_information
        this.student_information = student_information;
    }

    // setPriority method sets the priority
    public void setPriority(String priority) {
    // Refers to this priority
        this.priority = priority;
    }

    // getStudentInformationMethod gets student_information
    public String getStudentInformation() {
    // Refers to this student_information
        return student_information;
    }

}


