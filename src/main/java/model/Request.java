package model;

public class Request {
    public String userId;
    public int gtype;
    public int gdetail;
    public int amount;

    public Request(String userId, int gtype, int gdetail, int amount) {
        this.userId = userId;
        this.gtype = gtype;
        this.gdetail = gdetail;
        this.amount = amount;
    }

    public String gameInfo() {
        return String.format("Game played: [%s] with amount: %d", getGtype(), amount);
    }

    private String getGtype() {
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
