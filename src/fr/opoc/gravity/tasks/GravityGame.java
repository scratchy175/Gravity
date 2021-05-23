package fr.opoc.gravity.tasks;

import fr.opoc.gravity.GravityMain;
import fr.opoc.gravity.GravityState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GravityGame extends BukkitRunnable {

    private GravityMain main;
    private int timer;
    public static HashMap<Player, Integer> FinishedPlayerLevel = new HashMap<>();
    public static HashMap<Player, Integer> Fails = new HashMap<>();
    public static List<String> arenas = new ArrayList<>();
    private boolean isTimerSet= false;

    public GravityGame(GravityMain main) {
        this.main=main;
    }


    @Override
    public void run() {
        if(!isTimerSet) {
            timer=main.getConfig().getInt("timer_max");
            isTimerSet=true;
        }
        if(main.isState(GravityState.PLAYING)){
        for (Player p : main.getPlayers()) {
            List <String> color = new ArrayList<>();

            for (int i=0; i<arenas.size();i++){
                if(FinishedPlayerLevel.get(p)-1>i) {
                    color.add(i,"§6⬛");
                }
                else if(FinishedPlayerLevel.get(p)>i){
                    color.add(i,"§7⬛");

                }
                else color.add(i,"§f⬛");
            }
           p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§fProgression §8» " +String.join("",color) + " §7┃ §fFails §8» §c"+ Fails.get(p)));
        }}
        if(timer==0 && main.isState(GravityState.PLAYING)) {
            sendmessage("§a§lTemps écoulé !");
            restart();
            cancel();
        }
        timer--;

    }
    public void sendmessage(String message) {
        for (Player p : main.getPlayers()) {
            p.sendMessage(message);
        }
    }
    public void sendtitle(String title,String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player p : main.getPlayers()) {
            p.sendTitle(title,subtitle,fadeIn,stay,fadeOut);
        }
    }

    public void start(){
        if(main.isState(GravityState.WAITING) && main.getPlayers().size()==main.getConfig().getInt("nb_players")) {

            GAutoStartTask start = new GAutoStartTask(main);
            start.runTaskTimer(main,0,20);
           // main.setState(GravityState.STARTING);


        }

    }
    public void Blocks (String material) {
        for(int x=7;x<=9;x++){
            for(int z=46; z<=48; z++){
                Bukkit.getWorld("Gravity").getBlockAt(x,234,z).setType(Material.valueOf(material));
            }
        }

    }
    public void restart() {
        main.setState(GravityState.WAITING);
        for(Player p : main.getPlayers()) {
            Location spawn = new Location(Bukkit.getWorld("world"), 0, 64, 0,-0,0);
            p.getInventory().clear();
            p.setLevel(0);
            p.teleport(spawn);
            p.setHealth(20);
        }
        main.getPlayers().clear();
        Blocks("STAINED_GLASS");

    }

    public void nextStage (Player p) {
        if(main.isState(GravityState.PLAYING) && main.getPlayers().contains(p)){
            if (FinishedPlayerLevel.get(p) <= arenas.size()-1) {
                Location loc = new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                loc.setWorld(Bukkit.getWorld(main.getConfig().getString("arenas." + arenas.get(FinishedPlayerLevel.get(p)) + ".world")));
                loc.setPitch((float) main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)) + ".pitch"));
                loc.setYaw((float) main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)) + ".yaw"));
                loc.setX(main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)) + ".x"));
                loc.setY(main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)) + ".y"));
                loc.setZ(main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)) + ".z"));
                p.teleport(loc);
                FinishedPlayerLevel.put(p, FinishedPlayerLevel.get(p) +1);
                p.sendTitle("","§fNiveau" + FinishedPlayerLevel.get(p),10,10,10);
            }
            else makewin(p);
        }
    }
    public void restartStage (Player p) {
        //if(main.isState(GravityState.PLAYING) && main.getPlayers().contains(p)){
            Location loc = new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            loc.setWorld(Bukkit.getWorld(main.getConfig().getString("arenas." + arenas.get(FinishedPlayerLevel.get(p)-1) + ".world")));
            loc.setPitch((float) main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)-1) + ".pitch"));
            loc.setYaw((float) main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)-1) + ".yaw"));
            loc.setX(main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)-1) + ".x"));
            loc.setY(main.getConfig().getDouble("arenas." + arenas.get(FinishedPlayerLevel.get(p)-1) + ".y"));
            loc.setZ (main.getConfig ().getDouble ("arenas." + arenas.get (FinishedPlayerLevel.get (p) - 1) + ".z"));
            Bukkit.getScheduler().runTaskLater(main, () -> p.teleport(loc), 1L);
            p.setFallDistance(0.0f);
            Fails.put(p,Fails.get(p)+1);
       //}
    }

    public void makewin (Player p) {
                Bukkit.broadcastMessage("§7[§6Gravity§7] " + p.getName() + " §aà gagné la partie.");
                restart();
    }


}
