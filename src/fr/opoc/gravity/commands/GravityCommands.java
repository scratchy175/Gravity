package fr.opoc.gravity.commands;

import fr.opoc.gravity.GravityMain;
import fr.opoc.gravity.GravityState;
import fr.opoc.gravity.tasks.GravityGame;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GravityCommands implements CommandExecutor {

    private GravityMain main;

    public GravityCommands(GravityMain main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage("§7[§6Gravity§7] §7/gravity join");
            p.sendMessage("§7[§6Gravity§7] §7/gravity arena");

        }
        else if (args[0].equalsIgnoreCase("join")) {
            if(main.getConfig().getConfigurationSection("lobbyspawn")!=null) {
                Location loc = new Location(Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
                loc.setWorld(Bukkit.getWorld(main.getConfig().getString("lobbyspawn.world")));
                loc.setPitch((float) main.getConfig().getDouble("lobbyspawn.pitch"));
                loc.setYaw((float) main.getConfig().getDouble("lobbyspawn.yaw"));
                loc.setX(main.getConfig().getDouble("lobbyspawn.x"));
                loc.setY(main.getConfig().getDouble("lobbyspawn.y"));
                loc.setZ(main.getConfig().getDouble("lobbyspawn.z"));
                p.teleport(loc);
                p.getInventory().clear();
                p.setFoodLevel(20);
                p.setHealth(20);
                if(!main.isState((GravityState.WAITING))) {
                    Bukkit.getScheduler().runTaskLater(main, () -> p.setGameMode(GameMode.SPECTATOR), 20L);
                    p.sendMessage("§4La partie à déjà commencé.");
                    return true;
                }

                if(!main.getPlayers().contains(p)) {

                    main.getPlayers().add(p);
                    GravityGame.FinishedPlayerLevel.put(p, 1);
                    GravityGame.Fails.put(p,0);
                }
                Bukkit.getScheduler().runTaskLater(main, () -> p.setGameMode(GameMode.ADVENTURE), 20L);
                p.sendMessage("§aVous avez rejoins la partie.");
                GravityGame game = new GravityGame(main);
                game.start();
                return true;
            }
            else {
                p.sendMessage("§aVous devez d'abord définir le spawn du lobby.");
                p.sendMessage("&7Faites /gravity setlobbyspawn");
            }
            return true;
        }
        else if(args[0].equalsIgnoreCase("arena")) {
            if(p.hasPermission("gravity.admin") || p.hasPermission("gravity.arena") || p.isOp()) {
                if(args.length < 2) {
                    p.sendMessage("§7[§6Gravity§7] §7/gravity arena add <arena>");
                    p.sendMessage("§7[§6Gravity§7] §7/gravity arena remove <arena>");
                    p.sendMessage("§7[§6Gravity§7] §7/gravity arena list");
                    return true;
                }
                if(args[1].equalsIgnoreCase("add")) {
                    main.getConfig().set("arenas." + args[2] + ".world", p.getWorld().getName());
                    main.getConfig().set("arenas." + args[2] + ".x", p.getLocation().getX());
                    main.getConfig().set("arenas." + args[2] + ".y", p.getLocation().getY());
                    main.getConfig().set("arenas." + args[2] + ".z", p.getLocation().getZ());
                    main.getConfig().set("arenas." + args[2] + ".yaw", p.getLocation().getYaw());
                    main.getConfig().set("arenas." + args[2] + ".pitch", p.getLocation().getPitch());
                    main.saveConfig();
                    GravityGame.arenas.add(args[2]);
                    p.sendMessage("§al'arène§6 " + args[2] + " §aa été crée avec succès.");
                    return true;
                }
                else if(args[1].equalsIgnoreCase("remove")) {
                    main.getConfig().set("arenas." + args[2],null);
                    main.saveConfig();
                    GravityGame.arenas.remove(args[2]);
                    p.sendMessage("§al'arène§6 " + args[2] + " §aa été supprimé avec succès.");
                    return true;
                }
                else if(args[1].equalsIgnoreCase("list")) {
                    p.sendMessage("§aNombre d'arènes actives : " + GravityGame.arenas.size());
                    p.sendMessage("§aListe d'arènes : " + String.join("§a, §6",GravityGame.arenas));
                    return true;
                }

            }
            else {
                p.sendMessage("§4Vous n'avez pas la permission.");
            }
        }
        else if(args[0].equalsIgnoreCase("setlobbyspawn")) {
            if(p.hasPermission("gravity.admin") || p.hasPermission("gravity.setlobbyspawn") || p.isOp()) {
                    main.getConfig().set("lobbyspawn.world", p.getWorld().getName());
                    main.getConfig().set("lobbyspawn.x", p.getLocation().getX());
                    main.getConfig().set("lobbyspawn.y", p.getLocation().getY());
                    main.getConfig().set("lobbyspawn.z", p.getLocation().getZ());
                    main.getConfig().set("lobbyspawn.yaw", p.getLocation().getYaw());
                    main.getConfig().set("lobbyspawn.pitch", p.getLocation().getPitch());
                    main.saveConfig();
                    p.sendMessage("§aLe spawn du lobby a été crée avec succès.");
                    return true;
            }
            else {
                p.sendMessage("§4Vous n'avez pas la permission.");
            }
        }
        return false;
    }
}
