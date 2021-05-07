package info.androidhive.sqlite.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;

public class MainActivity<Student> extends AppCompatActivity {
    private StudentsAdapter mAdapter; // Declare mAdapter as a StudentsAdapter variable
    private List<Student> studentsList = new ArrayList<>(); // Declare studentsList as an ArrayList to put it in an array
    private CoordinatorLayout coordinatorLayout; // Declare coordinatorLayout as a CoordinatorLayout variable
    private RecyclerView recyclerView; // Declare recyclerView as a RecyclerView variable
    private TextView noStudentsView; // Declare noStudentsView as a TextView variable

    private DatabaseHelper db; // Declare db as a DatabaseHelper variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // findViewById toolbar in layout file.
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout); // findViewById coordinatorLayout in layout file
        recyclerView = findViewById(R.id.recycler_view); // findViewById recyclerView in layout file
        noStudentsView = findViewById(R.id.empty_students_view); // findViewById noStudentsView in layout file

        db = new DatabaseHelper(this);

        studentsList.addAll((Collection<? extends Student>) db.getAllStudents());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); // findViewById fab in layout file
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudentDialog(false, null, -1);
            }
        });

        mAdapter = new StudentsAdapter(this, (List<info.androidhive.sqlite.database.model.Student>) studentsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyStudents();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    // showStudentDialog method
    private void showStudentDialog(boolean b, Object o, int i) {

    }

    /**
     * Inserting new student in db
     * and refreshing the list
     */
    private void createStudent(String student) {
        // Insert student in db and getting
        // Newly inserted student id
        long id = db.insertStudent(student);

        // Get the newly inserted student from db
        Student s = (Student) db.getStudent(id);

        if (s != null) {
            // Adding new student to array list at 0 position
            studentsList.add(0, s);

            // Refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyStudents();
        }
    }

    /**
     * Updating student in db and updating
     * item in the list by its position
     */
    private void updateStudent(String student, int position) {
        Student s = studentsList.get(position);
        // updating student text
       s.setStudent(student);

        // Updating student in db
        db.updateStudent((info.androidhive.sqlite.database.model.Student) s);

        // Refreshing the list
        studentsList.set(position, s);
        mAdapter.notifyItemChanged(position);

        toggleEmptyStudents();
    }

    /**
     * Deleting student from SQLite and removing the
     * item from the list by its position
     */
    private void deleteStudent(int position) {
        // Deleting the student from db
        db.deleteStudent((info.androidhive.sqlite.database.model.Student) studentsList.get(position));

        // Removing the student from the list
        studentsList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyStudents();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNoteDialog(true, studentsList.get(position), position);
                } else {
                    deleteStudent(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a student.
     * When shouldUpdate=true, it automatically displays old student and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Student student, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.student_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputStudent = view.findViewById(R.id.student);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_student_title) : getString(R.string.lbl_edit_student_title));


        if (shouldUpdate && student != null) {
            inputStudent.setText(student.getStudent());
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputStudent.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter student!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // If text is entered, dismiss the alertDialog
                    alertDialog.dismiss();
                }

                // Check if user updating student
                if (shouldUpdate && student != null) {
                    // Update student by it's id
                    updateStudent(inputStudent.getText().toString(), position);
                } else {
                    // Create new student
                    createStudent(inputStudent.getText().toString());
                }
            }
        });
    }

    /**
     * Toggle list and empty students view
     */
    private void toggleEmptyStudents() {
        // Check if studentsList.size() > 0

        if (db.getStudentsCount() > 0) {
            noStudentsView.setVisibility(View.GONE);
        } else {
            noStudentsView.setVisibility(View.VISIBLE);
        }
    }
}