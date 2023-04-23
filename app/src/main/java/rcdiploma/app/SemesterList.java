package rcdiploma.app;

import java.util.ArrayList;

public class SemesterList {

    String semester;
    int image;
    ArrayList<SubjectList> subjectArrayList;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<SubjectList> getSubjectArrayList() {
        return subjectArrayList;
    }

    public void setSubjectArrayList(ArrayList<SubjectList> subjectArrayList) {
        this.subjectArrayList = subjectArrayList;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
