package com.omniscient.omnibalance.Core.GUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class OmniGUI {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Click{
        int[] slots() default {};
        InventoryAction[] actions() default {};
    }

    protected Player player;
    protected String title = "Unnamed OmniGUI";
    protected int size = 54;

    public void open(Player player){
        this.player = player;
        if(player.getOpenInventory() != null && player.getOpenInventory().getTopInventory() != null) {
            player.closeInventory();
        }
        GUIListener.guis.put(player.getUniqueId(), this);
        player.openInventory(makeInventory());
    }

    public void update(){
        GUIListener.guis.remove(player.getUniqueId());
        Inventory inventory = makeInventory();
        player.openInventory(inventory);
        GUIListener.guis.put(player.getUniqueId(), this);
    }

    public abstract void makeItems(ItemStack[] items);
    public Inventory makeInventory(){
        Inventory inventory = Bukkit.createInventory(null, this.size, this.title);
        ItemStack[] items = new ItemStack[this.size];
        makeItems(items);
        inventory.setContents(items);
        return inventory;
    }


    public void onOpen(InventoryOpenEvent e){
    }
    public void onClose(InventoryCloseEvent e){
        GUIListener.guis.remove(e.getPlayer().getUniqueId());
    }
}
