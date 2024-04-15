package Zaidimas;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathGameGUITest {

    @Test
    public void testCalculatePoints() {
        MathGameGUI mathGameGUI = new MathGameGUI();
        long startTime = System.currentTimeMillis() - 5000; // 5 sekundės praeityje
        int points = mathGameGUI.calculatePoints(startTime);
        assertEquals(500, points); // Tikimasi 500 taškų už 5 sekundžių trukmę
    }

    @Test
    public void testPlayerNameAndAge() {
        MathGameGUI mathGameGUI = new MathGameGUI();
        mathGameGUI.playerName = "Jonas";
        mathGameGUI.playerAge = 25;
        assertEquals("Jonas", mathGameGUI.getPlayerName()); // Patikrinti, ar teisingai gaunamas žaidėjo vardas
        assertEquals(25, mathGameGUI.getPlayerAge()); // Patikrinti, ar teisingai gaunamas žaidėjo amžius
    }

    // Pridėti daugiau testų kitiems metams, kuriuos norite testuoti
}
