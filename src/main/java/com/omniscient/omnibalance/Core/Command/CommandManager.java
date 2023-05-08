package com.omniscient.omnibalance.Core.Command;

import com.omniscient.omnibalance.Core.Core;
import com.omniscient.omnibalance.Core.Localization.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements Listener {
    public static List<OmniCommand> registeredCommands = new ArrayList<>();

    public void preProcessCommand(CommandSender sender, String message, Cancellable e){
        List<String> parts = Arrays.stream(message.split(" ")).toList();
        String trigger = parts.get(0).substring((sender instanceof Player) ? 1 : 0);
        List<String> parameters = parts.subList(1, parts.size());
        OmniCommand command = registeredCommands.stream()
                .filter(c -> c.getTrigger().equalsIgnoreCase(trigger) || c.getAliases().stream().anyMatch(a -> a.equalsIgnoreCase(trigger.toLowerCase())))
                .findFirst()
                .orElse(null);
        if(command == null) return;
        e.setCancelled(true);
        if(!command.canExecute(sender)){
            sender.sendMessage(Strings.make("command.no_permission"));
            return;
        }
        HashMap<String, Object> parsedParameters = new HashMap<>();
        for (int i = 0; i < command.getParameters().size(); i++) {
            OmniParameter p = command.getParameters().get(i);
            if(i >= parameters.size()){
                if(sender instanceof Player && !p.isRequired()) continue;
                sender.sendMessage(Strings.make("command.usage", command.makeUsage()));
                return;
            }
            if(!p.canExecute(sender)){
                sender.sendMessage(Strings.make("command.no_permission"));
                return;
            }
            try {
                Object parsed = p.typify(parameters.get(i));
                parsedParameters.put(p.getName(), parsed);
            }catch (OmniParameter.ParameterParseException ex){
                sender.sendMessage(Strings.make("command.parse_error", ex.getParameter().getName(), ex.getErrorMessage()));
                return;
            }
        }
        command.onCommand(sender, parsedParameters);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e){
        e.getCompletions().clear();
        List<String> parts = new ArrayList<>(Arrays.stream((e.getBuffer()).split(" ")).toList());
        if(e.getBuffer().endsWith(" ")) parts.add("");
        String trigger = (parts.size() > 0) ? parts.get(0).substring((e.getSender() instanceof Player) ? 1 : 0) : "";
        if(trigger.equals("") || parts.size() == 1){
            e.getCompletions().addAll(registeredCommands.stream()
                    .filter(command -> command.canExecute(e.getSender()))
                    .filter(command -> command.trigger.toLowerCase().startsWith(trigger.toLowerCase()))
                    .map(command -> {
                        List<String> commands = new ArrayList<>();
                        commands.add(command.getTrigger());
                        commands.addAll(command.getAliases());
                        return commands;
                    }).reduce(new ArrayList<>(), (a, b) -> {
                        a.addAll(b);
                        return a;
                    }));
        }else{
            OmniCommand command = registeredCommands.stream()
                    .filter(c -> c.getTrigger().equalsIgnoreCase(trigger) || c.getAliases().stream()
                            .anyMatch(a -> a.equalsIgnoreCase(trigger.toLowerCase())))
                    .findFirst()
                    .orElse(null);
            if(command == null) return;
            e.getCompletions().addAll(command.onTabComplete(e.getSender(), parts.subList(1, parts.size())));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPreProcessCommand(PlayerCommandPreprocessEvent e){
        if(e.isCancelled()) return;
        preProcessCommand(e.getPlayer(), e.getMessage(), e);
    }

    @EventHandler
    public void onConsolePreProcessCommand(ServerCommandEvent e){
        preProcessCommand(e.getSender(), e.getCommand(), e);
    }


}