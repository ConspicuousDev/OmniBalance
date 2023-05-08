package com.omniscient.omnibalance.Commands;

import com.omniscient.omnibalance.Balance.BalanceData;
import com.omniscient.omnibalance.Balance.BalanceManager;
import com.omniscient.omnibalance.Core.Command.OmniCommand;
import com.omniscient.omnibalance.Core.Command.Parameters.IntegerParameter;
import com.omniscient.omnibalance.Core.Command.Parameters.PlayerParameter;
import com.omniscient.omnibalance.Core.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Map;

public class SetBalanceCommand extends OmniCommand {
    public SetBalanceCommand() {
        super("setbalance", "omnibalance.setbalance", "Sets the target's balance to the given amount.");
        this.aliases.add("setbal");
        this.parameters.add(new PlayerParameter("target", null, true, () -> BalanceManager.store.keySet().stream().toList()));
        this.parameters.add(new IntegerParameter("amount", null, true, null));
        this.consoleExecutable = true;
    }

    @Override
    public void onCommand(CommandSender sender, Map<String, Object> parameters) {
        Player target = (Player) parameters.get("target");
        BalanceData data = BalanceManager.store.get(target);
        data.setBalance(BigInteger.valueOf((int) parameters.get("amount")));
        sender.sendMessage(Core.color("&3"+target.getName()+"'s balance has been set to &a"+NumberFormat.getCurrencyInstance().format(data.getBalance())+"&3."));
        target.sendMessage(Core.color("&3Your balance has been set to &a"+ NumberFormat.getCurrencyInstance().format(data.getBalance())+"&3."));
    }
}
