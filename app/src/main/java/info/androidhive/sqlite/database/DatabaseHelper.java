package info.androidhive.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.model.Student;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Students_db";


    public static final String TABLE_NAME = "Course Registration Waiting List";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NEW_STUDENTS = "Students";
    public static final String COLUMN_PRIORITY = "Priority";
    public static final String COLUMN_EDIT = "Student Information";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Create Table with COLUMN_ID, COLUMN_NEW_STUDENTS, COLUMN_PRIORITY, COLUMN_EDIT with input type specified.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE" + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NEW_STUDENTS + " TEXT, " +
                COLUMN_PRIORITY + "TEXT, " +
                COLUMN_EDIT + "TEXT)";

        db.execSQL(Student.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // addNewStudent students, priority, and be able to edit student_information
    void addNewStudent(String students, String priority, String student_information) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NEW_STUDENTS, students);
        values.put(COLUMN_PRIORITY, priority);
        values.put(COLUMN_EDIT, student_information);
        long res = db.insert(TABLE_NAME, null, values);

    }

    // insertStudent method
    public long insertStudent(String student) {
        // Get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `priority` will be inserted automatically.
        // No need to add them
        values.put(Student.COLUMN_NEW_STUDENTS, student);

        // Insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // Close db connection
        db.close();

        // Return newly inserted row id
        return id;
    }

    public Student getStudent(long id) {
        // Get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ID, Student.COLUMN_NEW_STUDENTS, Student.COLUMN_PRIORITY},
                Student.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // Prepare student object
        Student student = new Student(
                cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_NEW_STUDENTS)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_PRIORITY)),
                cursor.getString(cursor.getColumnIndex(Student.COLUMN_EDIT)));

        // Close the db connection
        cursor.close();

        // Return student
        return student;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COLUMN_PRIORITY + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndex(Student.COLUMN_ID)));
                student.setStudent(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NEW_STUDENTS)));
                student.setPriority(cursor.getString(cursor.getColumnIndex(Student.COLUMN_PRIORITY)));
                student.setStudentInformation(cursor.getString(cursor.getColumnIndex(Student.COLUMN_EDIT)));
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return students list
        return students;
    }

    // getStudentsCount method
    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    // updateStudent method
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_NEW_STUDENTS, student.getStudent());

        // updating row
        return db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
    }

    // deleteStudent method
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});

        // Close db connection
        db.close();
    }
}












