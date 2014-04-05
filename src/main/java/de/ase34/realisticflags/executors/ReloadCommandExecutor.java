package de.ase34.realisticflags.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.ase34.realisticflags.RealisticFlagsPlugin;

public class ReloadCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public ReloadCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            boolean force = args.length > 0 ? args[args.length - 1].equalsIgnoreCase("force")
                    : false;
            String type = args.length > 0 ? args[0] : null;

            boolean allTypes = type == null || type.equalsIgnoreCase("force");
            boolean reloadFlags = allTypes || type.equalsIgnoreCase("flags");
            boolean reloadColorMaps = allTypes || type.equalsIgnoreCase("colormaps");
            if (!force) {
                if (reloadFlags) {
                    plugin.saveFlags();
                }
                if (reloadColorMaps) {
                    plugin.saveColorMaps();
                }
            }

            if (reloadFlags) {
                plugin.removeAllFlags();
                plugin.loadFlags();
                plugin.respawnFlags();
            }
            if (reloadColorMaps) {
                plugin.loadColorMaps();
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§7Flags and/or color maps reloaded!"));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
