package Zaidimas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class HighScores {
    private List<HighScoreEntry> highScoreEntries;

    public HighScores() {
        this.highScoreEntries = new LinkedList<>();
    }


    public void fetchHighScores() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi_projektas", "root", "");
            

            String sql = "SELECT Vardas, Highscore, AverageScore FROM highscores ORDER BY Highscore DESC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            

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

            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public List<HighScoreEntry> getHighScoreEntries() {
        return highScoreEntries;
    }

    public void calculateAverageScores() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi_projektas", "root", "");
            

            String sql = "SELECT Vardas, AVG(Rezultatas) AS AverageScore FROM rezultatai GROUP BY Vardas";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            

            while (resultSet.next()) {
                String playerName = resultSet.getString("Vardas");
                double averageScore = resultSet.getDouble("AverageScore");
                

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

            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<HighScoreEntry> filterHighScores(int minScore) {
        List<HighScoreEntry> filteredEntries = new ArrayList<>();
        for (HighScoreEntry entry : highScoreEntries) {
            if (entry.getHighScore() >= minScore) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    public String formatHighScores(int minScore) {
        StringBuilder sb = new StringBuilder();
        sb.append("High Scores:\n");
        List<HighScoreEntry> filteredEntries = filterHighScores(minScore);
        for (HighScoreEntry entry : filteredEntries) {
            sb.append(entry.getPlayerName()).append(": ").append(entry.getHighScore()).append("\n");
        }
        return sb.toString();
    }
    
}
