package scheduler;

import net.dv8tion.jda.api.JDA;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void init(JDA jda) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
        minuteTrigger(scheduler, jda);
    }

    private static void minuteTrigger(ScheduledExecutorService scheduler, JDA jda) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
        ZonedDateTime alarm0 = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute(), 0, 0, ZoneId.of("GMT+8"));
        if (alarm0.isBefore(now)) {
            alarm0 = alarm0.plusMinutes(1).plusSeconds(1);
        }
        scheduler.scheduleAtFixedRate(RollDice.execute(jda), Duration.between(now, alarm0).toSeconds(), 60, TimeUnit.SECONDS);
    }
}
