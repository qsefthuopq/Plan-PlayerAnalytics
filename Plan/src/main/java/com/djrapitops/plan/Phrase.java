package com.djrapitops.plan;

import org.bukkit.ChatColor;

/**
 *
 * @author Rsl1122
 */
public enum Phrase {
    DATABASE_TYPE_DOES_NOT_EXIST("That database type doesn't exist."),
    DATABASE_FAILURE_DISABLE("Database initialization has failed, disabling Plan."),
    PLANLITE_REG_HOOK("Registered additional hook, passed on to PlanLite: "),
    USERNAME_NOT_VALID(ChatColor.RED + "This Player doesn't exist."),
    USERNAME_NOT_SEEN(ChatColor.RED + "This Player has not played on this server."),
    USERNAME_NOT_KNOWN(ChatColor.RED + "Player not found from the database."),
    COLOR_MAIN(ChatColor.DARK_GREEN),
    COLOR_SEC(ChatColor.GRAY),
    COLOR_TER(ChatColor.WHITE),
    ARROWS_RIGHT("»"),
    BALL("•"),
    ERROR_PLANLITE("PlanLite not found, if you're have plugins using PlanAPI v1.6.0 download PlanLite."),
    ERROR_NO_USERNAME("INSPECT-GETNAME\nNo username given, returned empty username.\n"),
    COMMAND_SENDER_NOT_PLAYER(ChatColor.RED + "[Plan] This command can be only used as a player."),
    COMMAND_REQUIRES_ARGUMENTS(ChatColor.RED + "[Plan] Command requires arguments."),
    COMMAND_REQUIRES_ARGUMENTS_ONE(ChatColor.RED + "[Plan] Command requires one argument."),
    COMMAND_NO_PERMISSION(ChatColor.RED + "[Plan] You do not have the required permmission.");

    private final String text;
    private final ChatColor color;

    private Phrase(final String text) {
        this.text = text;
        this.color = null;
    }

    private Phrase(final ChatColor color) {
        this.color = color;
        this.text = "";
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * @return Color of the COLOR_ENUM
     */
    public ChatColor color() {
        return color;
    }
}