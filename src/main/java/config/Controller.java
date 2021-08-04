package config;

import logic.Dice;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Scanner;

/** Event Date: 6 Jul 2021 - 11 Jul 2021 */

public class Controller {

    //region File-Related Controller

    private static final String TOKEN_FILE = "token";
    private static final String DB_TOKEN_FILE = "dbtoken";

    /**
     * Retrieves the token from file
     * @return Token for discord JDA
     * @throws FileNotFoundException Specified token file is not found
     */
    public static String getToken() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(TOKEN_FILE));
        String token = sc.next();
        sc.close();

        return token;
    }

    /**
     * Retrieves the token, uid, pw for database token
     * @return A list of 3 items - token, uid, pw
     * @throws FileNotFoundException Specified token file is not found
     */
    public static List<String> getDbToken() throws FileNotFoundException {

        Scanner sc = new Scanner(new File(DB_TOKEN_FILE));
        String token = sc.next();
        String uid = sc.next();
        String pw = sc.next();
        sc.close();

        return List.of(token, uid, pw);
    }

    //endregion

    //region Stats (Fixed)

    //RATES
    public static final int SOLO_RATE = 5;
    public static final int HALF_RATE = 2;
    public static final int LOST_RATE = 0;

    //LIMITS
    public static final int DICE_HISTORY_LIMIT = 10;
    public static final int LIMIT = 3000;
    public static final int SELF_LIMIT = 1000;

    //endregion

    //region Game State

    private static boolean accepting = false;
    public static ZonedDateTime acceptingSince = null;

    /**
     * Gets the status of the game - if the game is currently accepting requests.
     * @return The status of the game.
     */
    public static boolean getAccepting() {
        return accepting;
    }

    /**
     * Sets the status of the game to "accepting request" state. If this method is called outside of event period,
     *   the status will remain in "not accepting" state.
     * @return true if the state had changed to "accepting request"; false otherwise.
     */
    public static boolean setAccepting() {
        acceptingSince = ZonedDateTime.now(ZoneId.of("GMT+8"));

        if (acceptingSince.isBefore(eventDateStart) || acceptingSince.isAfter(eventDateEnd)) {
            acceptingSince = null;
            return false;
        }
        accepting = true;
        return true;
    }

    /**
     * Sets the status of the game to "not accepting request" state.
     */
    public static void setNotAccepting() {
        accepting = false;
    }

    private static Dice dice = new Dice();

    /**
     * Perform a dice roll.
     * @return A number between 1 and 6.
     */
    public static int rollDice() {
        return dice.roll();
    }

    /**
     * Gets the list of dice roll history, up to the history limit.
     * @return A String containing the dice roll history.
     */
    public static String getDiceHistory() {
        return dice.getDiceHistory();
    }

    //endregion

    //region Game / Event Period

    private static ZonedDateTime eventDateStart = ZonedDateTime.of(
            LocalDateTime.of(2021, 7, 6, 0,0,0), ZoneId.of("GMT+8"));
    private static ZonedDateTime eventDateEnd = ZonedDateTime.of(
            LocalDateTime.of(2021, 7, 11, 0,0,0), ZoneId.of("GMT+8"));

    /**
     * Allows the program to shorten / lengthen / update the end date.
     * @param end New end date for the game / event.
     */
    public static void updateEventEndDate(ZonedDateTime end) {
        eventDateEnd = end;
    }

    //endregion

}
