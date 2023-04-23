package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SemesterExpandActivity extends AppCompatActivity {

    ExpandableListView expandableListView;

    String[] semesterArray = {"1", "2", "3", "4", "5", "6"};
    int[] semesterImageArray = {R.drawable.flutter_logo,R.drawable.clock_24,R.drawable.calendar_icon,R.drawable.java,R.drawable.android_image,R.drawable.flutter_logo,R.drawable.clock_24};
    ArrayList<SemesterList> arrayList;

    String[] subject1Array = {"Maths", "CPU"};
    int[] subjectImage1Array ={R.drawable.android_image,R.drawable.flutter_logo};

    String[] subject2Array = {"C++", "Physics", "Basic Electronics"};
    int[] subjectImage2Array = {R.drawable.java,R.drawable.clock_24,R.drawable.calendar_icon,R.drawable.android_image};

    String[] subject3Array = {"OOP"};
    int[] subjectImage3Array = {R.drawable.flutter_logo};

    String[] subject4Array = {"Core Java", "FSD", "ICT", "DBMS"};
    int[] subjectImage4Array = {R.drawable.android_image,R.drawable.flutter_logo,R.drawable.java,R.drawable.calendar_icon};
    ArrayList<SubjectList> subjectArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_expand);
        expandableListView = findViewById(R.id.semester_expandable_listview);
        arrayList = new ArrayList<>();
        for (int i = 0; i < semesterArray.length; i++) {
            SemesterList list = new SemesterList();
            list.setSemester(semesterArray[i]);
            list.setImage(semesterImageArray[i]);
            if (i == 0) {
                subjectArrayList = new ArrayList<>();
                for (int j = 0; j < subject1Array.length; j++) {
                    SubjectList sList = new SubjectList();
                    sList.setSubjectName(subject1Array[j]);
                    sList.setSubjectImage(subjectImage1Array[j]);
                    subjectArrayList.add(sList);
                }
                list.setSubjectArrayList(subjectArrayList);
            }
            if (i == 1) {
                subjectArrayList = new ArrayList<>();
                for (int j = 0; j < subject2Array.length; j++) {
                    SubjectList sList = new SubjectList();
                    sList.setSubjectName(subject2Array[j]);
                    sList.setSubjectImage(subjectImage2Array[j]);
                    subjectArrayList.add(sList);
                }
                list.setSubjectArrayList(subjectArrayList);
            }
            if (i == 2) {
                subjectArrayList = new ArrayList<>();
                for (int j = 0; j < subject3Array.length; j++) {
                    SubjectList sList = new SubjectList();
                    sList.setSubjectName(subject3Array[j]);
                    sList.setSubjectImage(subjectImage3Array[j]);
                    subjectArrayList.add(sList);
                }
                list.setSubjectArrayList(subjectArrayList);
            }
            if (i == 3) {
                subjectArrayList = new ArrayList<>();
                for (int j = 0; j < subject4Array.length; j++) {
                    SubjectList sList = new SubjectList();
                    sList.setSubjectName(subject4Array[j]);
                    sList.setSubjectImage(subjectImage4Array[j]);
                    subjectArrayList.add(sList);
                }
                list.setSubjectArrayList(subjectArrayList);
            }
            if (i == 4) {
                subjectArrayList = new ArrayList<>();
                SubjectList sList = new SubjectList();
                sList.setSubjectName("No Data Available");
                sList.setSubjectImage(R.drawable.ic_man);
                subjectArrayList.add(sList);
                /*for(int j=0;j<subject1Array.length;j++){
                    SubjectList sList = new SubjectList();
                    sList.setSubjectName(subject1Array[j]);
                    subjectArrayList.add(sList);
                }*/
                list.setSubjectArrayList(subjectArrayList);
            }
            if (i == 5) {
                subjectArrayList = new ArrayList<>();
                SubjectList sList = new SubjectList();
                sList.setSubjectName("No Data Available");
                sList.setSubjectImage(R.drawable.ic_man);
                subjectArrayList.add(sList);
                /*for(int j=0;j<subject1Array.length;j++){
                    SubjectList sList = new SubjectList();
                    sList.setSubjectName(subject1Array[j]);
                    subjectArrayList.add(sList);
                }*/
                list.setSubjectArrayList(subjectArrayList);
            }
            arrayList.add(list);
        }
        SemesterAdapter adapter = new SemesterAdapter(SemesterExpandActivity.this, arrayList);
        expandableListView.setAdapter(adapter);
    }

    private class SemesterAdapter extends BaseExpandableListAdapter {

        Context context;
        ArrayList<SemesterList> arrayList;
        LayoutInflater inflater;

        public SemesterAdapter(SemesterExpandActivity semesterExpandActivity, ArrayList<SemesterList> arrayList) {
            this.context = semesterExpandActivity;
            this.arrayList = arrayList;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount() {
            return arrayList.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return arrayList.get(i).getSubjectArrayList().size();
        }

        @Override
        public Object getGroup(int i) {
            return arrayList.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return arrayList.get(i).getSubjectArrayList().get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.custom_semester,null);
            TextView name = view.findViewById(R.id.custom_semester_name);
            ImageView iv = view.findViewById(R.id.custom_semester_image);
            name.setText(arrayList.get(i).getSemester());
            iv.setImageResource(arrayList.get(i).getImage());
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.custom_subject,null);
            TextView subjectName = view.findViewById(R.id.custom_subject_name);
            ImageView subjectImage = view.findViewById(R.id.custom_subject_image);
            subjectImage.setImageResource(arrayList.get(i).getSubjectArrayList().get(i1).getSubjectImage());
            subjectName.setText(arrayList.get(i).getSubjectArrayList().get(i1).getSubjectName());
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}