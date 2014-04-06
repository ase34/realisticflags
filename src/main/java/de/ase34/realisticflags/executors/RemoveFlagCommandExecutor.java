package de.ase34.realisticflags.executors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.ase34.realisticflags.Flag;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class RemoveFlagCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public RemoveFlagCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        List<String> removed = new ArrayList<String>();

        String regex = args[0];

        Iterator<Entry<String, Flag>> it = plugin.getFlags().entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Flag> entry = it.next();
            if (entry.getKey().matches(regex)) {
                entry.getValue().remove();

                removed.add(entry.getKey());
                it.remove();
            }
        }

        String joined = StringUtils.join(removed, ", ");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                String.format("§7Removed %d flags:\n§7§o%s", removed.size(), joined)));
        return true;
    }
}
