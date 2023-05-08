package com.omniscient.omnibalance.Commands;

import com.omniscient.omnibalance.Balance.BalanceData;
import com.omniscient.omnibalance.Balance.BalanceManager;
import com.omniscient.omnibalance.Core.Command.OmniCommand;
import com.omniscient.omnibalance.Core.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Random;

public class EarnCommand extends OmniCommand {
    public EarnCommand() {
        super("earn", "omnibalance.earn", "Adds a random amount between 1 and 5 to the user's balance. Can be used every minute.");
        this.consoleExecutable = false;
    }

    @Override
    public void onCommand(CommandSender sender, Map<String, Object> parameters) {
        Player target = (Player) sender;
        BalanceData data = BalanceManager.store.get(target);
        if(System.currentTimeMillis()-data.getLastEarn() < 60*1000){
            sender.sendMessage(Core.color("&cThis action is on cooldown for "+(60-(System.currentTimeMillis()-data.getLastEarn())/1000))+" more second(s).");
            return;
        }
        int earn = new Random().nextInt(5)+1;
        data.setLastEarn(System.currentTimeMillis());
        data.setBalance(data.getBalance().add(BigInteger.valueOf(earn)));
        sender.sendMessage(Core.color("&a"+ NumberFormat.getCurrencyInstance().format(earn)+" &3was added to your balance."));
    }
}
