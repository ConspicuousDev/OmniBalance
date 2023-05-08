package com.omniscient.omnibalance.Core.Util;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class MiscUtils {
    public static void veinMine(Block origin, Material type){
        if(origin.getType() != type) return;
        origin.setType(Material.AIR);
        veinMine(origin.getLocation().clone().add(1, 0, 0).getBlock(), type);
        veinMine(origin.getLocation().clone().add(-1, 0, 0).getBlock(), type);
        veinMine(origin.getLocation().clone().add(0, 0, 1).getBlock(), type);
        veinMine(origin.getLocation().clone().add(0, 0, -1).getBlock(), type);
        veinMine(origin.getLocation().clone().add(0, 1, 0).getBlock(), type);
        veinMine(origin.getLocation().clone().add(0, -1, 0).getBlock(), type);
    }
}
