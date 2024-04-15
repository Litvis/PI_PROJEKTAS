package Zaidimas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class HighScores {
    private List<HighScoreEntry> highScoreEntries;

    public HighScores() {
        this.highScoreEntries = new LinkedList<>();
    }

    // Method to fetch high scores from the database
    public void fetchHighScores() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            // Establish connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi_projektas", "root", "");
            
            // Execute query to fetch high scores
            String sql = "SELECT Vardas, Highscore, AverageScore FROM Highscores ORDER BY Highscore DESC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            // Process the result set
            while (resultSet.next()) {
                String playerName = resultSet.getString("Vardas");
                int highScore = resultSet.getInt("Highscore");
                double averageScore = resultSet.getDouble("AverageScore");
                
                HighScoreEntry entry = new HighScoreEntry(playerName, highScore, averageScore);
                highScoreEntries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    // Method to calculate and set the average scores
    public void calculateAverageScores() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            // Establish connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi_projektas", "root", "");
            
            // Execute query to fetch average scores
            String sql = "SELECT Vardas, AVG(Rezultatas) AS AverageScore FROM rezultatai GROUP BY Vardas";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            // Process the result set
            while (resultSet.next()) {
                String playerName = resultSet.getString("Vardas");
                double averageScore = resultSet.getDouble("AverageScore");
                
                // Find the corresponding high score entry and set its average score
                for (HighScoreEntry entry : highScoreEntries) {
                    if (entry.getPlayerName().equals(playerName)) {
                        entry.setAverageScore(averageScore);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to format high scores as a string
    public String formatHighScores() {
        StringBuilder sb = new StringBuilder();
        sb.append("High Scores:\n");
        for (HighScoreEntry entry : highScoreEntries) {
            sb.append(entry.getPlayerName()).append(": ").append(entry.getHighScore()).append("\n");
        }
        return sb.toString();
    }
}
