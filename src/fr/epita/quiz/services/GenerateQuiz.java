package fr.epita.quiz.services;

import fr.epita.quiz.datamodel.Quiz;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.io.IOException;

public class GenerateQuiz {
    private Connection connection;
    private Quiz quiz;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void printQuiz(String fileName) throws IOException, SQLException {
        if(!fileName.startsWith("expotedQuizs")){
            fileName = "expotedQuizs/"+fileName;
        }
        FileWriter writer = new FileWriter(fileName);
        String quizTitle = quiz.getTitle();
        ArrayList<String> questionIdList = quiz.getQuestionIdList();
        String writeText = "";
        writeText += quizTitle+'\n';
        for (int i=0;i<questionIdList.size();i++){
            String questionId = questionIdList.get(i);
            String questionCount = String.valueOf(i+1);
            String query = "SELECT * FROM QUESTIONS WHERE QUESTION_ID="+questionId+";";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rsSearch = statement.executeQuery();

            while (rsSearch.next()){
                String questionType = rsSearch.getString(4);
                String question = rsSearch.getString(3);
                String mcqChoices = rsSearch.getString(5);
                writeText += "No " + questionCount+"."+'\n';
                writeText += question+'\n';
                if (questionType.equals("MCQ")){
                    String[] choiceList = mcqChoices.split("; ");
                    for (int j=0;j<choiceList.length;j++){
                        writeText += choiceList[j]+'\n';
                    }
                }
                writeText += "\n" +
                        "-----------------------------------------------------------------------------------------" +
                        "\n";
            }
        }
        writer.write(writeText);
        writer.close();
    };

    //input a list of topics, return all the questionIDs that match the topic
    public static ArrayList<String> assembleQuizByTopics(Connection connection ,String[] topicList) throws SQLException {
        for(int i=0; i<topicList.length; i++){
            topicList[i] = "\'"+topicList[i]+"\'";
        }
        String topics = "(" + String.join(",",topicList) + ")";
        String query = "SELECT QUESTION_ID FROM QUESTIONS WHERE TOPIC IN " + topics + ";";
        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rsSearch = statement.executeQuery();
        ArrayList<String> questionIdList = new ArrayList<String>();
        while (rsSearch.next()){
//            System.out.println("QUESTION_ID: " + rsSearch.getString(1));
            questionIdList.add(rsSearch.getString(1));
        }
        return questionIdList;
    }



    // generate a quiz in the console and txt file based on the quiz object given
    public static void generateConsoleQuiz(Connection connection ,Quiz quiz, boolean isExportQuiz) throws SQLException, IOException {
        ArrayList<String> questionIdList = quiz.getQuestionIdList();
        String fileName = "Java quiz.txt";
        FileWriter writer = new FileWriter(fileName);
        String quizTitle = quiz.getTitle();
        String writeText = "";
        System.out.println(quizTitle);
        writeText += quizTitle+'\n';

        for (int i=0;i<questionIdList.size();i++){
            String questionId = questionIdList.get(i);
            String questionCount = String.valueOf(i+1);
            String query = "SELECT * FROM QUESTIONS WHERE QUESTION_ID="+questionId+";";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rsSearch = statement.executeQuery();

            while (rsSearch.next()){
                String questionType = rsSearch.getString(4);
                String question = rsSearch.getString(3);
                String mcqChoices = rsSearch.getString(5);
                System.out.println("No " + questionCount+".");
                writeText += "No " + questionCount+"."+'\n';
                System.out.println(question);
                writeText += question+'\n';
                if (questionType.equals("MCQ")){
                    String[] choiceList = mcqChoices.split("; ");
                    for (int j=0;j<choiceList.length;j++){
                        System.out.println(choiceList[j]);
                        writeText += choiceList[j]+'\n';
                    }
                }
                System.out.println("\n" +
                        "-----------------------------------------------------------------------------------------");
                writeText += "\n" +
                        "-----------------------------------------------------------------------------------------" +
                        "\n";

            }
        }
        if (isExportQuiz==true){
            writer.write(writeText);
        }
        writer.close();
    }

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:h2:~/Desktop/IdeaJ projects/QuizManager/MCQ", "liaoweichen","123456");
        String[] topicList = {"oop","loop"};
//        String[] topicList = {"exception", "operators"};
        ArrayList<String> questionIdList = assembleQuizByTopics(connection, topicList);
//        ArrayList<String> questionIdList = new ArrayList<String>();
//        questionIdList.add("36");
//        questionIdList.add("50");
//        questionIdList.add("51");
//        questionIdList.add("52");
        System.out.println(questionIdList);


        Quiz quiz = new Quiz();
        quiz.setTitle("Oop and loop quiz");
//        quiz.setTitle("exception and operators quiz");
        quiz.setQuestionIdList(questionIdList);
        // write object to file
        FileOutputStream fos = new FileOutputStream("quizs/Oop_and_loop_quiz.object");
//        FileOutputStream fos = new FileOutputStream("quizs/exception_and_operators_quiz.object");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(quiz);
        oos.close();

        generateConsoleQuiz(connection, quiz, false);
    }
}
