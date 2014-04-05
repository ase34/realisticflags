package de.ase34.realisticflags.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.ase34.realisticflags.RealisticFlagsPlugin;

public class RespawnFlagsCommandExecutor implements CommandExecutor {

    public RealisticFlagsPlugin plugin;

    public RespawnFlagsCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.respawnFlags();
        int size = plugin.getFlags().size();
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '§',
                String.format("§7§o%d§r§7 " + (size == 1 ? "flag was" : "flags were")
                        + " respawned!", size)));
        return true;
    }
}
