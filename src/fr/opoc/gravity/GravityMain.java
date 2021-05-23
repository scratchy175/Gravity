package fr.opoc.gravity;

import fr.opoc.gravity.commands.GravityCommands;
import fr.opoc.gravity.listeners.GravityListeners;
import fr.opoc.gravity.tasks.GravityGame;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GravityMain extends JavaPlugin {

    private List<Player> players = new ArrayList<>();
    private GravityState state;

    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();
        setState(GravityState.WAITING);
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new GravityListeners(this), this);
        getCommand("gravity").setExecutor((CommandExecutor) new GravityCommands(this));
        if(this.getConfig().getConfigurationSection("arenas").getKeys(false) != null) {
            GravityGame.arenas.addAll(this.getConfig().getConfigurationSection("arenas").getKeys(false));
            System.out.println("Arenas loaded : " + GravityGame.arenas.size());

        }
    }

    public void setState(GravityState state){

        this.state=state;
    }
    public boolean isState(GravityState state){
        return this.state==state;
    }


    public List<Player> getPlayers(){
        return this.players;
    }


    @Override
    public void onDisable() {
        super.onDisable();
        GravityGame game = new GravityGame(this);
        game.restart();
    }

}
