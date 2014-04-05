package de.ase34.realisticflags.executors;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import de.ase34.realisticflags.DyeColorMapPluginProxy;
import de.ase34.realisticflags.Flag;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class ListFlagsCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public ListFlagsCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && !args[0].matches("\\d+")) {
            return false;
        }

        int pageNumber = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        double maxDistance = args.length > 1 ? Double.parseDouble(args[1]) : Double.MAX_VALUE;
        String keyRegex = args.length > 2 ? args[2] : ".*";

        Location location = sender instanceof Player ? ((Player) sender).getLocation() : null;

        StringBuilder result = new StringBuilder();
        for (Entry<String, Flag> entry : plugin.getFlags().entrySet()) {
            if (location != null) {
                double disanceSquared = entry.getValue().getSpawnpoint().distanceSquared(location);
                if (disanceSquared > Math.pow(maxDistance, 2)) {
                    continue;
                }
            }
            if (entry.getKey().matches(keyRegex)) {
                result.append(buildLine(entry, location) + "\n");
            }
        }

        ChatPage page = ChatPaginator.paginate(result.toString(), pageNumber,
                ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH,
                ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                "§ePage §f%d§e of §f%d§e: §fName - Distance/World - Color Map Name",
                page.getPageNumber(), page.getTotalPages())));
        for (String line : page.getLines()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', line));
        }
        return true;
    }

    private String buildLine(Entry<String, Flag> entry, Location location) {
        Flag flag = entry.getValue();
        String colorMapName = flag.getColors() instanceof DyeColorMapPluginProxy
                ? ((DyeColorMapPluginProxy) flag.getColors()).getKey() : "[unknown]";

        if (location != null && flag.getSpawnpoint().getWorld().equals(location.getWorld())) {
            return String.format("§7§o%s§r§7 - %.2f blocks - §7§o%s§r§7", entry.getKey(), flag
                    .getSpawnpoint().distance(location), colorMapName);
        } else {
            return String.format("§7§o%s§r§7 - %s - §7§o%s§r§7", entry.getKey(), flag
                    .getSpawnpoint().getWorld().getName(), colorMapName);
        }
    }
}
