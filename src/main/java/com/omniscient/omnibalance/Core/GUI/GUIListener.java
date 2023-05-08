package com.omniscient.omnibalance.Core.GUI;

import com.omniscient.omnibalance.Core.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GUIListener implements Listener {
    public static Map<UUID, OmniGUI> guis = new HashMap<>();
    public static final Map<UUID, OmniGUIInput> listening = new HashMap<>();

    public OmniGUI isOmniGUI(UUID uuid) {
        if (!guis.containsKey(uuid)) return null;
        return guis.get(uuid);
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent e) throws InvocationTargetException, IllegalAccessException {
        OmniGUI gui = isOmniGUI(e.getWhoClicked().getUniqueId());
        if (gui == null) return;
        e.setCancelled(true);
        Class<?> targetClass = gui.getClass();
        while (targetClass != null) {
            for (Method method : targetClass.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(OmniGUI.Click.class)) continue;
                OmniGUI.Click atClick = method.getAnnotation(OmniGUI.Click.class);
                if ((atClick.actions().length == 0 || Arrays.stream(atClick.actions()).anyMatch(action -> action == e.getAction())) &&
                        (atClick.slots().length == 0 || Arrays.stream(atClick.slots()).anyMatch(slot -> slot == e.getRawSlot())))
                    method.invoke(gui, e);
            }
            targetClass = targetClass.getSuperclass();
        }
    }

    @EventHandler
    public void onGUIOpen(InventoryOpenEvent e) {
        OmniGUI gui = isOmniGUI(e.getPlayer().getUniqueId());
        if (gui == null) return;
        gui.onOpen(e);
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        OmniGUI gui = isOmniGUI(e.getPlayer().getUniqueId());
        if (gui == null) return;
        gui.onClose(e);
    }

    @EventHandler
    public void onPlayerSendMessage(AsyncPlayerChatEvent e){
        if(!listening.containsKey(e.getPlayer().getUniqueId())) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                listening.remove(e.getPlayer().getUniqueId()).receive(e.getMessage().trim());
            }
        }.runTaskLater(Core.PLUGIN, 0);
        e.setCancelled(true);
    }
}
