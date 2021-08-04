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
