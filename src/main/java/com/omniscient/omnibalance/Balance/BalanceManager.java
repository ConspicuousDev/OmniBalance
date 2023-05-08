package com.omniscient.omnibalance.Balance;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.omniscient.omnibalance.Core.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BalanceManager implements Listener {
    public static final Map<Player, BalanceData> store = new HashMap<>();

    public static void loadData(Player... players) {
        ObjectNode json = Core.readJSONConfig("database");
        Arrays.stream(players)
                .forEach(player -> {
                    BalanceData data = new BalanceData();
                    if(json.has(player.getUniqueId().toString()))
                        data.fromJSON((ObjectNode) json.get(player.getUniqueId().toString()));
                    store.put(player, data);
                });
    }

    public static void saveData(Player... players) {
        ObjectNode json = Core.readJSONConfig("database");
        Arrays.stream(players)
                .forEach(player -> {
                    BalanceData data = store.remove(player);
                    if(data == null) data = new BalanceData();
                    json.set(player.getUniqueId().toString(), data.toJSON());
                });
        Core.saveJSONConfig("database", json);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        loadData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        saveData(e.getPlayer());
    }
}