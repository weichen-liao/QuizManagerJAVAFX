package fr.epita.quiz.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class TestMain extends Application {

    Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Button btnScene1 = new Button("scene1");
        btnScene1.relocate(10,10);
        btnScene1.setOnAction(e -> {
        });

        Button btnScene2 = new Button("scene2");
        btnScene2.relocate(10,50);
        btnScene2.setOnAction(e -> {

        });

        Button btnScene3 = new Button("scene3");
        btnScene3.relocate(10,100);
        btnScene3.setOnAction(e -> {

        });

        Pane paneHome = new Pane();
        paneHome.setStyle("-fx-background-color: blue, lightgray;");
        paneHome.setStyle("-fx-background-insets: 0, 4;");
        paneHome.setStyle("-fx-background-radius: 4, 2;");
        paneHome.getChildren().addAll(btnScene1, btnScene2, btnScene3);
        Scene sceneHome = new Scene(paneHome, 500, 500);

        stage.setScene(sceneHome);
        stage.setTitle("Welcome to Quiz Manager");
        stage.show();

    }
}
