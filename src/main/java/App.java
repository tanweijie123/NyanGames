import config.Controller;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import scheduler.Scheduler;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;

public class App {
    public static void main(String[] args) {

        //JDA init
        JDA jda = null;

        try {
            String token = Controller.getToken();
            jda = JDABuilder.create(token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_EMOJIS
            ).build();

        } catch (FileNotFoundException fnf) {
            System.out.println("Token file not found! Terminating program.");
            fnf.printStackTrace();
            return;
        } catch (IllegalArgumentException iae) {
            System.out.println("JDA Gateway Intent not specified or unallowed! Terminating program.");
            iae.printStackTrace();
            return;
        } catch (LoginException le) {
            System.out.println("The provided token is invalid! Terminating program.");
            le.printStackTrace();
            return;
        }

        //command init
        CommandListUpdateAction commands = jda.updateCommands();
        addSlashCommands(commands);
        jda.addEventListener(new SlashCommand());

        //wait for bot to be ready
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //initialise scheduler
        System.out.println("Initialising scheduler");
        Scheduler.init(jda);
        System.out.println("Initialised scheduler");

    }

    private static void addSlashCommands(CommandListUpdateAction commands) {
        commands.addCommands(
                new CommandData("create", "create an account with NyanGames (only useable once)"),
                new CommandData("game", "play a new game")
                    .addOptions(new OptionData(OptionType.INTEGER, "game_type", "plays game type", true),
                                new OptionData(OptionType.INTEGER, "game_details", "read instructions", true),
                                new OptionData(OptionType.INTEGER, "amount", "amount of stake (default=1)")),
                new CommandData("bal", "get my balance")
        );

        commands.queue(); //required to update all added slash commands here.
    }
}
