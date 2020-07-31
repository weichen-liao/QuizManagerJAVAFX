package fr.epita.quiz.services;


import fr.epita.quiz.datamodel.MCQQuestion;
import fr.epita.quiz.datamodel.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;


public class QuestionDAO {
    private static void showAllSqlResult(ResultSet rs) throws SQLException {
        while (rs.next()){
            System.out.println("QUESTION_ID: " + rs.getString(1));
            System.out.println("TOPIC: " + rs.getString(2));
            System.out.println("QUESTION: " + rs.getString(3));
            System.out.println("QUESTION_TYPE: " + rs.getString(4));
            System.out.println("MCQ_CHOICES: " + rs.getString(5));
            System.out.println("MCQ_ANSWER: " + rs.getString(6));
            System.out.println("DIFFICULTY: " + rs.getString(7));
            System.out.println("\n");
        }
    }

    // create an ordinary question
    private static void create(Connection connection, Question ordQuestion) throws SQLException {
        String topic = ordQuestion.getTopics();
        String question = ordQuestion.getQuestion();
        String question_type = null;
        String difficulty = String.valueOf(ordQuestion.getDifficulty());
        Formatter query = new Formatter();
        query.format("INSERT INTO QUESTIONS(TOPIC, QUESTION, QUESTION_TYPE, DIFFICULTY)\n" +
                "VALUES (\'%s\', \'%s\', \'%s\', %s);",topic, question, question_type, difficulty);
//        System.out.println(String.valueOf(query));
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
        statement.execute();
    }

    // create a MCQ question
    private static void createMCQ(Connection connection, MCQQuestion mcqQuestion) throws SQLException {
        String topic = mcqQuestion.getTopics();
        String question = mcqQuestion.getQuestion();
        String question_type = "MCQ";
        String mcqChoices = mcqQuestion.getMCQChoices();
        String mcqAnswer = mcqQuestion.getMCQAnswer();
        String difficulty = String.valueOf(mcqQuestion.getDifficulty());
        Formatter query = new Formatter();
        query.format("INSERT INTO QUESTIONS(TOPIC, QUESTION, QUESTION_TYPE, MCQ_CHOICES, MCQ_ANSWER, DIFFICULTY)\n" +
                "VALUES (\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', %s);",topic, question, question_type, mcqChoices, mcqAnswer, difficulty);
//        System.out.println(String.valueOf(query));
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
        statement.execute();
    }

    // update an ordinary question
    private static void update(Connection connection, String question_id, Question ordQuestion) throws SQLException {
        String topic = ordQuestion.getTopics();
        String question = ordQuestion.getQuestion();
        String difficulty = String.valueOf(ordQuestion.getDifficulty());
        Formatter query = new Formatter();
        query.format("UPDATE QUESTIONS SET\n" +
                "   TOPIC=\'%s\', QUESTION=\'%s\', QUESTION_TYPE=NULL, MCQ_CHOICES=NULL, MCQ_ANSWER=NULL, DIFFICULTY=%s\n" +
                "WHERE QUESTION_ID=%s;",topic, question, difficulty, question_id);
//        System.out.println(String.valueOf(query));
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
        statement.execute();
    }

    private static void updateMCQ(Connection connection, String question_id, MCQQuestion mcqQuestion) throws SQLException {
        String topic = mcqQuestion.getTopics();
        String question = mcqQuestion.getQuestion();
        String mcqChoices = mcqQuestion.getMCQChoices();
        String mcqAnswer = mcqQuestion.getMCQAnswer();
        String difficulty = String.valueOf(mcqQuestion.getDifficulty());
        Formatter query = new Formatter();
        query.format("UPDATE QUESTIONS SET\n" +
                "   TOPIC=\'%s\', QUESTION=\'%s\', QUESTION_TYPE=\'MCQ\', MCQ_CHOICES=\'%s\', MCQ_ANSWER=\'%s\', DIFFICULTY=%s\n" +
                "WHERE QUESTION_ID=%s;",topic, question, mcqChoices, mcqAnswer, difficulty, question_id);
//        System.out.println(String.valueOf(query));
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
        statement.execute();
    }

    // delete a question by the question_id. if you don't know the question_id, search it first
    private static void delete(Connection connection, String question_id) throws SQLException {
        Formatter query = new Formatter();
        query.format("DELETE FROM QUESTIONS WHERE QUESTION_ID=%s",question_id);
//        System.out.println(String.valueOf(query));
        PreparedStatement statement = connection.prepareStatement(String.valueOf(query));
        statement.execute();
    }

    // clear every row in this table
    private static void deleteAll(Connection connection) throws SQLException {
        String query = "DELETE FROM QUESTIONS;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
    }

    // search the question by topic, question content, question type or difficulty. None, one or multiple conditions are allowed
    // if the input conditions are all empty, then search every result in the db
    // if there are one or more conditions, combine them into one condition and add it to the query
    private static void search(Connection connection, String topic, String question, String question_type, String difficulty) throws SQLException {
        ArrayList<String> conditionList = new ArrayList<String>();
        if (topic!=""){
            conditionList.add("TOPIC="+"\'"+topic+"\'");
        }
        if (question!=""){
            conditionList.add("QUESTION="+"\'"+question+"\'");
        }
        if (question_type!=""){
            conditionList.add("QUESTION_TYPE="+"\'"+question_type+"\'");
        }
        if (difficulty!=""){
            conditionList.add("DIFFICULTY="+difficulty);
        }

        String whereConditions = ";";
        if (conditionList.size()!=0) {
            whereConditions = "WHERE " + String.join(" AND ",conditionList) + whereConditions;
        }
        String query = "SELECT * FROM QUESTIONS " + whereConditions;
//        System.out.println(query);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rsSearch = statement.executeQuery();
        showAllSqlResult(rsSearch);
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:~/Desktop/IdeaJ projects/QuizManager/MCQ", "liaoweichen","123456");

        MCQQuestion mcqQuestion1 = new MCQQuestion();
        mcqQuestion1.setTopics("operators");
        mcqQuestion1.setQuestion("What will happen when you compile and run the following code?\n" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tint i = 0;\n" +
                "\t\tint j = i++ + ++i;\n" +
                "\t\tSystem.out.println( j );\n" +
                "\t}\n" +
                "}");
        mcqQuestion1.setMCQChoices("A. 1; B. 2; C. 3; D. 4");
        mcqQuestion1.setMCQAnswer("B");
        mcqQuestion1.setDifficulty(2);

        MCQQuestion mcqQuestion2 = new MCQQuestion();
        mcqQuestion2.setTopics("operators");
        mcqQuestion2.setQuestion("What will happen when you compile and run the following code?\n" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tint i = 0;\n" +
                "\t\tint j = ++i + i++;\n" +
                "\t\tSystem.out.println( j );\n" +
                "\t}\n" +
                "}");
        mcqQuestion2.setMCQChoices("A. 1; B. 2; C. 3; D. 4");
        mcqQuestion2.setMCQAnswer("A");
        mcqQuestion2.setDifficulty(2);

        MCQQuestion mcqQuestion3 = new MCQQuestion();
        mcqQuestion3.setTopics("operators");
        mcqQuestion3.setQuestion("What will happen when you compile and run the following code?\n" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tboolean b1 = true, b2 = false, b3 = true;\n" +
                "\t\tSystem.out.println( b1&&b2&&!b3 );\n" +
                "\t}\n" +
                "}");
        mcqQuestion3.setMCQChoices("A. true; B.false; C. Compilation error");
        mcqQuestion3.setMCQAnswer("B");
        mcqQuestion3.setDifficulty(4);

        MCQQuestion mcqQuestion4 = new MCQQuestion();
        mcqQuestion4.setTopics("operators");
        mcqQuestion4.setQuestion("What will happen when you compile and run the following code?\n" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args) {\n" +
                "\t\tint i = -21;\n" +
                "\t\tint j = 4;\t\t\n" +
                "\t\tSystem.out.println( i%j );\n" +
                "\t}\n" +
                "}");
        mcqQuestion4.setMCQChoices("A. 1; B. -1; C. 5; D. -5");
        mcqQuestion4.setMCQAnswer("C");
        mcqQuestion4.setDifficulty(3);

        MCQQuestion mcqQuestion5 = new MCQQuestion();
        mcqQuestion5.setTopics("oop");
        mcqQuestion5.setQuestion("What will happen when you compile and run the following code?\n" +
                "class One{\n" +
                "\tprivate void className(){\n" +
                "\t\tSystem.out.println(\"Parent\");\n" +
                "\t}\n" +
                "}\n" +
                "class Two extends One{\n" +
                "\tpublic void className(){\n" +
                "\t\tSystem.out.println(\"Child\");\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\t\n" +
                "\t\tOne one = new Two();\n" +
                "\t\tone.className();\n" +
                "\t}\t\t\n" +
                "}");
        mcqQuestion5.setMCQChoices("A. Parent; B. Child; C. Compilation error; D. Runtime error");
        mcqQuestion5.setMCQAnswer("C");
        mcqQuestion5.setDifficulty(5);

        MCQQuestion mcqQuestion6 = new MCQQuestion();
        mcqQuestion6.setTopics("oop");
        mcqQuestion6.setQuestion("A private method defined in the parent class cannot be overridden by child class.");
        mcqQuestion6.setMCQChoices("A. true; B. false");
        mcqQuestion6.setMCQAnswer("A");
        mcqQuestion6.setDifficulty(6);

        MCQQuestion mcqQuestion7 = new MCQQuestion();
        mcqQuestion7.setTopics("oop");
        mcqQuestion7.setQuestion("Does the class Two override the someMethod() correctly?" +
                "class One{\n" +
                "\tpublic void someMethod(int i){\t\t\n" +
                "\t}\n" +
                "}\n" +
                "class Two extends One{\n" +
                "\tpublic void someMethod(short s){\t\t\n" +
                "\t}\n" +
                "}");
        mcqQuestion7.setMCQChoices("A. Yes; B. No");
        mcqQuestion7.setMCQAnswer("B");
        mcqQuestion7.setDifficulty(5);

        MCQQuestion mcqQuestion8 = new MCQQuestion();
        mcqQuestion8.setTopics("assertion");
        mcqQuestion8.setQuestion("What will happen when you compile and run the following code with assertion enabled?" +
                "public class Test{\n" +
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
                "}");
        mcqQuestion8.setMCQChoices("A. The code will not compile; B. The code will compile but will throw AssertionError when executed; C. The code will compile and print Not Valid; D. The code will compile and print Valid");
        mcqQuestion8.setMCQAnswer("A");
        mcqQuestion8.setDifficulty(9);

        MCQQuestion mcqQuestion9 = new MCQQuestion();
        mcqQuestion9.setTopics("assertion");
        mcqQuestion9.setQuestion("What will happen when you compile and run the following code with assertion enabled?" +
                "public class Test{\t\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\t\t\n" +
                "\t\tassert false;\n" +
                "\t\tSystem.out.println(\"True\");\n" +
                "\t\t\n" +
                "\t}\t\n" +
                "}");
        mcqQuestion9.setMCQChoices("A. The code will not compile due to unreachable code since false is hard coded in assert statement; B. The code will compile but will throw AssertionError when executed; C. The code will compile and print true; D. None of the above");
        mcqQuestion9.setMCQAnswer("B");
        mcqQuestion9.setDifficulty(10);

        MCQQuestion mcqQuestion10 = new MCQQuestion();
        mcqQuestion10.setTopics("assertion");
        mcqQuestion10.setQuestion("Consider below given code. Will it compile without any errors?" +
                "public class Test{\t\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\t\t\t\t\n" +
                "\t\tboolean b = false;\n" +
                "\t\tassert b = true;\n" +
                "\t}\n" +
                "}");
        mcqQuestion10.setMCQChoices("A. Yes; B. No");
        mcqQuestion10.setMCQAnswer("A");
        mcqQuestion10.setDifficulty(8);

        MCQQuestion mcqQuestion11 = new MCQQuestion();
        mcqQuestion11.setTopics("assertion");
        mcqQuestion11.setQuestion("What is wrong with the below given code?" +
                "import java.util.ArrayList;\n" +
                "\n" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\t\n" +
                "\t\t ArrayList listNames = new ArrayList();\n" +
                "\t\t addNames(listNames);\n" +
                "\t\t\n" +
                "\t\t System.out.println( removeJohn(listNames) );\n" +
                "\t}\n" +
                "\n" +
                "\tprivate static boolean removeJohn(ArrayList listNames) {\n" +
                "\t\tassert listNames.remove(\"John\") : \"Not Removed\";\n" +
                "\t\treturn true;\n" +
                "\t}\n" +
                "\n" +
                "\tprivate static void addNames(ArrayList listNames) {\n" +
                "\t\tlistNames.add(\"John\");\n" +
                "\t\tlistNames.add(\"Mike\");\n" +
                "\t\tlistNames.add(\"Charlie\");\t\t\n" +
                "\t}\n" +
                "\t\n" +
                "}");
        mcqQuestion11.setMCQChoices("A. Code will not compile; B. There is nothing wrong with the code; C. Assertion is not used properly; D. None of the above");
        mcqQuestion11.setMCQAnswer("C");
        mcqQuestion11.setDifficulty(10);

        MCQQuestion mcqQuestion12 = new MCQQuestion();
        mcqQuestion12.setTopics("exception");
        mcqQuestion12.setQuestion("Will this class compile?" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\tTest t = new Test();\n" +
                "\t\tt.method1();\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic void method1(){\n" +
                "\t\tmethod2();\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic void method2(){\n" +
                "\t\ttry{\n" +
                "\t\t\tthrow new NullPointerException();\n" +
                "\t\t}catch(NullPointerException e){\n" +
                "\t\t\tthrow e;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}");
        mcqQuestion12.setMCQChoices("A. Yes; B. No");
        mcqQuestion12.setMCQAnswer("A");
        mcqQuestion12.setDifficulty(1);

        MCQQuestion mcqQuestion13 = new MCQQuestion();
        mcqQuestion13.setTopics("exception");
        mcqQuestion13.setQuestion("What will happen when you compile and run the following code?" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\tTest t = new Test();\n" +
                "\t\tt.method1();\n" +
                "\t\tSystem.out.print(\"5\");\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic void method1(){\n" +
                "\t\ttry{\n" +
                "\t\t\t\n" +
                "\t\t\tmethod2();\n" +
                "\t\t\tSystem.out.print(\"1\");\n" +
                "\t\t\t\n" +
                "\t\t}catch(NullPointerException ne){\n" +
                "\t\t\tSystem.out.println(\"2\");\n" +
                "\t\t}finally{\n" +
                "\t\t\tSystem.out.println(\"3\");\n" +
                "\t\t}\n" +
                "\t\tSystem.out.print(\"4\");\t\t\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic void method2(){\n" +
                "\t\tthrow new ArrayIndexOutOfBoundsException();\n" +
                "\t}\n" +
                "}");
        mcqQuestion13.setMCQChoices("A. 245; B. 15; C. 12345; D. 23; E. None of the above");
        mcqQuestion13.setMCQAnswer("E");
        mcqQuestion13.setDifficulty(10);

        MCQQuestion mcqQuestion14 = new MCQQuestion();
        mcqQuestion14.setTopics("exception");
        mcqQuestion14.setQuestion("Error (and its subclasses) cannot be caught, only Exception (and its subclasses) can be caught using the catch block.");
        mcqQuestion14.setMCQChoices("A. true; B. false");
        mcqQuestion14.setMCQAnswer("B");
        mcqQuestion14.setDifficulty(5);

        MCQQuestion mcqQuestion15 = new MCQQuestion();
        mcqQuestion15.setTopics("exception");
        mcqQuestion15.setQuestion("Will this code compile?" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\ttry{\n" +
                "\t\t\tTest t = new Test();\n" +
                "\t\t\tt.method1();\n" +
                "\t\t}catch(NullPointerException ne){\n" +
                "\t\t\t\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\t\n" +
                "\tpublic void method1() throws Exception{\t\t\t\n" +
                "\t}\n" +
                "\t\n" +
                "}");
        mcqQuestion15.setMCQChoices("A. Yes; B. No");
        mcqQuestion15.setMCQAnswer("B");
        mcqQuestion15.setDifficulty(7);

        MCQQuestion mcqQuestion16 = new MCQQuestion();
        mcqQuestion16.setTopics("exception");
        mcqQuestion16.setQuestion("What will happen when you compile and run the following code?" +
                "public class Test{\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\n" +
                "\t\ttry{\n" +
                "\t\t\t\n" +
                "\t\t\tSystem.out.print(\"1\");\n" +
                "\n" +
                "\t\t\ttry{\n" +
                "\t\t\t\tSystem.out.print(\"a\");\n" +
                "\t\t\t\tthrow new Exception();\n" +
                "\t\t\t}catch(Exception e){\n" +
                "\t\t\t\tSystem.out.print(\"b\");\n" +
                "\t\t\t\tthrow e;\n" +
                "\t\t\t}finally{\n" +
                "\t\t\t\tSystem.out.print(\"c\");\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t}catch(Exception e){\n" +
                "\t\t\tSystem.out.print(\"3\");\n" +
                "\t\t}finally{\n" +
                "\t\t\tSystem.out.print(\"4\");\n" +
                "\t\t}\n" +
                "\t}\t\n" +
                "}");
        mcqQuestion16.setMCQChoices("A. 1ac34; B. 1abc34; C. 1ab3c4; D. 1abc4");
        mcqQuestion16.setMCQAnswer("B");
        mcqQuestion16.setDifficulty(4);

        MCQQuestion mcqQuestion17 = new MCQQuestion();
        mcqQuestion17.setTopics("loop");
        mcqQuestion17.setQuestion("What will happen when you compile and run the following code?" +
                "public class Test{\t\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\t\n" +
                "\t\tint a = 0;\n" +
                "\t\twhile(a < 10){\n" +
                "\t\t\tSystem.out.println(a++);\n" +
                "\t\t}\n" +
                "\t}\t\n" +
                "}");
        mcqQuestion17.setMCQChoices("A. Compilation error; B. Code will print 1 to 9; C. Code will print 0 to 9; D. Code will print 1 to 10");
        mcqQuestion17.setMCQAnswer("C");
        mcqQuestion17.setDifficulty(2);

        MCQQuestion mcqQuestion18 = new MCQQuestion();
        mcqQuestion18.setTopics("loop");
        mcqQuestion18.setQuestion("What will happen when you compile and run the following code?" +
                "public class Test{\t\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\t\n" +
                "\t\tint a = 0, b = 10;\n" +
                "\t\twhile( a + 2 < b){\n" +
                "\t\t\tSystem.out.println(a);\n" +
                "\t\t}\n" +
                "\t}\t\n" +
                "}");
        mcqQuestion18.setMCQChoices("A. Compilation error; B. Code will print 2 to 8; C. Code will print 0 to 8; D. None of the above");
        mcqQuestion18.setMCQAnswer("D");
        mcqQuestion18.setDifficulty(8);

        MCQQuestion mcqQuestion19 = new MCQQuestion();
        mcqQuestion19.setTopics("loop");
        mcqQuestion19.setQuestion("What will happen when you compile and run the following code?" +
                "public class Test{\t\n" +
                "\t\n" +
                "\tpublic static void main(String[] args){\n" +
                "\t\tint a = 4, b = 4;\n" +
                "\t\touter: while(a-- > 0){\n" +
                "\t\t\twhile(b-- > 0){\n" +
                "\t\t\t\tif(a == b)\n" +
                "\t\t\t\t\tcontinue outer;\n" +
                "\t\t\t\tSystem.out.print(a + \" \" + b + \", \");\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\t\n" +
                "}");
        mcqQuestion19.setMCQChoices("A. 3 2, 3 1, 2 3, 2 1, 1 3, 1 2,; B. 3 2, 2 3, 1 3, 1 2,; C. 3 3, 2 2, 1 1,; D. None of the above");
        mcqQuestion19.setMCQAnswer("D");
        mcqQuestion19.setDifficulty(10);

        MCQQuestion mcqQuestion20 = new MCQQuestion();
        mcqQuestion20.setTopics("loop");
        mcqQuestion20.setQuestion("Regardless of the boolean condition of the do while loop, there is always at least one iteration of the loop.");
        mcqQuestion20.setMCQChoices("A. true; B. false");
        mcqQuestion20.setMCQAnswer("A");
        mcqQuestion20.setDifficulty(5);

//        MCQQuestion mcqQuestion = new MCQQuestion();
//        mcqQuestion.setTopics("");
//        mcqQuestion.setQuestion("");
//        mcqQuestion.setMCQChoices("A. ; B. ; C. ; D. ");
//        mcqQuestion.setMCQAnswer("");
//        mcqQuestion.setDifficulty();



        Question ordQuestion1 = new Question();
        ordQuestion1.setTopics("concept");
        ordQuestion1.setQuestion("Explain JDK, JRE and JVM?");
        ordQuestion1.setDifficulty(3);

        Question ordQuestion2 = new Question();
        ordQuestion2.setTopics("concept");
        ordQuestion2.setQuestion("Explain public static void main(String args[]) in Java");
        ordQuestion2.setDifficulty(1);

        Question ordQuestion3 = new Question();
        ordQuestion3.setTopics("concept");
        ordQuestion3.setQuestion("Why Java is platform independent?");
        ordQuestion3.setDifficulty(5);

        Question ordQuestion4 = new Question();
        ordQuestion4.setTopics("oop");
        ordQuestion4.setQuestion("Why Java is not 100% Object-oriented?");
        ordQuestion4.setDifficulty(7);

        Question ordQuestion5 = new Question();
        ordQuestion5.setTopics("concept");
        ordQuestion5.setQuestion("What are wrapper classes in Java?");
        ordQuestion5.setDifficulty(9);

        Question ordQuestion6 = new Question();
        ordQuestion6.setTopics("concept");
        ordQuestion6.setQuestion("What are constructors in Java?");
        ordQuestion6.setDifficulty(3);

        Question ordQuestion7 = new Question();
        ordQuestion7.setTopics("concept");
        ordQuestion7.setQuestion("What is singleton class in Java and how can we make a class singleton?");
        ordQuestion7.setDifficulty(8);

        Question ordQuestion8 = new Question();
        ordQuestion8.setTopics("concept");
        ordQuestion8.setQuestion("What is the difference between Array list and vector in Java?");
        ordQuestion8.setDifficulty(4);



//        deleteAll(connection);
//
//        create(connection, ordQuestion1);
//        create(connection, ordQuestion2);
//        create(connection, ordQuestion3);
//        create(connection, ordQuestion4);
//        create(connection, ordQuestion5);
//        create(connection, ordQuestion6);
//        create(connection, ordQuestion7);
//        create(connection, ordQuestion8);
//        createMCQ(connection, mcqQuestion1);
//        createMCQ(connection, mcqQuestion2);
//        createMCQ(connection, mcqQuestion3);
//        createMCQ(connection, mcqQuestion4);
//        createMCQ(connection, mcqQuestion5);
//        createMCQ(connection, mcqQuestion6);
//        createMCQ(connection, mcqQuestion7);
//        createMCQ(connection, mcqQuestion8);
//        createMCQ(connection, mcqQuestion9);
//        createMCQ(connection, mcqQuestion10);
//        createMCQ(connection, mcqQuestion11);
//        createMCQ(connection, mcqQuestion12);
//        createMCQ(connection, mcqQuestion13);
//        createMCQ(connection, mcqQuestion14);
//        createMCQ(connection, mcqQuestion15);
//        createMCQ(connection, mcqQuestion16);
//        createMCQ(connection, mcqQuestion17);
//        createMCQ(connection, mcqQuestion18);
//        createMCQ(connection, mcqQuestion19);
//        createMCQ(connection, mcqQuestion20);

//        delete(connection,"8");
//        deleteAll(connection);
//        update(connection, "9", ordQuestion2);
//        updateMCQ(connection, "7", mcqQuestion2);
        search(connection, "","","","");





    }

}

