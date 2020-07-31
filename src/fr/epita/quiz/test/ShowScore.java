package fr.epita.quiz.test;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShowScore {

    public void display(String score, String studentName, String studentId, String studentAnswer, String correctAnswer){
        Stage window = new Stage();
        Text text = new Text();
        text.relocate(100,100);
        String content = "student name: " + studentName + "      student id: " + studentId +
                "\n" +
                "your answer is: " + studentAnswer + "\n" +
                "correct answer is: " + correctAnswer + "\n" +
                "your score is: " + score;

        text.setX(10);
        text.setY(50);
        text.setText(content);

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: blue, lightgray;");
        pane.setStyle("-fx-background-insets: 0, 4;");
        pane.setStyle("-fx-background-radius: 4, 2;");
        pane.getChildren().addAll(text);
        Scene scene = new Scene(pane, 400, 400);

        window.setTitle("result of this quiz");
        window.setScene(scene);
        window.show();
    }
}
