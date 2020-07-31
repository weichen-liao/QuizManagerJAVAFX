package fr.epita.quiz.gui;

import fr.epita.quiz.test.ShowScore;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class startQuizStudent {
    int questionIdListIndex = 0;
    int questionCount = 1;
    String content;
    ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<String> studentAnswerList = new ArrayList<String>();
    ArrayList<String> correctAnswerList = new ArrayList<String>();


    // get the detail of this question based on the questionId
    public ArrayList<String> getQuestion(Connection connection, String questionId) throws SQLException {
        String query = "SELECT * FROM QUESTIONS WHERE QUESTION_ID="+questionId+";";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rsSearch = statement.executeQuery();
        ArrayList<String> questionInfo = new ArrayList<>();

        while (rsSearch.next()) {
            String questionType = rsSearch.getString(4);
            String question = rsSearch.getString(3);
            String mcqChoices = rsSearch.getString(5);
            String answer = rsSearch.getString(6);
            questionInfo.add(questionId);
            questionInfo.add(question);
            questionInfo.add(questionType);
            questionInfo.add(mcqChoices);
            questionInfo.add(answer);
            break;
        }
        return questionInfo;
    }

    public static String getContent(ArrayList<String> questionInfo, int questionCount){
        String question = questionInfo.get(1);
        String questionType = questionInfo.get(2);
        String mcqChoices = questionInfo.get(3);
        String answer = questionInfo.get(4);

        String content = "No. "+questionCount+"\n";
        content += question;
        if (questionType.equals("MCQ")){
            content += '\n';
            String[] choiceList = mcqChoices.split("; ");
            for (int j=0;j<choiceList.length;j++){
                content += choiceList[j]+'\n';
            }
        }
        return content;
    }

    // compare the result and calculate the score. the maximun score is always 100
    public static String calScore(ArrayList<String> studentAnswerList, ArrayList<String> correctAnswerList, ArrayList<String> typeList){
        float numberOfQuestions = 0;
        float correctCount = 0;
        System.out.println("typeList:"+typeList);
        for (int i=0;i<typeList.size();i++){
            if (typeList.get(i).equals("MCQ")){
                numberOfQuestions += 1;
                if (studentAnswerList.get(i).equals(correctAnswerList.get(i))){
                    correctCount += 1;
                }
            }
        }
        String score = String.valueOf(Math.ceil((correctCount/numberOfQuestions)*100));
        return score;
    }

    public void display(Connection connection, ArrayList<String> questionIdList, String studentName, String studentId) throws SQLException {
        Stage window = new Stage();

        String questionId = questionIdList.get(questionIdListIndex);
        ArrayList<String> questionInfo = getQuestion(connection ,questionId);
        content = getContent(questionInfo, questionCount);

        for (int i1=0;i1<questionIdList.size();i1++){
            String quesId = questionIdList.get(i1);
            ArrayList<String> quesInfo = getQuestion(connection ,quesId);
            typeList.add(quesInfo.get(2));
            studentAnswerList.add("");
            correctAnswerList.add(quesInfo.get(4));
        }

        Text text = new Text();
        text.setX(10);
        text.setY(50);
        text.setText(content);

        Label lbEnterAnswer = new Label("enter answer here:");
        lbEnterAnswer.relocate(250,470);
        TextField tfEnterAnswer = new TextField();
        tfEnterAnswer.relocate(400,470);
        tfEnterAnswer.setPrefColumnCount(30);

        Button btnConfirm = new Button("confirm");
        btnConfirm.relocate(800, 470);
        btnConfirm.setOnAction(e -> {
            String answerFromStudent = tfEnterAnswer.getText();
            studentAnswerList.set(questionIdListIndex,answerFromStudent);
//            System.out.println("studentAnswerList" + studentAnswerList);
//            System.out.println("correctAnswerList" + correctAnswerList);
        });

        Button btnSubmit = new Button("submit");
        btnSubmit.relocate(600,500);
        btnSubmit.setOnAction(e -> {
            String score = calScore(studentAnswerList, correctAnswerList, typeList);
            System.out.println("score: "+ score);
            ShowScore quizResult = new ShowScore();
            String studentAnswers = String.join(",",studentAnswerList);
            String correctAnswers = String.join(",",correctAnswerList);
            quizResult.display(score, studentName, studentId, studentAnswers, correctAnswers);
            window.close();

        });

        Button btnNext = new Button("next");
        btnNext.relocate(500,500);
        btnNext.setOnAction(e -> {
            questionCount = Math.min(questionCount+1,questionIdList.size());
            questionIdListIndex = Math.min(questionIdListIndex+1, questionIdList.size()-1);
            String currQuestionId = questionIdList.get(questionIdListIndex);
            ArrayList<String> currQuestionInfo = null;
            try {
                currQuestionInfo = getQuestion(connection ,currQuestionId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            content = getContent(currQuestionInfo, questionCount);
            text.setText(content);
        });

        Button btnPrevious = new Button("previous");
        btnPrevious.relocate(400,500);
        btnPrevious.setOnAction(e -> {
            questionCount = Math.max(questionCount-1,1);
            questionIdListIndex = Math.max(questionIdListIndex-1, 0);
            String currQuestionId = questionIdList.get(questionIdListIndex);
            ArrayList<String> currQuestionInfo = null;
            try {
                currQuestionInfo = getQuestion(connection ,currQuestionId);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            content = getContent(currQuestionInfo, questionCount);
            text.setText(content);
        });

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: blue, lightgray;");
        pane.setStyle("-fx-background-insets: 0, 4;");
        pane.setStyle("-fx-background-radius: 4, 2;");
        pane.getChildren().addAll(text, btnNext, btnPrevious, tfEnterAnswer, lbEnterAnswer, btnConfirm, btnSubmit);
        Scene scene = new Scene(pane, 1000, 600);

        window.setTitle("view the questions in this quiz");
        window.setScene(scene);
        window.show();

    }

}

