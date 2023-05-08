package com.omniscient.omnibalance.Core.Items;

import com.omniscient.omnibalance.Core.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ItemFactory {
    public static final ItemStack BACK = ItemFactory.make("&eBack", "&7Click to go back.", Material.ARROW);

    public static final int CHAR_LIMIT = 30;

    public static List<String> parseLore(String lore, int charLimit){
        String[] loreArray = lore.split("\\n");
        List<String> finalLore = new ArrayList<>();
        for (String s : loreArray) {
            if (s.startsWith("¬")) {
                finalLore.add(Core.color(s.substring(1)));
                continue;
            }

            String line = s;

            int indent = 0;
            while(line.startsWith(" ")){
                line = line.substring(1);
                indent++;
            }

            while (line.length() > charLimit) {
                int lastIndex = line.substring(charLimit).indexOf(" ") + charLimit;
                finalLore.add(Core.color(line.substring(0, lastIndex).trim().indent(indent)));
                line = line.substring(lastIndex).trim();
            }
            finalLore.add(Core.color(line.indent(indent)));
        }

        for (int i = 1; i < finalLore.size(); i++)
            finalLore.set(i, ChatColor.getLastColors(finalLore.get(i-1)) + finalLore.get(i));

        return finalLore;
    }
    public static List<String> parseLore(String lore){
        return parseLore(lore, CHAR_LIMIT);
    }

    public static ItemStack make(String name, String lore, Material material, int amount){
        ItemStack stack = new ItemStack(material, amount);

        ItemMeta meta = stack.getItemMeta();
        assert meta != null;

        meta.setDisplayName(Core.color(name));

        meta.setLore(parseLore(lore));


        stack.setItemMeta(meta);
        return stack;
    }
    public static ItemStack make(String name, String lore, Material material){
        return make(name, lore, material, 1);
    }

    public static ItemStack addLore(ItemStack stack, String lore){
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;

        List<String> loreList = new ArrayList<>(meta.getLore() == null ? new ArrayList<>() : meta.getLore());
        loreList.addAll(parseLore(lore));

        meta.setLore(loreList);

        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack action(ItemStack stack, BiFunction<ItemStack, ItemMeta, ItemStack> action){
        return action.apply(stack, stack.getItemMeta());
    }
}
