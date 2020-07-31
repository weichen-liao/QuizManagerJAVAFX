package fr.epita.quiz.datamodel;

import java.io.Serializable;
import java.util.ArrayList;

public class Quiz implements Serializable {
    private String title;
    private ArrayList<String> questionIdList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getQuestionIdList() {
        return questionIdList;
    }

    public void setQuestionIdList(ArrayList<String> questionIdList) {
        this.questionIdList = questionIdList;
    }
}
