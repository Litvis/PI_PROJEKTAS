package Zaidimas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HighScoreEntryTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String playerName = "TestPlayer";
        int highScore = 100;
        double averageScore = 75.5;

        // Act
        HighScoreEntry entry = new HighScoreEntry(playerName, highScore, averageScore);

        // Assert
        assertEquals(playerName, entry.getPlayerName());
        assertEquals(highScore, entry.getHighScore());
        assertEquals(averageScore, entry.getAverageScore());
    }

    @Test
    public void testSetters() {
        // Arrange
        HighScoreEntry entry = new HighScoreEntry("TestPlayer", 100, 75.5);

        // Act
        entry.setPlayerName("NewPlayer");
        entry.setHighScore(200);
        entry.setAverageScore(85.5);

        // Assert
        assertEquals("NewPlayer", entry.getPlayerName());
        assertEquals(200, entry.getHighScore());
        assertEquals(85.5, entry.getAverageScore());
    }
}
