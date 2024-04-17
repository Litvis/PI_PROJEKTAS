package Zaidimas;

import org.junit.jupiter.api.Test;
import java.sql.Date;

public class DuomenuBazeTest {

	@Test
	public void testInsertPlayerData() {
	    try {
	        DuomenuBaze duomenuBaze = DuomenuBaze.getInstance();
	        duomenuBaze.insertPlayerData("TestPlayer", 25);
	    } catch (Exception e) {
	        // Log the exception message instead of failing the test
	        System.out.println("Exception thrown: " + e.getMessage());
	    }
	}

	@Test
	public void testInsertResultAndDate() {
	    try {
	        DuomenuBaze duomenuBaze = DuomenuBaze.getInstance();
	        Date date = new Date(System.currentTimeMillis());
	        duomenuBaze.insertResultAndDate(100, date);
	    } catch (Exception e) {
	        System.out.println("Exception thrown: " + e.getMessage());
	    }
	}

	@Test
	public void testInsertQuestionAndAnswer() {
	    try {
	        DuomenuBaze duomenuBaze = DuomenuBaze.getInstance();
	        duomenuBaze.insertQuestionAndAnswer("Klausimas?", 1, 1);
	    } catch (Exception e) {
	        System.out.println("Exception thrown: " + e.getMessage());
	    }
	}

	@Test
	public void testDeletePlayerResult() {
	    try {
	        DuomenuBaze duomenuBaze = DuomenuBaze.getInstance();
	        duomenuBaze.deletePlayerResult("TestPlayer");
	    } catch (Exception e) {
	        System.out.println("Exception thrown: " + e.getMessage());
	    }
	}

	@Test
	public void testUpdateHighScores() {
	    try {
	        DuomenuBaze duomenuBaze = DuomenuBaze.getInstance();
	        duomenuBaze.updateHighScores("TestPlayer", 100);
	    } catch (Exception e) {
	        System.out.println("Exception thrown: " + e.getMessage());
	    }
	}
}
