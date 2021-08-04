package model;

import config.Controller;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Dice object which returns a random number between 1 and 6, and keeps track up to the last 10 dice rolls.
 */
public class Dice {
    private FixedList<Integer> diceHistory;
    private Random random;

    public Dice() {
        diceHistory = new FixedList<>(Controller.DICE_HISTORY_LIMIT);
        random = new SecureRandom();
    }

    /**
     * Gets the list of dice roll history, up to the history limit.
     * @return A String of dice roll history.
     */
    public String getDiceHistory() {
        return diceHistory.toString();
    }

    /**
     * Perform a dice roll.
     * @return Returns a number between 1 and 6.
     */
    public int roll() {
        int roll = random.nextInt(6) + 1;
        diceHistory.add(roll);
        return roll;
    }

    class FixedList<E> extends LinkedList<E> {
        private int limit;

        public FixedList(int lim) {
            this.limit = lim;
        }

        @Override
        public boolean add(E object) {
            super.add(object);
            while (super.size() > limit) {
                super.remove();
            }
            return true;
        }

        @Override
        public String toString() {
            return "[" + Stream.of(toArray()).map(x -> x.toString()).collect(Collectors.joining(",")) + "]";
        }
    }
}
