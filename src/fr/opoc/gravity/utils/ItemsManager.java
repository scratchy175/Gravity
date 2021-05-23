package fr.opoc.gravity.utils;

import fr.opoc.gravity.GravityMain;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemsManager {

    private GravityMain main;

    public ItemsManager(GravityMain main) {
        this.main=main;
    }

    public void giverestartItem(Player p){
        List<String> lore = new ArrayList<>();
        ItemStack restart = new ItemStack(Material.valueOf(main.getConfig().getString("items.restart.material")),main.getConfig().getInt("items.restart.amount"),(short)main.getConfig().getInt("items.restart.data"));
        ItemMeta meta = restart.getItemMeta();
        meta.setDisplayName("§4§lRestart");
        lore.add("");
        lore.add("§7Cliquez pour restart le niveau");
        meta.setLore(lore);
        restart.setItemMeta(meta);
        p.getInventory().setItem(main.getConfig().getInt("items.restart.slot"),restart);
    }
}
