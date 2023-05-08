package com.omniscient.omnibalance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omniscient.omnibalance.Balance.BalanceManager;
import com.omniscient.omnibalance.Commands.BalanceCommand;
import com.omniscient.omnibalance.Commands.EarnCommand;
import com.omniscient.omnibalance.Commands.GiveCommand;
import com.omniscient.omnibalance.Commands.SetBalanceCommand;
import com.omniscient.omnibalance.Core.Core;
import com.omniscient.omnibalance.Core.Localization.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class OmniBalance extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Core.init(this, "&3[&eOmni&fBalance&3]");

        Core.registerCommand(new BalanceCommand());
        Core.registerCommand(new GiveCommand());
        Core.registerCommand(new SetBalanceCommand());
        Core.registerCommand(new EarnCommand());

        Core.registerListener(new BalanceManager());

        if(!new File(Core.PLUGIN.getDataFolder(), "database.json").exists())
            Core.saveJSONConfig("database", new ObjectMapper().createObjectNode());
        BalanceManager.loadData(Bukkit.getOnlinePlayers().toArray(new Player[0]));

    }

    @Override
    public void onDisable() {
        BalanceManager.saveData(BalanceManager.store.keySet().toArray(new Player[0]));

        Core.consoleLog(Strings.make("core.disabled"));
    }
}
