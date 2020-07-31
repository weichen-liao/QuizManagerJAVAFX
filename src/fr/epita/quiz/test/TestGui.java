package fr.epita.quiz.test;;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TestGui extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        // Create the Label for the Name
        Label nameLbl = new Label("Name:");
        // Set the position of the Label
        nameLbl.relocate(10, 10);
        // Create the TextField for the Name
        TextField nameFld = new TextField();
        // Set the position of the TextField
        nameFld.relocate(50, 10);
        // Create the Label for the Password
        Label passwordLbl = new Label("Password:");
        // Set the position of the Label
        passwordLbl.relocate(200, 10);
        // Create the TextField for the Password
        TextField passwordFld = new TextField();
        // Set the position of the TextField
        passwordFld.relocate(260, 10);
        // Create the Login Button
        Button loginBtn = new Button("Login");
        // Set the position of the Button
        loginBtn.relocate(420, 10);

        // Anchor the Name Label to the Left Edge
        AnchorPane.setLeftAnchor(nameLbl, 0.0);
        // Anchor the Name Field 50px from the Left Edge
        AnchorPane.setLeftAnchor(nameFld, 50.0);
        // Anchor the Password Label 150px from the Right Edge
        AnchorPane.setRightAnchor(passwordLbl, 150.0);
        // Anchor the Password Field to the Right Edge
        AnchorPane.setRightAnchor(passwordFld, 0.0);
        // Anchor the Login Button 50px from the Bottom Edge
        AnchorPane.setBottomAnchor(loginBtn, 10.0);
        // Create the AnchorPane
        AnchorPane root = new AnchorPane();

        // Set the padding of the AnchorPane
        root.setStyle("-fx-padding: 10;");
        // Set the border-style of the AnchorPane
        root.setStyle("-fx-border-style: solid inside;");
        // Set the border-width of the AnchorPane
        root.setStyle("-fx-border-width: 2;");
        // Set the border-insets of the AnchorPane
        root.setStyle("-fx-border-insets: 5;");
        // Set the border-radius of the AnchorPane
        root.setStyle("-fx-border-radius: 5;");
        // Set the border-color of the AnchorPane
        root.setStyle("-fx-border-color: blue;");
        // Set the size of the AnchorPane
        root.setPrefSize(500, 200);
        // Add the children to the AnchorPane
        root.getChildren().addAll(nameLbl, nameFld, passwordLbl, passwordFld, loginBtn);

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the scene to the Stage
        stage.setScene(scene);
        // Set the title of the Stage
        stage.setTitle("An AnchorPane Example");
        // Display the Stage
        stage.show();
    }
}
