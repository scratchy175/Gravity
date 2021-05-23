package fr.opoc.gravity.tasks;

import fr.opoc.gravity.GravityMain;
import fr.opoc.gravity.GravityState;
import fr.opoc.gravity.utils.ItemsManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartTimer extends BukkitRunnable {

    private GravityMain main;
    private boolean isTimerSet;
    private int timer;

    public StartTimer(GravityMain main) {
        this.main=main;
    }

    @Override
    public void run() {
        GravityGame game = new GravityGame(main);
        if(!isTimerSet) {
            timer= main.getConfig().getInt("timer_jump");
            isTimerSet= true;
        }
        if ((timer > 0)) {
            game.sendtitle("§4§l" + timer,"§eavant le début",1,500,1);
        }
        else {
            game.sendtitle("§2§lC'est parti !","",10,10,10);
            game.Blocks("AIR");
            for (Player p : main.getPlayers()) {
                ItemsManager item = new ItemsManager(main);
                item.giverestartItem(p);
            }
            main.setState(GravityState.PLAYING);
            game.runTaskTimer(main,0,20);
            cancel();
        }
        timer--;
    }
}
