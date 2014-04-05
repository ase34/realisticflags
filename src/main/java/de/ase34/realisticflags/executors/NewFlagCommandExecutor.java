package de.ase34.realisticflags.executors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.ase34.realisticflags.DyeColorMapPluginProxy;
import de.ase34.realisticflags.Flag;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class NewFlagCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public NewFlagCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cOnly available for players!"));
            return true;
        }

        if (args.length < 2) {
            return false;
        }

        Player player = (Player) sender;

        String flagName = args[0];
        if (plugin.getFlags().containsKey(flagName)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                    "§7A flag with the name §o%s§r§7 already exists! Use "
                            + "\"/realistiflags-removeflag %1$s\" first.", flagName)));
            return true;
        }

        String colorMapKey = args[1];
        DyeColorMapPluginProxy colorMap = new DyeColorMapPluginProxy(plugin, colorMapKey);

        if (colorMap.getDelegate() == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                    "§cA color map with the key §o%s§r§c does not exist!", colorMapKey)));
            return true;
        }

        Location location;
        if (args.length > 2) {
            Vector vector = parseVector(args[2]);
            if (vector == null) {

                return true;
            }

            location = new Location(player.getWorld(), vector.getX(), vector.getY(), vector.getZ());
        } else {
            location = player.getLocation().clone();
            normalizeLocation(location);
        }

        Vector direction;
        if (args.length > 3) {
            double angle = (Double.parseDouble(args[3]) + 90) * (Math.PI / 180);
            direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
        } else {
            double angle = Math.round(((player.getLocation().getYaw() + 90) % 360) / 90) * 90
                    * (Math.PI / 180);
            direction = new Vector(Math.cos(angle), 0, Math.sin(angle));
        }

        Flag flag = new Flag(colorMap, location, direction);

        plugin.getFlags().put(flagName, flag);
        flag.spawn();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                String.format("§7A new flag was created with name §o%s§r§7!", flagName)));
        return true;
    }

    public static Vector parseVector(String string) {
        String[] vars = string.split(",");

        if (vars.length != 3) {
            return null;
        }

        double x, y, z;

        String intRegex = "^-?\\d+$";
        String doubleRegex = "^-?\\d+.\\d+$";

        if (vars[0].matches(intRegex)) {
            x = Integer.parseInt(vars[0]) + 0.5;
        } else if (vars[0].matches(doubleRegex)) {
            x = Double.parseDouble(vars[0]);
        } else {
            return null;
        }

        if (vars[1].matches(intRegex)) {
            y = Integer.parseInt(vars[1]) + 0.5;
        } else if (vars[1].matches(doubleRegex)) {
            y = Double.parseDouble(vars[1]);
        } else {
            return null;
        }

        if (vars[2].matches(intRegex)) {
            z = Integer.parseInt(vars[2]) + 0.5;
        } else if (vars[2].matches(doubleRegex)) {
            z = Double.parseDouble(vars[2]);
        } else {
            return null;
        }

        return new Vector(x, y, z);
    }

    public static void normalizeLocation(Location location) {
        Vector vector = location.toVector();
        Vector newVector = new Vector(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ())
                .add(new Vector(0.5, 0.5, 0.5));

        location.setX(newVector.getX());
        location.setY(newVector.getY());
        location.setZ(newVector.getZ());
    }
}
