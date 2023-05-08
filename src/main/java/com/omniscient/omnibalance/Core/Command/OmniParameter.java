package com.omniscient.omnibalance.Core.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Supplier;

public abstract class OmniParameter<T> {
    public static class ParameterParseException extends Exception {
        private final OmniParameter parameter;
        private final String errorMessage;
        public ParameterParseException(OmniParameter parameter, String errorMessage){
            this.parameter = parameter;
            this.errorMessage = errorMessage;
        }

        public OmniParameter getParameter() {
            return parameter;
        }
        public String getErrorMessage() {
            return errorMessage;
        }
    }

    protected String name;
    protected String permission;
    protected boolean required;
    protected Supplier<List<T>> values;
    public OmniParameter(String name, String permission, boolean required, Supplier<List<T>> values){
        this.name = name;
        this.permission = permission;
        this.required = required;
        this.values = values;
    }

    public String getName() {
        return name;
    }
    public String getPermission() {
        return permission;
    }
    public boolean isRequired() {
        return required;
    }
    public List<T> getValues() {
        return values.get();
    }

    public boolean canExecute(CommandSender sender){
        if(permission ==  null || permission.isEmpty()) return true;
        if(sender instanceof Player)
            return sender.hasPermission(permission) || sender.isOp();
        return sender instanceof ConsoleCommandSender;
    }

    public abstract String stringify(T value);

    public abstract T typify(String s) throws ParameterParseException;

    public boolean constrain(T value){
        return true;
    }
}
