package com.omniscient.omnibalance.Core.Command;

import com.omniscient.omnibalance.Core.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class OmniCommand {
    protected String trigger;
    protected String permission;
    protected String description;
    protected List<String> aliases = new ArrayList<>();
    protected boolean consoleExecutable = true;
    protected List<OmniParameter<?>> parameters = new ArrayList<>();

    public OmniCommand(String trigger, String permission, String description){
        this.trigger = trigger;
        this.permission = permission;
        this.description = description;
    }

    public String getTrigger() {
        return trigger;
    }
    public String getPermission() {
        return permission;
    }
    public String getDescription() {
        return description;
    }
    public List<String> getAliases(){
        return aliases;
    }
    public boolean isConsoleExecutable() {
        return consoleExecutable;
    }
    public List<OmniParameter<?>> getParameters() {
        return parameters;
    }

    public String makeUsage(){
        return "/"+trigger+parameters.stream().map(p -> (p.isRequired() ? "<" : "[")+p.getName()+(p.isRequired() ? ">" : "]")).reduce("", (a, b) -> a+" "+b);
    }
    public boolean canExecute(CommandSender sender){
        if(sender instanceof Player)
            return sender.hasPermission(permission) || sender.isOp();
        return consoleExecutable;
    }

    public abstract void onCommand(CommandSender sender, Map<String, Object> parameters);

    public <T> List<String> onTabComplete(CommandSender sender, List<String> parameters) {
        try{
            OmniParameter<T> p = (OmniParameter<T>) this.parameters.get(parameters.size() == 0 ? 0 : parameters.size()-1);
            if(p.values == null) return new ArrayList<>();
            return p.getValues().stream().map(p::stringify).toList();
        }catch (Exception ex){
            return new ArrayList<>();
        }

    }
}
