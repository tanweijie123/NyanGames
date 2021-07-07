import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import scheduler.Scheduler;

import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        try {

            Scanner sc = new Scanner(new File("token"));
            String token = sc.next();
            JDA jda = JDABuilder.create(token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_EMOJIS
            ).build();


            CommandListUpdateAction commands = jda.updateCommands();
            addSlashCommands(commands);

            jda.addEventListener(new SlashCommand());
            jda.awaitReady();

            //initialise scheduler
            System.out.println("Initialising scheduler");
            Scheduler.init(jda);
            System.out.println("Initialised scheduler");


        } catch (Exception e) {
            System.out.println("Bot Token Failed!");
            e.printStackTrace();
            return;
        }
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
