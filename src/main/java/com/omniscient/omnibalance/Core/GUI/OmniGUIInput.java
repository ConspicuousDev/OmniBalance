package com.omniscient.omnibalance.Core.GUI;

import com.omniscient.omnibalance.Core.Core;
import com.omniscient.omnibalance.Core.Localization.Strings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OmniGUIInput {
    private Player player;
    private OmniGUI gui;
    private String name;
    private int[] time = {15};
    private BiConsumer<Player, String> onReceive = (player, message) -> player.sendMessage(Strings.make("general.error"));
    private Consumer<Player> onFail = (player) -> {};
    public OmniGUIInput(Player player, OmniGUI gui, String name){
        this.player = player;
        this.gui = gui;
        this.name = name;
    }
    public OmniGUIInput(Player player, OmniGUI gui, String name, int time){
        this.player = player;
        this.gui = gui;
        this.name = name;
        this.time[0] = time;
    }

    public Player getPlayer() {
        return player;
    }
    public String getName() {
        return name;
    }
    public int getTime() {
        return time[0];
    }
    public BiConsumer<Player, String> getOnReceive() {
        return onReceive;
    }
    public Consumer<Player> getOnFail() {
        return onFail;
    }

    public void run(BiConsumer<Player, String> onReceive, Consumer<Player> onFail){
        this.onReceive = onReceive;
        this.onFail = onFail;
        player.closeInventory();
        GUIListener.listening.put(player.getUniqueId(), this);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!GUIListener.listening.containsKey(player.getUniqueId())){
                    cancel();
                    return;
                }
                if(time[0] < 0){
                    GUIListener.listening.remove(player.getUniqueId());
                    fail();
                    cancel();
                    return;
                }
                player.sendTitle(Core.color("&eType the "+name+" in chat."), Core.color("&c"+ time[0] +" second(s)"));
                time[0]--;
            }
        }.runTaskTimer(Core.PLUGIN, 0, 20);
    }
    public void run(BiConsumer<Player, String> onReceive){
        run(onReceive, onFail);
    }

    public void receive(String message){
        onReceive.accept(player, message);
        player.sendTitle("", "");
        gui.open(player);
    }

    public void fail(){
        onFail.accept(player);
        player.sendTitle("", "");
        gui.open(player);
    }
}
