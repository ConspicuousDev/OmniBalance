package com.omniscient.omnibalance.Commands;

import com.omniscient.omnibalance.Balance.BalanceData;
import com.omniscient.omnibalance.Balance.BalanceManager;
import com.omniscient.omnibalance.Core.Command.OmniCommand;
import com.omniscient.omnibalance.Core.Command.Parameters.IntegerParameter;
import com.omniscient.omnibalance.Core.Command.Parameters.PlayerParameter;
import com.omniscient.omnibalance.Core.Core;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Map;

public class GiveCommand extends OmniCommand {
    public GiveCommand() {
        super("give", "omnibalance.give", "Subtracts the amount from the user's balance and adds the amount to the target's balance.");
        this.aliases.add("pay");
        this.parameters.add(new PlayerParameter("target", null, true, () -> BalanceManager.store.keySet().stream().toList()));
        this.parameters.add(new IntegerParameter("amount", null, true, null));
        this.consoleExecutable = true;
    }

    @Override
    public void onCommand(CommandSender sender, Map<String, Object> parameters) {
        BigInteger amount = BigInteger.valueOf((int) parameters.get("amount"));
        if (sender instanceof Player) {
            if (amount.compareTo(BigInteger.ZERO) <= 0) {
                sender.sendMessage(Core.color("&cYou need to specify an amount greater than zero."));
                return;
            }
            BalanceData userData = BalanceManager.store.get(sender);
            if (userData.getBalance().compareTo(amount) < 0) {
                sender.sendMessage(Core.color("&cYou don't have enough money to do this."));
                return;
            }
            userData.setBalance(userData.getBalance().subtract(amount));
        }
        Player target = (Player) parameters.get("target");
        BalanceData targetData = BalanceManager.store.get(target);
        targetData.setBalance(targetData.getBalance().add(amount));
        sender.sendMessage(Core.color("&3You sent &a" + NumberFormat.getCurrencyInstance().format(amount) + "&3 to " + target.getName() + "."));
        target.sendMessage(Core.color("&3" + sender.getName() + " sent you &a" + NumberFormat.getCurrencyInstance().format(amount) + "&3."));
    }
}
