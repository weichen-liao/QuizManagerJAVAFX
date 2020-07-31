package fr.epita.quiz.gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewQuizTeacher {
    int questionIdListIndex = 0;
    int questionCount = 1;
    String content;

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
            content += "answer: "+answer;
        }
        return content;
    }

    public void display(Connection connection, ArrayList<String> questionIdList) throws SQLException {
        Stage window = new Stage();

        String questionId = questionIdList.get(questionIdListIndex);
        ArrayList<String> questionInfo = getQuestion(connection ,questionId);
        content = getContent(questionInfo, questionCount);

        Text text = new Text();
        text.setX(10);
        text.setY(50);
        text.setText(content);

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
        pane.getChildren().addAll(text, btnNext, btnPrevious);
        Scene scene = new Scene(pane, 1000, 600);

        window.setTitle("view the questions in this quiz");
        window.setScene(scene);
        window.show();

    }

}
