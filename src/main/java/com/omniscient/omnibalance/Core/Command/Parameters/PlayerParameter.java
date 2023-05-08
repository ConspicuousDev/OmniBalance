package com.omniscient.omnibalance.Core.Command.Parameters;

import com.omniscient.omnibalance.Core.Command.OmniParameter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Supplier;

public class PlayerParameter extends OmniParameter<Player> {
    public PlayerParameter(String name, String permission, boolean required, Supplier<List<Player>> values) {
        super(name, permission, required, values);
    }

    @Override
    public String stringify(Player value) {
        return value.getName();
    }

    @Override
    public Player typify(String s) throws ParameterParseException {
        Player p = Bukkit.getPlayer(s);
        if(p == null) throw new ParameterParseException(this, "is not an online player");
        return p;
    }
}
