package Zaidimas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date; // Import Date class

public class DuomenuBaze {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pi_projektas";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Method to insert player data into the database
    public void insertPlayerData(String playerName, int playerAge) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO zaidejai (Vardas, Amzius) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, playerName);
                statement.setInt(2, playerAge);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Player data inserted successfully!");
                } else {
                    System.out.println("Failed to insert player data!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert result and date into the database
    public void insertResultAndDate(int result, Date date) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO rezultatai (Rezultatas, Data) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, result);
                statement.setDate(2, date);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Result and date inserted successfully!");
                } else {
                    System.out.println("Failed to insert result and date!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void insertQuestionAndAnswer(String question, int correctAnswer, int userAnswer) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO klausimai (Klausimas, Teisingas_ATS, zaidejo_ats) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, question);
                statement.setInt(2, correctAnswer);
                statement.setInt(3, userAnswer);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Question and answers inserted successfully!");
                } else {
                    System.out.println("Failed to insert question and answers!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateHighScores(String playerName, int totalPoints) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Update high score
            String updateHighScoreSQL = "INSERT INTO HighScores (Vardas, Highscore) "
                                      + "VALUES (?, ?) "
                                      + "ON DUPLICATE KEY UPDATE HighScore = GREATEST(Highscore, ?)";
            try (PreparedStatement updateHighScoreStatement = connection.prepareStatement(updateHighScoreSQL)) {
                updateHighScoreStatement.setString(1, playerName);
                updateHighScoreStatement.setInt(2, totalPoints);
                updateHighScoreStatement.setInt(3, totalPoints);
                updateHighScoreStatement.executeUpdate();
            }

            // Update average score
            String updateAverageScoreSQL = "UPDATE Highscores SET AverageScore = "
                                         + "(SELECT AVG(Rezultatas) FROM rezultatai WHERE Vardas = ?) "
                                         + "WHERE Vardas = ?";
            try (PreparedStatement updateAverageScoreStatement = connection.prepareStatement(updateAverageScoreSQL)) {
                updateAverageScoreStatement.setString(1, playerName);
                updateAverageScoreStatement.setString(2, playerName);
                updateAverageScoreStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}