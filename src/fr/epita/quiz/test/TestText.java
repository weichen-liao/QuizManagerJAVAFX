package fr.epita.quiz.test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestText extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //Creating a Text object
        Text text = new Text();

        //Setting the text to be added.
        text.setText("No 2.\n" +
                "What will happen when you compile and run the following code with assertion enabled?public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\t\n" +
                "\t\tint age = 20;\n" +
                "\t\tassert age > 20 : getMessage();\n" +
                "\t\tSystem.out.println(\"Valid\");\n" +
                "\t\t\n" +
                "\t}\n" +
                "\n" +
                "\tprivate static void getMessage() {\n" +
                "\t\tSystem.out.println(\"Not valid\");\n" +
                "\t}\n" +
                "\t\n" +
                "}\n" +
                "A. The code will not compile\n" +
                "B. The code will compile but will throw AssertionError when executed\n" +
                "C. The code will compile and print Not Valid\n" +
                "D. The code will compile and print Valid");

        //setting the position of the text
        text.setX(50);
        text.setY(50);

        //Creating a Group object
        Group root = new Group(text);

        //Creating a scene object
        Scene scene = new Scene(root, 600, 300);

        //Setting title to the Stage
        stage.setTitle("Sample Application");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

}
