package fr.epita.quiz.gui;

import fr.epita.quiz.datamodel.Quiz;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import org.h2.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AssembleQuiz {
    String newName = "loop_quiz";

    public String selectTopics(Connection connection) throws SQLException {
        String res = "Topic        Count    AverageDifficulty"+"\n";
        String query = "SELECT DISTINCT TOPIC, COUNT(*), AVG(DIFFICULTY) FROM QUESTIONS GROUP BY TOPIC;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rsSearch = statement.executeQuery();
        while (rsSearch.next()) {
            String topic = rsSearch.getString(1);
            String count = rsSearch.getString(2);
            String avgDiff = rsSearch.getString(3);
            res += (topic + " ".repeat(16-topic.length()) + count + " ".repeat(25-(topic.length()+count.length()+8)) + avgDiff + "\n" );
        }
        return res;
    }

    //input a list of topics, return all the questionIDs that match the topic
    public static ArrayList<String> assembleQuizByTopics(Connection connection , String[] topicList) throws SQLException {
        for(int i=0; i<topicList.length; i++){
            topicList[i] = "\'"+topicList[i]+"\'";
        }
        String topics = "(" + String.join(",",topicList) + ")";
        String query = "SELECT QUESTION_ID FROM QUESTIONS WHERE TOPIC IN " + topics + ";";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rsSearch = statement.executeQuery();
        ArrayList<String> questionIdList = new ArrayList<String>();
        while (rsSearch.next()){
//            System.out.println("QUESTION_ID: " + rsSearch.getString(1));
            questionIdList.add(rsSearch.getString(1));
        }
        return questionIdList;
    }

    // generate and save quiz object
    public static void generateQuiz(ArrayList<String> questionIdList, String name) throws IOException {
        Quiz quiz = new Quiz();
        quiz.setTitle(name);
        quiz.setQuestionIdList(questionIdList);
        // write object to file
        String objectName = "quizs/"+name+".object";
        FileOutputStream fos = new FileOutputStream(objectName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(quiz);
        oos.close();
    }

    public String display(Connection connection){
        Stage window = new Stage();

        Text text = new Text();
        text.setX(10);
        text.setY(60);

        Button btnInventory = new Button("over-all check");
        btnInventory.relocate(10,10);
        btnInventory.setOnAction(e -> {
            try {
                String checkRes = selectTopics(connection);
                text.setText(checkRes);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        Label lbEnterQuizName = new Label("enter quiz name");
        lbEnterQuizName.relocate(300,10);
        TextField tfEnterQuizName = new TextField();
        tfEnterQuizName.setPrefColumnCount(20);
        tfEnterQuizName.relocate(300,40);
        Label lbEnterTopics = new Label("enter topics(seperate by ';')");
        lbEnterTopics.relocate(300,70);
        TextField tfEnterTopics = new TextField();
        tfEnterTopics.setPrefColumnCount(20);
        tfEnterTopics.relocate(300,100);

        Button btnAssembleByTopics = new Button("assemble by topics");
        btnAssembleByTopics.relocate(700,100);
        btnAssembleByTopics.setOnAction(e -> {
            String inputTopic = tfEnterTopics.getText();
            String[] topics = inputTopic.split(";");
            if (topics.length > 0){
                try {
                    ArrayList<String> questionIdList = assembleQuizByTopics(connection, topics);
                    newName = tfEnterQuizName.getText();
                    generateQuiz(questionIdList, newName);

                } catch (SQLException | IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: blue, lightgray;");
        pane.setStyle("-fx-background-insets: 0, 4;");
        pane.setStyle("-fx-background-radius: 4, 2;");
        pane.getChildren().addAll(btnInventory, text, tfEnterQuizName, lbEnterQuizName, lbEnterTopics, tfEnterTopics, btnAssembleByTopics);
        Scene scene = new Scene(pane, 1000, 600);

        window.setTitle("assemble a quiz");
        window.setScene(scene);
        window.show();
        return newName+".object";
    }
}
