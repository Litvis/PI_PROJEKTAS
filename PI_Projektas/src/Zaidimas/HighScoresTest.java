package Zaidimas;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class HighScoresTest {

    @Test
    public void testFetchHighScores() {
        HighScores highScores = new HighScores();
        highScores.fetchHighScores();

        // Verify that the high score entries were fetched
        List<HighScoreEntry> highScoreEntries = highScores.getHighScoreEntries();
        assertNotNull(highScoreEntries);
        assertFalse(highScoreEntries.isEmpty());
    }


    @Test
    public void testFilterHighScores() {
        HighScores highScores = new HighScores();
        List<HighScoreEntry> filteredEntries = highScores.filterHighScores(50);

        // Perform assertions to verify the correctness of the filtered high score entries
        // For example, assert the size of the filteredEntries list or specific entries
    }

    @Test
    public void testFormatHighScores() {
        HighScores highScores = new HighScores();
        String formattedScores = highScores.formatHighScores(0);

        // Perform assertions to verify the correctness of the formatted high scores
        // For example, assert that the formattedScores string contains expected content
    }
}
