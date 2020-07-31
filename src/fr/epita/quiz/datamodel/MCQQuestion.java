package fr.epita.quiz.datamodel;

public class MCQQuestion extends Question{
    private String MCQChoices;
    private String MCQAnswer;

    public String getMCQChoices() {
        return MCQChoices;
    }

    public void setMCQChoices(String MCQChoices) {
        this.MCQChoices = MCQChoices;
    }

    public String getMCQAnswer() {
        return MCQAnswer;
    }

    public void setMCQAnswer(String MCQAnswer) {
        this.MCQAnswer = MCQAnswer;
    }
}