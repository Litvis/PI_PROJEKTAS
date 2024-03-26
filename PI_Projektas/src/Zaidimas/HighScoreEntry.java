package Zaidimas;

public class HighScoreEntry {
    private String playerName;
    private int highScore;
    private double averageScore; // Add averageScore field
    
    public HighScoreEntry(String playerName, int highScore, double averageScore) {
        this.playerName = playerName;
        this.highScore = highScore;
        this.averageScore = averageScore; // Initialize averageScore
    }
    
    // Getter and setter methods for playerName, highScore, and averageScore
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
    
    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}