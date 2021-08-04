package scheduler;

import config.Controller;
import logic.GamesMethod;
import model.Request;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RollDice {
    private static final String guildId = "822405141906718751";
    private static final String channelId = "861905213374857216";

    static Runnable execute(JDA jda) {
        Runnable ex = () -> {

            if (!Controller.getAccepting()) {
                boolean accepting = Controller.setAccepting();
                if (accepting)
                    jda.getGuildById(guildId).getTextChannelById(channelId).sendMessage("```fix\n[GAME ALERT] Accepting request\n```").queue();
                return;
            } else {
                jda.getGuildById(guildId).getTextChannelById(channelId).sendMessage("```fix\n[GAME ALERT] Stopping request\n```").queue();
                Controller.setNotAccepting();
                runCalculation(jda);
            }
        };
        return ex;
    }

    private static void runCalculation(JDA jda) {
        //get all request
        List<Request> reqList = GamesMethod.getRequests(Controller.acceptingSince);

        //roll dice
        int dice = Controller.rollDice();

        //calculate
        HashMap<String, Integer> toUpdate = new HashMap<>();

        for (Request req : reqList) {
            int result = calculateReward(req, dice);
            if (toUpdate.containsKey(req.userId)) {
                toUpdate.put(req.userId, toUpdate.get(req.userId) + result);
            } else {
                toUpdate.put(req.userId, result);
            }
        }

        //distribute
        List<Pair<String, Integer>> toUpdatePair = toUpdate.entrySet().stream().map(x -> ImmutablePair.of(x.getKey(), x.getValue())).collect(Collectors.toList());
        String result = String.format("Dice rolled: %d \nDice History: %s\nWinners:\n\n %s", dice, Controller.getDiceHistory(), toUpdatePair.stream().filter(x -> x.getRight() > 0)
                .map(x -> jda.getGuildById(guildId).getMemberById(x.getLeft()).getEffectiveName() + " -> " + x.getRight())
                .collect(Collectors.joining("\n")));

        GamesMethod.insertFinalLog(toUpdatePair);

        //display top10 & total balance in game.
        int totalBalance = GamesMethod.getTotalBalance();
        String reply = result + "\n" + String.format("Total Balance in Game: %d\nMax Balance in Game: %d\n\nTop 10 Players:\n\n %s",
                totalBalance, Controller.LIMIT,
                GamesMethod.getTop10Users().stream()
                        .map(x -> jda.getGuildById(guildId).getMemberById(x.getLeft()).getEffectiveName() + " -> " + x.getRight())
                        .collect(Collectors.joining("\n"))
        );
        jda.getGuildById(guildId).getTextChannelById(channelId).sendMessage(reply).queue();

        if (totalBalance >= Controller.LIMIT) {
            Controller.updateEventEndDate(ZonedDateTime.now(ZoneId.of("GMT+8")));
            jda.getGuildById(guildId).getTextChannelById(channelId).sendMessage("Total Balance Limit has been reached. Event ended.").queue();
        }

    }

    private static int calculateReward(Request req, int dice) {
        switch (req.gtype) {
            case 1: return runSolo(req, dice);
            case 2: return runOddEven(req, dice);
            case 3: return runLowHigh(req, dice);
        }
        return 0;
    }

    private static int runSolo(Request req, int dice) {
        return req.gdetail == dice ? Controller.SOLO_RATE * req.amount : 0;
    }

    private static int runOddEven(Request req, int dice) {
        return (req.gdetail % 2 == dice % 2) ? Controller.HALF_RATE * req.amount : 0;
    }

    private static int runLowHigh(Request req, int dice) {
        if (req.gdetail == 2 && dice >= 4) {
            return Controller.HALF_RATE * req.amount;
        } else if (req.gdetail == 1 && dice <= 3) {
            return Controller.HALF_RATE * req.amount;
        } else {
            return 0;
        }
    }

}
