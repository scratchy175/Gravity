package fr.opoc.gravity.listeners;

import fr.opoc.gravity.GravityMain;
import fr.opoc.gravity.GravityState;
import fr.opoc.gravity.tasks.GravityGame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

public class GravityListeners implements Listener {

    private GravityMain main;

    public GravityListeners(GravityMain main) {
        this.main = main;

    }

    @EventHandler
    public void damage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(main.getPlayers().contains(p)) {
                if(e.getFinalDamage() >= p.getHealth()) {
                    e.setCancelled(true);
                    GravityGame game = new GravityGame(main);
                    game.sendmessage("§7" + p.getName()+ "§a s'est écrasé au sol.");
                    game.restartStage(p);
                    p.setLevel(p.getLocation().getBlockY());
                    p.setHealth(20);
                }

                else if(e.getCause() != EntityDamageEvent.DamageCause.FALL) {
                    e.setCancelled(true);
                }
            }

        }
    }
    @EventHandler
    public void move(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        GravityGame game = new GravityGame(main);
        if(main.isState(GravityState.PLAYING) && main.getPlayers().contains(p)) {
            p.setLevel(p.getLocation().getBlockY());
            if(p.getLocation().getBlock().getType().equals(Material.PORTAL)) {
                game.nextStage(p);
            }
        }

    }
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        GravityGame game = new GravityGame(main);
        Player p = e.getPlayer();
        if(main.isState(GravityState.PLAYING) && main.getPlayers().contains(p)){
            if(e.getHand().equals(EquipmentSlot.HAND)){
                if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                    if(p.getInventory().getItemInMainHand().getType().equals(Material.valueOf(main.getConfig().getString("items.restart.material")))){
                        game.restartStage(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e)
    {
        Player p = (Player)e.getEntity();
        if(e.getEntity() instanceof Player && main.getPlayers().contains(p)) {
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(main.getPlayers().contains(p)) {
            main.getPlayers().remove(p);
            e.setQuitMessage("§7" + p.getName() + " §ca quitté la partie.");

        }
        if(main.isState(GravityState.PLAYING) && main.getPlayers().size()==0) {
            GravityGame game = new GravityGame(main);
            game.restart();
        }
    }
}
