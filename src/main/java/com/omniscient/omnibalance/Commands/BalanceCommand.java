package com.omniscient.omnibalance.Commands;

import com.omniscient.omnibalance.Balance.BalanceData;
import com.omniscient.omnibalance.Balance.BalanceManager;
import com.omniscient.omnibalance.Core.Command.OmniCommand;
import com.omniscient.omnibalance.Core.Command.Parameters.PlayerParameter;
import com.omniscient.omnibalance.Core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Map;

public class BalanceCommand extends OmniCommand {
    public BalanceCommand() {
        super("balance", "omnibalance.balance", "Returns the target's balance (the user is the target if none provided). ");
        this.aliases.add("bal");
        this.parameters.add(new PlayerParameter("target", null, false, () -> BalanceManager.store.keySet().stream().toList()));
        this.consoleExecutable = true;
    }

    @Override
    public void onCommand(CommandSender sender, Map<String, Object> parameters) {
        Player target;
        if((target = (Player) parameters.get("target")) == null)
            target = (Player) sender;

        BalanceData data = BalanceManager.store.get(target);
        sender.sendMessage(Core.color("&3"+target.getName()+"'s balance: &a"+ NumberFormat.getCurrencyInstance().format(data.getBalance())));
    }
}
