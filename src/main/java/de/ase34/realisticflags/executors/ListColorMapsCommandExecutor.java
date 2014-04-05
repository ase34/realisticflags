package de.ase34.realisticflags.executors;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import de.ase34.realisticflags.DyeColorMap;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class ListColorMapsCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public ListColorMapsCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && !args[0].matches("\\d+")) {
            return false;
        }

        int pageNumber = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        String keyRegex = args.length > 1 ? args[1] : ".*";

        StringBuilder result = new StringBuilder();
        for (Entry<String, DyeColorMap> entry : plugin.getColorMaps().entrySet()) {
            if (entry.getKey().matches(keyRegex)) {
                result.append(buildLine(entry) + "\n");
            }
        }

        ChatPage page = ChatPaginator.paginate(result.toString(), pageNumber,
                ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH,
                ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1);

        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '§',
                String.format("§ePage §f%d§e of §f%d§e: §fName - Size", page.getPageNumber(),
                        page.getTotalPages())));
        for (String line : page.getLines()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', line));
        }
        return true;
    }

    private Object buildLine(Entry<String, DyeColorMap> entry) {
        return String.format("§7§o%s§r§7 - %d x %d", entry.getKey(), entry.getValue().getHeight(),
                entry.getValue().getWidth());
    }
}
