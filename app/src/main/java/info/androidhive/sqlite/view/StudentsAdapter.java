package info.androidhive.sqlite.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.Student;


public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.MyViewHolder> {

    private Context context;
    private List<Student> studentsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView student; // Declare student as a TextView variable
        public TextView dot; // Declare dot as a TextView variable
        public TextView priority; // Declare priority as a TextView variable

        public MyViewHolder(View view) {
            super(view);
            student = view.findViewById(R.id.student); // findViewById student in layout file.
            dot = view.findViewById(R.id.dot); // findView by dot in layout file.
            priority = view.findViewById(R.id.priority); // findViewById by priority in layout file.
        }
    }

    public StudentsAdapter(Context context, List<Student> studentsList) {
        this.context = context;
        this.studentsList = studentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student student = studentsList.get(position);

        holder.student.setText(student.getStudent());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Disaplying priority from from student
        holder.priority.setText(student.getPriority());
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

};

