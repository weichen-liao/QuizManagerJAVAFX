package fr.epita.quiz.datamodel;

import java.io.Serializable;

public class tempVariable implements Serializable {
    private String selectedQuiz;



    public String getSelectedQuiz() {
        return selectedQuiz;
    }

    public void setSelectedQuiz(String selectedQuiz) {
        this.selectedQuiz = selectedQuiz;
    }
}
