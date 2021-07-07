import config.Controller;
import logic.GamesMethod;
import model.Request;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommand extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;

        switch (event.getName()) {
            case "bal":
                replyBalance(event);
                break;
            case "game":
                replyGame(event);
                break;
            case "create":
                replyCreate(event);
                break;
            default: event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }

    }

    private void replyBalance(SlashCommandEvent event) {
        int balance = GamesMethod.getBalance(event.getUser().getId());
        if (balance >= 0) {
            event.reply("You have : " + balance).queue();
        } else {
            event.reply("Unable to retrieve your balance. Did you create a new acct?").queue();
        }
    }

    private void replyCreate(SlashCommandEvent event) {
        boolean success = GamesMethod.createUser(event.getUser().getId());
        if (success)
            event.reply("Account created with 10 credit").queue();
        else
            event.reply("Unable to create account").queue();
    }

    private void replyGame(SlashCommandEvent event) {
        if (!Controller.getAccepting()) {
            event.reply("not accepting games now").queue();
            return;
        }

        int balance = GamesMethod.getBalance(event.getUser().getId());

        int gameAmount = 1;
        if (event.getOption("amount") != null) {
            gameAmount = (int) event.getOption("amount").getAsLong();
        }

        if (gameAmount < 1) {
            event.reply("Invalid game amount").queue();
            return;
        }

        if (balance - gameAmount < 0) {
            event.reply("Unable to retrieve your account balance / insufficient balance. Use /bal to check").queue();
            return;
        }

        long game_type_long = event.getOption("game_type").getAsLong();
        if (game_type_long < 1 || game_type_long > 3) {
            event.reply("Invalid game type option.").queue();
            return;
        }
        int gtype = (int) game_type_long;

        long game_details_long = event.getOption("game_details").getAsLong();
        if (game_details_long < 1) {
            event.reply("Invalid game details - number less than 1.").queue();
            return;
        }

        if (gtype == 1) {
            if (game_details_long > 6) {
                event.reply("invalid game details for game type 1. Allowed [1,2,3,4,5,6]").queue();
                return;
            }
        } else {
            if (game_details_long > 2) {
                event.reply("invalid game details for game type 1. Allowed [1,2]").queue();
                return;
            }
        }

        int gdetail = (int) game_details_long;

        Request req = new Request(event.getUser().getId(), gtype, gdetail, gameAmount);
        boolean success = GamesMethod.insertRequest(req);
        if (success)
            event.reply(req.gameInfo()).queue();
        else
            event.reply("Unable to process your request.").queue();
    }
}
