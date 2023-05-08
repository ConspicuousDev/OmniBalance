package com.omniscient.omnibalance.Core.Command.Parameters;

import com.omniscient.omnibalance.Core.Command.OmniParameter;
import org.bukkit.NamespacedKey;

import java.util.List;
import java.util.function.Supplier;

public class NamespacedKeyParameter extends OmniParameter<NamespacedKey> {
    public NamespacedKeyParameter(String name, String permission, boolean required, Supplier<List<NamespacedKey>> values) {
        super(name, permission, required, values);
    }

    @Override
    public String stringify(NamespacedKey value) {
        return value.getNamespace()+":"+value.getKey();
    }

    @Override
    public NamespacedKey typify(String s) throws ParameterParseException {
        String[] parts = s.split(":");
        if(parts.length != 2) throw new ParameterParseException(this, "must follow the pattern &7namespace:key");
        return new NamespacedKey(parts[0], parts[1]);
    }
}
