package fr.opoc.gravity.tasks;

import fr.opoc.gravity.GravityMain;
import fr.opoc.gravity.GravityState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GAutoStartTask extends BukkitRunnable {

    private GravityMain main;
    private int timer;
    private boolean istimerset= false;



    public GAutoStartTask(GravityMain main) {
        this.main=main;
    }



    @Override
    public void run() {
        GravityGame game = new GravityGame(main);
        StartTimer start = new StartTimer(main);
        if (!istimerset) {
            this.timer=main.getConfig().getInt("timer_start");
            istimerset=true;
        }
        for(Player p : main.getPlayers()) {
            p.setLevel(timer);
        }
        if (timer ==10) {
            game.sendmessage("§eLa partie commence dans : §6" + timer + " §esecondes.");
        }
        if (timer ==5 || timer ==4 || timer ==3 || timer ==2) {
            game.sendmessage("§eLa partie commence dans : §c" + timer + " §esecondes.");
        }
        if (timer ==1) {
            game.sendmessage("§eLa partie commence dans : §c" + timer + " §eseconde.");
        }

        if(timer ==0) {
            game.sendmessage("§a§lLancement du jeu !");
            for(Player p : main.getPlayers()) {
                Location loc = new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                loc.setWorld(Bukkit.getWorld(main.getConfig().getString("arenas." + GravityGame.arenas.get(0) + ".world")));
                loc.setPitch((float) main.getConfig().getDouble("arenas." + GravityGame.arenas.get(0) + ".pitch"));
                loc.setYaw((float) main.getConfig().getDouble("arenas." + GravityGame.arenas.get(0) + ".yaw"));
                loc.setX(main.getConfig().getDouble("arenas." + GravityGame.arenas.get(0) + ".x"));
                loc.setY(main.getConfig().getDouble("arenas." + GravityGame.arenas.get(0) + ".y"));
                loc.setZ(main.getConfig().getDouble("arenas." + GravityGame.arenas.get(0) + ".z"));
                p.teleport(loc);
                Bukkit.getScheduler().runTaskLater(main, () -> p.setGameMode(GameMode.ADVENTURE), 20L);
            }
            main.setState(GravityState.STARTING);
            start.runTaskTimer(main,0,20);
            cancel();
        }
        timer--;
    }
}
