package fr.epita.quiz.gui;

import fr.epita.quiz.datamodel.Quiz;
import fr.epita.quiz.datamodel.tempVariable;
import fr.epita.quiz.services.GenerateQuiz;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Main extends Application {
    Stage currentWindow;
    Scene sceneHome;
    Scene sceneTeacher;
    Scene sceneLoginStudent;
    Scene sceneQuizListViewTeacher;
    Scene sceneQuizListViewStudent;
    Scene sceneViewQuizTeacher;
    Connection connection = DriverManager.getConnection("jdbc:h2:~/Desktop/IdeaJ projects/QuizManager/MCQ", "liaoweichen","123456");
    String studentName;
    String studentId;

    public Main() throws SQLException {
    }

    public static ArrayList<String> listFilesForFolder(File folder) {
        ArrayList<String> files = new ArrayList<>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String f = fileEntry.getName();
                if (f.endsWith(".object")){
                    files.add(fileEntry.getName());
                }
            }
        }
        return files;
    }

    // load Quiz object from file
    public Quiz loadQuizObject(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("quizs/"+path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Quiz quiz = (Quiz) ois.readObject();
        ois.close();
        return quiz;
    }

    // save the temp object to file
    public void setTempVariable(String selectedQuiz) throws IOException {
        tempVariable temp = new tempVariable();
        temp.setSelectedQuiz(selectedQuiz);
        // write object to file
        FileOutputStream fos = new FileOutputStream("temp.object");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(temp);
        oos.close();
    }

    // load temp object from file
    public tempVariable loadTempVariable() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("temp.object");
        ObjectInputStream ois = new ObjectInputStream(fis);
        tempVariable temp = (tempVariable) ois.readObject();
        ois.close();
        return temp;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException, SQLException {
        currentWindow = primaryStage;

        // paneLogin
        Label lbLogin = new Label("login in as");
        lbLogin.relocate(60, 10);
        Button btnLoginAsTeacher = new Button("teacher");
        btnLoginAsTeacher.relocate(60,60);
        btnLoginAsTeacher.setOnAction(e -> currentWindow.setScene(sceneQuizListViewTeacher));
        Button btnLoginAsStudent = new Button("student");
        btnLoginAsStudent.relocate(60,100);
        btnLoginAsStudent.setOnAction(e -> currentWindow.setScene(sceneLoginStudent));
        Pane paneLogin = new Pane();
        // Set the background-color of the Pane
        paneLogin.setStyle("-fx-background-color: blue, lightgray;");
        // Set the background-insets of the Pane
        paneLogin.setStyle("-fx-background-insets: 0, 4;");
        // Set the background-radius of the Pane
        paneLogin.setStyle("-fx-background-radius: 4, 2;");
        paneLogin.getChildren().addAll(lbLogin, btnLoginAsTeacher, btnLoginAsStudent);
        sceneHome = new Scene(paneLogin, 500, 500);



        // PaneQuizTeacher
        Label lbPublishedQuizs = new Label("published quizs:");
        lbPublishedQuizs.relocate(10,0);
        Button btnHomeFromLoginTeacher = new Button("return home");
        btnHomeFromLoginTeacher.setOnAction(e -> currentWindow.setScene(sceneHome));
        btnHomeFromLoginTeacher.relocate(400,450);

        // read the existing quizs in folder
        ArrayList<String> filesTeacher = listFilesForFolder(new File("quizs/"));
        ListView<String> quizListViewTeacher = new ListView<String>();
        quizListViewTeacher.relocate(10,30);
        for (int i=0; i<filesTeacher.size();i++){
            quizListViewTeacher.getItems().add(filesTeacher.get(i));
        }
        // Select the first item from the list
        quizListViewTeacher.getSelectionModel().selectFirst();
        // listenning the selected item
        ArrayList<String> selectedQuizTeacher = new ArrayList<String>();
        if (filesTeacher.size()>0){
            selectedQuizTeacher.add(filesTeacher.get(0));
        }
        quizListViewTeacher.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedQuizTeacher.add(newValue);
            }
        });
        // button for print
        Button btnPrintQuiz = new Button("print");
        btnPrintQuiz.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // load Quiz object from file
                if (selectedQuizTeacher.size()>0) {
                    try {
                        String fileName = selectedQuizTeacher.get(selectedQuizTeacher.size()-1);
                        Quiz quizPrint = loadQuizObject(fileName);
                        GenerateQuiz generateQuiz = new GenerateQuiz();
                        generateQuiz.setQuiz(quizPrint);
                        generateQuiz.setConnection(connection);
                        generateQuiz.printQuiz(quizPrint.getTitle());

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
        }});
        btnPrintQuiz.relocate(400,50);

        // button for view quiz content
        Button btnViewQuiz = new Button("view");
        btnViewQuiz.relocate(400,80);
        btnViewQuiz.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // load Quiz object from file
                if (selectedQuizTeacher.size()>0) {
                    try{
                        String fileName = selectedQuizTeacher.get(selectedQuizTeacher.size() - 1);
//                        // save to temp object
//                        setTempVariable(fileName);
//                        currentWindow.setScene(sceneViewQuizTeacher);
                        Quiz quizView = loadQuizObject(fileName);
                        ArrayList<String> questionIdList = quizView.getQuestionIdList();
                        ViewQuizTeacher viewQuiz = new ViewQuizTeacher();
                        viewQuiz.display(connection, questionIdList);


                    } catch (IOException | ClassNotFoundException | SQLException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }});

        // button for delete a quiz
        Button btnDeleteQuiz = new Button("delete");
        btnDeleteQuiz.relocate(400,110);
        btnDeleteQuiz.setOnAction(e -> {
            // delete the file
            File seletedQuizFile = new File("quizs/"+selectedQuizTeacher.get(selectedQuizTeacher.size() - 1));
            seletedQuizFile.delete();
            // clear the list
            quizListViewTeacher.getItems().clear();
            // rescan the directory
            ArrayList<String> newFilesTeacher = listFilesForFolder(new File("quizs/"));
            for (int i=0; i<newFilesTeacher.size();i++){
                quizListViewTeacher.getItems().add(newFilesTeacher.get(i));
            }
        });

        //button to assemble a quiz
        Button btnAssemble = new Button("assemble quiz");
        btnAssemble.relocate(400,140);
        btnAssemble.setOnAction(e -> {
            AssembleQuiz newQuiz = new AssembleQuiz();
            String newName = newQuiz.display(connection);
            System.out.println("new name:"+newName);
            // reflesh the viewlist
            quizListViewTeacher.getItems().clear();
            ArrayList<String> newFilesTeacher = listFilesForFolder(new File("quizs/"));
            newFilesTeacher.add(newName);
            for (int i=0; i<newFilesTeacher.size();i++){
                quizListViewTeacher.getItems().add(newFilesTeacher.get(i));
            }
        });

        Pane PaneQuizTeacher = new Pane();
        PaneQuizTeacher.setStyle("-fx-background-color: blue, lightgray;");
        PaneQuizTeacher.setStyle("-fx-background-insets: 0, 4;");
        PaneQuizTeacher.setStyle("-fx-background-radius: 4, 2;");
        PaneQuizTeacher.getChildren().addAll(lbPublishedQuizs, quizListViewTeacher, btnHomeFromLoginTeacher, btnPrintQuiz, btnViewQuiz, btnDeleteQuiz, btnAssemble);
        sceneQuizListViewTeacher = new Scene(PaneQuizTeacher, 500, 500);



        // paneLoginStudent
        Label lbStudent1 = new Label("this is sceneStudent");
        Label lbEnterName = new Label("enter your name");
        lbEnterName.relocate(100,50);
        Label lbEnterId = new Label("enter your Id");
        lbEnterId.relocate(100,150);

        TextField tfEnterName = new TextField();
        tfEnterName.relocate(100,100);
        lbEnterName.setLabelFor(tfEnterName);
        lbEnterName.setMnemonicParsing(true);

        TextField tfEnterId = new TextField();
        tfEnterId.relocate(100,200);
        lbEnterId.setLabelFor(tfEnterId);
        lbEnterId.setMnemonicParsing(true);

        Button btnHomeFromLoginStudent = new Button("return home");
        btnHomeFromLoginStudent.setOnAction(e -> currentWindow.setScene(sceneHome));
        btnHomeFromLoginStudent.relocate(400,450);
        Button btnLoginStudent = new Button("login");
        btnLoginStudent.setOnAction(e -> {
            studentName = tfEnterName.getText();
            studentId = tfEnterId.getText();
            currentWindow.setScene(sceneQuizListViewStudent);
        });
        btnLoginStudent.relocate(100,250);
        Pane paneLoginStudent = new Pane();
        paneLoginStudent.setStyle("-fx-background-color: blue, lightgray;");
        paneLoginStudent.setStyle("-fx-background-insets: 0, 4;");
        paneLoginStudent.setStyle("-fx-background-radius: 4, 2;");
        paneLoginStudent.getChildren().addAll(lbStudent1, lbEnterName, tfEnterName, lbEnterId, tfEnterId, btnHomeFromLoginStudent, btnLoginStudent);
        sceneLoginStudent = new Scene(paneLoginStudent, 500, 500);



        // listViewQuizStudent
        Label lbPublishedQuizsStudent = new Label("published quizs:");
        lbPublishedQuizsStudent.relocate(10,0);
        Button btnHomeFromViewQuizStudent = new Button("return home");
        btnHomeFromViewQuizStudent.setOnAction(e -> currentWindow.setScene(sceneHome));
        btnHomeFromViewQuizStudent.relocate(400,450);

        // read the existing quizs in folder
        ArrayList<String> filesStudent = listFilesForFolder(new File("quizs/"));
        ListView<String> quizListViewStudent = new ListView<String>();
        quizListViewStudent.relocate(10,30);
        for (int i=0; i<filesStudent.size();i++){
            quizListViewStudent.getItems().add(filesStudent.get(i));
        }
        // Select the first item from the list
        quizListViewStudent.getSelectionModel().selectFirst();
        // listenning the selected item
        ArrayList<String> selectedQuizStudent = new ArrayList<String>();
        if (filesStudent.size()>0){
            selectedQuizStudent.add(filesStudent.get(0));
        }
        quizListViewStudent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedQuizStudent.add(newValue);
            }
        });
        // button for start the selected quiz
        Button btnStartQuiz = new Button("start quiz");
        btnStartQuiz.relocate(400,80);
        btnStartQuiz.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // load Quiz object from file
                if (selectedQuizStudent.size()>0) {
                    try{
                        String fileName = selectedQuizStudent.get(selectedQuizStudent.size() - 1);
                        Quiz quizStart = loadQuizObject(fileName);
                        ArrayList<String> questionIdList = quizStart.getQuestionIdList();
                        startQuizStudent quizStartStudent = new startQuizStudent();
                        quizStartStudent.display(connection, questionIdList, studentName, studentId);


                    } catch (IOException | ClassNotFoundException | SQLException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }});

        Pane PaneViewQuizStudent = new Pane();
        PaneViewQuizStudent.setStyle("-fx-background-color: blue, lightgray;");
        PaneViewQuizStudent.setStyle("-fx-background-insets: 0, 4;");
        PaneViewQuizStudent.setStyle("-fx-background-radius: 4, 2;");
        PaneViewQuizStudent.getChildren().addAll(lbPublishedQuizsStudent, btnHomeFromViewQuizStudent, quizListViewStudent, btnStartQuiz);
        sceneQuizListViewStudent = new Scene(PaneViewQuizStudent, 500, 500);





        // main event starts with sceneHome
        currentWindow.setScene(sceneHome);
        currentWindow.setTitle("Welcome to Quiz Manager -- developed by Weichen Liao");
        currentWindow.show();


    }
}
