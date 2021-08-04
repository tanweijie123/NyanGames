package model;

/**
 * An immutable game request for the game to process.
 */
public class Request {
    /** The discord user id for the user. */
    private final String userId;
    /** The game type the player requested. */
    private final int gtype;
    /** The option used for the game type. */
    private final int gdetail;
    /** The amount of duplicates for this request. */
    private final int amount;

    /**
     * Creates a new game request for the game to process.
     */
    public Request(String userId, int gtype, int gdetail, int amount) {
        this.userId = userId;
        this.gtype = gtype;
        this.gdetail = gdetail;
        this.amount = amount;
    }

    //region Get-Set Methods

    public String getUserId() {
        return userId;
    }
    public int getGtype() {
        return gtype;
    }
    public int getGdetail() {
        return gdetail;
    }
    public int getAmount() {
        return amount;
    }

    //endregion

    /**
     * Returns the game request's details.
     * @return Returns a String with this game request's details.
     */
    public String gameInfo() {
        return String.format("Game played: [%s] with amount: %d", getGtypeDetails(), amount);
    }

    private String getGtypeDetails() {
        if (gtype == 1) {
            return "Solo for digit " + gdetail;
        }

        if (gtype == 2) {
            return "Odd-Even for " + ((gdetail % 2 == 0) ? "even" : "odd") ;
        }

        if (gtype == 3) {
            return "Low-High for " + ((gdetail == 1) ? "low" : "high") ;
        }

        return "";
    }

}
