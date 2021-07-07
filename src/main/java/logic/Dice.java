package logic;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dice {
    private List<String> diceHistory;
    private Random random;

    public Dice() {
        diceHistory = new ArrayList<>();
        random = new SecureRandom();
    }

    public String getDiceHistory() {
        return "[" + String.join(",", diceHistory) + "]";
    }

    public int roll() {
        int roll = random.nextInt(6) + 1;
        if (diceHistory.size() >= 10) {
            diceHistory.remove(0);
        }
        diceHistory.add(String.valueOf(roll));
        return roll;
    }
}
