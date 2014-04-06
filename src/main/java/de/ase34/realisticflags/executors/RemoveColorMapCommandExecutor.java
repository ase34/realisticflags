package de.ase34.realisticflags.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.ase34.realisticflags.AbstractDyeColorMap;
import de.ase34.realisticflags.DyeColorMapPluginProxy;
import de.ase34.realisticflags.Flag;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class RemoveColorMapCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public RemoveColorMapCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        List<String> removed = new ArrayList<String>();

        String regex = args[0];

        for (String key : plugin.getColorMaps().keySet()) {
            if (key.matches(regex)) {
                List<String> linkedFlags = findLinkedFlags(key);
                if (!linkedFlags.isEmpty()) {
                    String joined = StringUtils.join(linkedFlags, ", ");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                            "§cThe color map %s is used in:\n§c§o%s\n§r§c"
                                    + "Please remove those flags with "
                                    + "\"/realisticflags-removeflag <flagname>\"!", key, joined)));
                    return true;
                }

                removed.add(key);
            }
        }

        for (String key : removed) {
            plugin.getColorMaps().remove(key);
        }

        String joined = StringUtils.join(removed, ", ");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                String.format("§7Removed %d color maps:\n§7§o%s", removed.size(), joined)));
        return true;
    }

    private List<String> findLinkedFlags(String key) {
        List<String> flags = new ArrayList<String>();

        for (Entry<String, Flag> entry : plugin.getFlags().entrySet()) {
            AbstractDyeColorMap colors = entry.getValue().getColors();
            if (colors instanceof DyeColorMapPluginProxy) {
                String flagKey = ((DyeColorMapPluginProxy) colors).getKey();
                if (key.equals(flagKey)) {
                    flags.add(entry.getKey());
                }
            }
        }

        return flags;
    }
}
