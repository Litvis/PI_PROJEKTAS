package Zaidimas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.Random;

public class MathGameGUI extends Application implements GameObserver {
    private static final Random random = new Random();
    private static final int GAME_DURATION = 60;
    int num1;
	int num2;
	int correctAnswer;
    private long startTime;
    private Scene scene;
    private Label problemLabel;
    private Label pointsLabel;
    private Label wrongAnswerLabel;
    private int totalPoints = 0;
    private boolean gameActive = true;
    String playerName;
    int playerAge;
    private Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Matematinis žaidimas");


        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();


        VBox vbox = createMainMenuLayout(primaryStage);
        mainScene = new Scene(vbox, screenWidth, screenHeight);


        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    private VBox createMainMenuLayout(Stage primaryStage) {
        Label label = new Label("Matematinis žaidimas");
        label.setStyle("-fx-font-size: 64px; -fx-font-family: 'Arcade';");

        TextField nameField = new TextField();
        nameField.setPromptText("Vardas");
        nameField.setStyle("-fx-font-size: 16px; -fx-font-family: 'Times New Roman';");
        nameField.setMaxWidth(300);


        nameField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z]*")) {
                return change;
            }
            return null;
        }));

        TextField ageField = new TextField();
        ageField.setPromptText("Amžius");
        ageField.setStyle("-fx-font-size: 16px; -fx-font-family: 'Times New Roman';");
        ageField.setMaxWidth(300);


        ageField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        }));

        Button playButton = new Button("Žaisti žaidimą");
        playButton.setMaxWidth(300);
        playButton.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        playButton.setOnAction(event -> {
            playerName = nameField.getText();
            playerAge = Integer.parseInt(ageField.getText());

            insertPlayerData(playerName, playerAge);
            showGameScreen(primaryStage, primaryStage.getWidth(), primaryStage.getHeight());
        });
        Button highscoresButton = new Button("Patikrinti rezultatus");
        highscoresButton.setMaxWidth(300);
        highscoresButton.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        highscoresButton.setOnAction(event -> {

            HighScores highScores = new HighScores();
            highScores.fetchHighScores();
            

            VBox highScoresLayout = new VBox();
            highScoresLayout.setAlignment(Pos.CENTER);
            highScoresLayout.setSpacing(20);
            
            Label titleLabel = new Label();
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Arcade';");
            
            int minScore = 1000;
            highScoresLayout.getChildren().add(titleLabel);
            highScoresLayout.getChildren().add(new Label(highScores.formatHighScores(minScore)));
            

            Button backButton = new Button("Grįžti į pagrindinį meniu");
            backButton.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arcade';");
            backButton.setOnAction(backEvent -> showMainMenu(primaryStage));
            highScoresLayout.getChildren().add(backButton);
            

            highScoresLayout.setStyle("-fx-font-size: 24px; -fx-font-family: 'Arcade';");
            

            Scene highScoresScene = new Scene(highScoresLayout, primaryStage.getWidth(), primaryStage.getHeight());
            primaryStage.setScene(highScoresScene);
        });
        
        Button exitButton = new Button("Išeiti iš žaidimo");
        exitButton.setMaxWidth(300);
        exitButton.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        exitButton.setOnAction(event -> {
            Platform.exit();
        });

        VBox vbox = new VBox(10, label, nameField, ageField, playButton, highscoresButton, exitButton);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private void insertPlayerData(String playerName, int playerAge) {
        DuomenuBaze duomenuBaze = new DuomenuBaze();

        duomenuBaze.insertPlayerData(playerName, playerAge);
    }

    private void updateHighScores(String playerName, int totalPoints) {
        DuomenuBaze duomenuBaze = new DuomenuBaze();

        duomenuBaze.deletePlayerResult(playerName);

        duomenuBaze.updateHighScores(playerName, totalPoints);
    }
    
    private void showGameScreen(Stage primaryStage, double screenWidth, double screenHeight) {

        problemLabel = new Label();
        problemLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        generateNewQuestion();

        pointsLabel = new Label();
        pointsLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");

        wrongAnswerLabel = new Label();
        wrongAnswerLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");

        Label countdownLabel = new Label(Integer.toString(GAME_DURATION));
        countdownLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        TextField answerField = new TextField();
        answerField.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");
        answerField.setMaxWidth(300);
        
        answerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                answerField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        Button answerButton = new Button("Pateikti atsakymą");
        answerButton.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");
        answerButton.setMaxWidth(300);

        answerButton.setOnAction(event -> {
            if (gameActive) { 
                String userAnswerText = answerField.getText();
                if (!userAnswerText.isEmpty()) {
                    int userAnswer = Integer.parseInt(userAnswerText);

                    DuomenuBaze duomenuBaze = new DuomenuBaze();
                    String question = "Kiek yra " + num1 + " + " + num2 + "?";
                    duomenuBaze.insertQuestionAndAnswer(question, correctAnswer, userAnswer);

                    if (userAnswer == correctAnswer) {
                        int points = calculatePoints(startTime);
                        totalPoints += points;
                        problemLabel.setText("Teisingai! Tu uzsidirbai " + points + " taškų.");
                        pointsLabel.setText("Iš viso taškų: " + totalPoints);
                        wrongAnswerLabel.setText("");
                    } else {
                        wrongAnswerLabel.setText("Neteisingas atsakymas.");
                    }
                    generateNewQuestion();
                    answerField.clear();
                }
            }
        });

        Button backButton = new Button("Grįžti į pagrindinį meniu");
        backButton.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");
        backButton.setMaxWidth(300);
        backButton.setOnAction(event -> {
            showMainMenu(primaryStage);
        });

        VBox vbox = new VBox(10, problemLabel, pointsLabel, answerField, wrongAnswerLabel, countdownLabel, answerButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        scene = new Scene(vbox, screenWidth, screenHeight);
        primaryStage.setScene(scene);


        startTime = System.currentTimeMillis();
        new Thread(() -> {
            try {
                for (int i = GAME_DURATION; i > 0; i--) {
                    int finalI = i;
                    Platform.runLater(() -> countdownLabel.setText(Integer.toString(finalI)));
                    Thread.sleep(1000);
                }
                Platform.runLater(() -> {
                    problemLabel.setText("Laikas baigėsi!");
                    answerField.setDisable(true);
                    answerButton.setDisable(true);


                    DuomenuBaze duomenuBaze = new DuomenuBaze();
                    updateHighScores(playerName, totalPoints);
                    duomenuBaze.insertResultAndDate(totalPoints, new Date(System.currentTimeMillis()));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void generateNewQuestion() {
        num1 = random.nextInt(100);
        num2 = random.nextInt(100);
        correctAnswer = num1 + num2;
        problemLabel.setText("Kiek yra " + num1 + " + " + num2 + "?");
    }

    int calculatePoints(long startTime) {
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime) / 1000;

        return (int) (100 * GAME_DURATION / timeTaken);
    }
    private void showMainMenu(Stage primaryStage) {
        primaryStage.setScene(mainScene);
    }
    private void displayHighScores(String highScoresText, Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        
        
        
        Label highScoresLabel = new Label(highScoresText);
        highScoresLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Arcade'; -fx-text-fill: black;");
        
        Button backButton = new Button("Grįžti į pagrindinį meniu");
        backButton.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");
        backButton.setOnAction(event -> {
            showMainMenu(primaryStage);
        });
        
        vbox.getChildren().addAll(highScoresLabel, backButton);
        
        Scene highScoresScene = new Scene(vbox, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(highScoresScene);
        primaryStage.show();
    }


    public String getPlayerName() {
        return playerName;
    }


    public int getPlayerAge() {
        return playerAge;
    }


    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public void onNewQuestion(String questionText) {
        problemLabel.setText(questionText);
    }
}