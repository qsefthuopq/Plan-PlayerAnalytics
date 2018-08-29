package com.djrapitops.plan.system.listeners.bukkit;

import com.djrapitops.plan.Plan;
import com.djrapitops.plan.system.processing.Processing;
import com.djrapitops.plan.system.processing.processors.CommandProcessor;
import com.djrapitops.plan.system.settings.Permissions;
import com.djrapitops.plan.system.settings.Settings;
import com.djrapitops.plugin.logging.L;
import com.djrapitops.plugin.logging.error.ErrorHandler;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import javax.inject.Inject;

/**
 * Event Listener for PlayerCommandPreprocessEvents.
 *
 * @author Rsl1122
 */
public class CommandListener implements Listener {

    private final Plan plugin;
    private final Processing processing;
    private final ErrorHandler errorHandler;

    @Inject
    public CommandListener(
            Plan plugin,
            Processing processing,
            ErrorHandler errorHandler
    ) {
        this.plugin = plugin;
        this.processing = processing;
        this.errorHandler = errorHandler;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        boolean hasIgnorePermission = event.getPlayer().hasPermission(Permissions.IGNORE_COMMAND_USE.getPermission());
        if (event.isCancelled() || hasIgnorePermission) {
            return;
        }

        try {
            actOnCommandEvent(event);
        } catch (Exception e) {
            errorHandler.log(L.ERROR, this.getClass(), e);
        }
    }

    private void actOnCommandEvent(PlayerCommandPreprocessEvent event) {
        String commandName = event.getMessage().substring(1).split(" ")[0].toLowerCase();

        boolean logUnknownCommands = Settings.LOG_UNKNOWN_COMMANDS.isTrue();
        boolean combineCommandAliases = Settings.COMBINE_COMMAND_ALIASES.isTrue();

        if (!logUnknownCommands || combineCommandAliases) {
            Command command = getBukkitCommand(commandName);
            if (command == null) {
                if (!logUnknownCommands) {
                    return;
                }
            } else if (combineCommandAliases) {
                commandName = command.getName();
            }
        }
        processing.submit(new CommandProcessor(commandName));
    }

    private Command getBukkitCommand(String commandName) {
        Command command = plugin.getServer().getPluginCommand(commandName);
        if (command == null) {
            try {
                command = plugin.getServer().getCommandMap().getCommand(commandName);
            } catch (NoSuchMethodError ignored) {
                /* Ignored, Bukkit 1.8 has no such method */
            }
        }
        return command;
    }
}
