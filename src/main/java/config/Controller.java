package config;

import logic.Dice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Event Date: 6 Jul 2021 - 11 Jul 2021 */

public class Controller {
    public static ZonedDateTime eventDateStart = ZonedDateTime.of(
            LocalDateTime.of(2021, 7, 6, 0,0,0), ZoneId.of("GMT+8"));
    public static ZonedDateTime eventDateEnd = ZonedDateTime.of(
            LocalDateTime.of(2021, 7, 11, 0,0,0), ZoneId.of("GMT+8"));


    private static boolean accepting = false;
    public static ZonedDateTime acceptingSince = null;

    //DICE
    public static Dice dice = new Dice();

    //RATES
    public static final int SOLO_RATE = 5;
    public static final int HALF_RATE = 2;

    //LIMITS
    public static final int LIMIT = 3000;
    public static final int SELF_LIMIT = 1000;

    public static boolean getAccepting() {
        return accepting;
    }

    public static boolean setAccepting() {
        acceptingSince = ZonedDateTime.now(ZoneId.of("GMT+8"));

        if (acceptingSince.isBefore(eventDateStart) || acceptingSince.isAfter(eventDateEnd)) {
            acceptingSince = null;
            return false;
        }
        accepting = true;
        return true;
    }

    public static void setNotAccepting() {
        accepting = false;
    }

}
