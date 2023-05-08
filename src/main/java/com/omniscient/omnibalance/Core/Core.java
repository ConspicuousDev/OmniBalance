package com.omniscient.omnibalance.Core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.omniscient.omnibalance.Core.Command.CommandManager;
import com.omniscient.omnibalance.Core.Command.OmniCommand;
import com.omniscient.omnibalance.Core.GUI.GUIListener;
import com.omniscient.omnibalance.Core.Localization.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Core {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Event{
    }

    public static Plugin PLUGIN;
    public static String PREFIX = "&3[&bOmni&fCore&3]";

    public static void init(Plugin plugin, String prefix){
        PLUGIN = plugin;
        PREFIX = prefix;

        Strings.load(readFileFromResources("/localization.json"));

        registerListener(new CommandManager());
        registerListener(new GUIListener());

        consoleLog(Strings.make("core.enabled"));
    }

    public static void registerListener(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, PLUGIN);
    }
    public static void registerCommand(OmniCommand command){
        try {
            SimplePluginManager simplePluginManager = (SimplePluginManager) Bukkit.getPluginManager();
            Field commandMapField = SimplePluginManager.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) commandMapField.get(simplePluginManager);
            Command bukkitCommand = new Command(command.getTrigger(), command.getDescription(), command.makeUsage(), command.getAliases()) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    return false;
                }
            };
            bukkitCommand.setPermission(command.getPermission());
            simpleCommandMap.register(PLUGIN.getName().toLowerCase(), bukkitCommand);
            CommandManager.registeredCommands.add(command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static String color(Object o){
        return ChatColor.translateAlternateColorCodes('&', o.toString());
    }
    public static String stripColor(Object o){
        return o.toString().replaceAll("&[0-9a-fA-F]", "");
    }
    public static void consoleLog(Object o){
        Bukkit.getConsoleSender().sendMessage(color(PREFIX + "&f " + o));
    }
    public static String readFileFromResources(String fileName) {
        Scanner scanner = new Scanner(Objects.requireNonNull(Core.class.getResourceAsStream(fileName)), StandardCharsets.UTF_8);
        scanner.useDelimiter("\\A");
        return scanner.next();
    }
    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> klass = type;
        while (klass != Object.class) {
            for (final Method method : klass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation))
                    methods.add(method);
            }
            klass = klass.getSuperclass();
        }
        return methods;
    }
    public static void saveJSONConfig(String name, ObjectNode jsonObject){
        File dataFolder = PLUGIN.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        try {
            new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(dataFolder, name + ".json"),
                            jsonObject
                    );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ObjectNode readJSONConfig(String name){
        File dataFolder = PLUGIN.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        try {
            return new ObjectMapper()
                    .readValue(
                            new File(dataFolder, name + ".json"),
                            ObjectNode.class
                    );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
