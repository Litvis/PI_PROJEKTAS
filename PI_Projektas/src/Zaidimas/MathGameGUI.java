package Zaidimas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.Random;

public class MathGameGUI extends Application {
    private static final Random random = new Random();
    private static final int GAME_DURATION = 60; // in seconds (1 minute)
    private int num1, num2, correctAnswer;
    private long startTime;
    private Scene scene; // Declare scene as a class-level variable
    private Label problemLabel;
    private Label pointsLabel; // Label to display points earned
    private Label wrongAnswerLabel; // Label to display "Neteisingas atsakymas"
    private int totalPoints = 0; // Total points earned
    private boolean gameActive = true; // Flag to track game status

    private String playerName;
    private int playerAge;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Matematinis žaidimas");

        Label label = new Label("Pagrindinis meniu");
        label.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        TextField nameField = new TextField();
        nameField.setPromptText("Įveskite savo vardą");
        nameField.setStyle("-fx-font-size: 16px; -fx-font-family: 'Times New Roman';");

        TextField ageField = new TextField();
        ageField.setPromptText("Įveskite savo amžių");
        ageField.setStyle("-fx-font-size: 16px; -fx-font-family: 'Times New Roman';");

        Button playButton = new Button("Žaisti žaidimą");
        playButton.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        playButton.setOnAction(event -> {
            playerName = nameField.getText();
            playerAge = Integer.parseInt(ageField.getText());
            // Insert player data into the database when the game starts
            insertPlayerData(playerName, playerAge);
            showGameScreen(primaryStage);
        });

        Button highscoresButton = new Button("Patikrinti rezultatus");
        highscoresButton.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        highscoresButton.setOnAction(event -> {
            HighScores highScores = new HighScores();
            highScores.fetchHighScores();
            displayHighScores(highScores.formatHighScores(), primaryStage);
        });

        Button exitButton = new Button("Išeiti iš žaidimo");
        exitButton.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        exitButton.setOnAction(event -> {
            Platform.exit();
        });

        VBox vbox = new VBox(10, label, nameField, ageField, playButton, highscoresButton, exitButton);
        vbox.setAlignment(Pos.CENTER);
        Scene mainScene = new Scene(vbox, 600, 400);
        primaryStage.setScene(mainScene);

        primaryStage.show();
    }

    private void insertPlayerData(String playerName, int playerAge) {
        DuomenuBaze duomenuBaze = new DuomenuBaze();
        // Insert player data into the database
        duomenuBaze.insertPlayerData(playerName, playerAge);
    }

    private void updateHighScores(String playerName, int totalPoints) {
        DuomenuBaze duomenuBaze = new DuomenuBaze();
        duomenuBaze.updateHighScores(playerName, totalPoints);
    }

    private void showGameScreen(Stage primaryStage) {

        problemLabel = new Label();
        problemLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        generateNewQuestion(); // Generate the first question

        pointsLabel = new Label();
        pointsLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");

        wrongAnswerLabel = new Label();
        wrongAnswerLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");

        Label countdownLabel = new Label(Integer.toString(GAME_DURATION));
        countdownLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Times New Roman';");

        TextField answerField = new TextField();
        answerField.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");

        answerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                answerField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        Button answerButton = new Button("Pateikti atsakymą");
        answerButton.setStyle("-fx-font-size: 20px; -fx-font-family: 'Times New Roman';");

        answerButton.setOnAction(event -> {
            if (gameActive) { // Check if the game is still active
                String userAnswerText = answerField.getText();
                if (!userAnswerText.isEmpty()) {
                    int userAnswer = Integer.parseInt(userAnswerText);
                    // Save the question, correct answer, and user's input answer
                    DuomenuBaze duomenuBaze = new DuomenuBaze();
                    String question = "Kiek yra " + num1 + " + " + num2 + "?";
                    duomenuBaze.insertQuestionAndAnswer(question, correctAnswer, userAnswer);

                    if (userAnswer == correctAnswer) {
                        int points = calculatePoints(startTime);
                        totalPoints += points;
                        problemLabel.setText("Teisingai! Tu uzsidirbai " + points + " taškų.");
                        pointsLabel.setText("Iš viso taškų: " + totalPoints);
                        wrongAnswerLabel.setText(""); // Clear wrong answer message
                    } else {
                        wrongAnswerLabel.setText("Neteisingas atsakymas.");
                    }
                    generateNewQuestion(); // Generate a new question regardless of the answer
                    answerField.clear(); // Clear the answer field
                }
            }
        });
        VBox vbox = new VBox(10, problemLabel, pointsLabel, answerField, wrongAnswerLabel, countdownLabel, answerButton);
        vbox.setAlignment(Pos.CENTER);
        scene = new Scene(vbox, 600, 400); // Update scene
        primaryStage.setScene(scene); // Set the updated scene

        // Start game countdown
        startTime = System.currentTimeMillis();
        new Thread(() -> {
            try {
                for (int i = GAME_DURATION; i > 0; i--) {
                    int finalI = i;
                    Platform.runLater(() -> countdownLabel.setText(Integer.toString(finalI)));
                    Thread.sleep(1000); // 1-second delay
                }
                Platform.runLater(() -> {
                    problemLabel.setText("Laikas baigėsi!");
                    answerField.setDisable(true); // Disable answer field after time's up
                    answerButton.setDisable(true); // Disable answer button after time's up

                    // Insert result and date into the database
                    DuomenuBaze duomenuBaze = new DuomenuBaze();
                    updateHighScores(playerName, totalPoints);
                    duomenuBaze.insertResultAndDate(totalPoints, new Date(System.currentTimeMillis()));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void generateNewQuestion() {
        num1 = random.nextInt(100);
        num2 = random.nextInt(100);
        correctAnswer = num1 + num2;
        problemLabel.setText("Kiek yra " + num1 + " + " + num2 + "?"); // Update text of existing label
    }

    private int calculatePoints(long startTime) {
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime) / 1000; // in seconds
        // Example: 1 second = 100 points
        return (int) (100 * GAME_DURATION / timeTaken);
    }

    private void displayHighScores(String highScoresText, Stage primaryStage) {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        
        Label highScoresLabel = new Label(highScoresText);
        highScoresLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Arcade'; -fx-text-fill: black;");
        
        vbox.getChildren().add(highScoresLabel);
        
        Scene highScoresScene = new Scene(vbox, 400, 300);
        primaryStage.setScene(highScoresScene);
    }

    // Method to get player name
    public String getPlayerName() {
        return playerName;
    }

    // Method to get player age
    public int getPlayerAge() {
        return playerAge;
    }

    // Method to get total points earned by the player
    public int getTotalPoints() {
        return totalPoints;
    }
}