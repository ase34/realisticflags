package de.ase34.realisticflags.executors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;

import de.ase34.realisticflags.DyeColorMap;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class NewColorMapCommandExecutor implements CommandExecutor {

    private RealisticFlagsPlugin plugin;

    public NewColorMapCommandExecutor(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        String type = args[0];
        String[] newargs = Arrays.copyOfRange(args, 1, args.length);

        if (type.equalsIgnoreCase("raw")) {
            return processRaw(sender, newargs);
        } else if (type.equalsIgnoreCase("worldedit")) {
            return processWorldEdit(sender, newargs);
        } else {
            return false;
        }

    }

    @SuppressWarnings("deprecation")
    private boolean processWorldEdit(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }

        String key = args[0];
        if (plugin.getColorMaps().containsKey(key)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                    "§7A color map with key §o%s§r§7 already exists! Use "
                            + "\"/realistiflags-removecolormap %1$s\" first.", key)));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cOnly available as a player!"));
            return true;
        }
        Player player = (Player) sender;

        if (plugin.getWorldEdit() == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cWorldEdit doesn't seems to be enabled!"));
            return true;
        }

        Selection selection = plugin.getWorldEdit().getSelection(player);
        if (selection == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cYou did not created a selection yet!"));
            return true;
        }

        if (!(selection instanceof CuboidSelection)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cYour selection is not a cuboid selection!"));
            return true;
        }

        CuboidSelection cuboidSelection = (CuboidSelection) selection;
        if (cuboidSelection.getWidth() != 1 && cuboidSelection.getLength() != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cYour selection must be 1 block thick and must stand vertical!"));
            return true;
        }

        World world = selection.getWorld();
        CuboidRegion region;
        try {
            region = (CuboidRegion) cuboidSelection.getRegionSelector().getRegion();
        } catch (IncompleteRegionException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cYou did not created a selection yet!"));
            return true;
        }

        if (region.getPos1().getBlockY() < region.getPos2().getBlockY()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    "§cThe first point of the selection must be not lower that the second one!"));
            return true;
        }

        Vector point1 = region.getPos1();
        Vector point2 = region.getPos2();

        Vector delta = point2.subtract(point1);

        Vector lengthVector = delta.multiply(1, 0, 1);
        Vector heightVector = delta.multiply(0, 1, 0);

        DyeColorMap colorMap = new DyeColorMap((int) lengthVector.length() + 1,
                (int) heightVector.length() + 1);

        System.out.println(lengthVector.toString() + heightVector.toString());

        Iterator<BlockVector> it = region.iterator();
        while (it.hasNext()) {
            BlockVector vector = it.next();

            int x = (int) point1.subtract(vector).multiply(lengthVector.normalize()).length();
            int y = (int) point1.subtract(vector).multiply(heightVector.normalize()).length();

            Block block = world.getBlockAt(vector.getBlockX(), vector.getBlockY(),
                    vector.getBlockZ());
            DyeColor color = block.getType() == Material.WOOL ? DyeColor.getByWoolData(block
                    .getData()) : null;

            colorMap.setColor(x, y, color);
        }

        plugin.getColorMaps().put(key, colorMap);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                "§7A new color map from the WorldEdit selection was saved with key §o%s§r§7!", key)));
        return true;
    }

    private boolean processRaw(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }

        String key = args[0];
        if (plugin.getColorMaps().containsKey(key)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                    "§7A color map with key §o%s§r§7 already exists! Use "
                            + "\"/realistiflags-removecolormap %1$s\" first.", key)));
            return true;
        }

        URL url;
        try {
            url = new URL(args[1]);
        } catch (MalformedURLException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§',
                    String.format("§cYour URL §o%s§r§c seems to be malformed!", args[1])));
            return true;
        }

        DyeColorMap colorMap;
        try {
            colorMap = readDyeColorMap(new InputStreamReader(url.openStream()));
        } catch (FileNotFoundException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                    "§cThe file at the specified URL §o%s§r§c could not be found!", args[1])));

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        plugin.getColorMaps().put(key, colorMap);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('§', String.format(
                "§7A new color map from §o%s§r§7 was saved with key §o%s§r§7!", args[1], key)));
        return true;
    }

    @SuppressWarnings("deprecation")
    public static DyeColorMap readDyeColorMap(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        int width = 0;
        ArrayList<String> lines = new ArrayList<String>();

        String line = bufferedReader.readLine();
        while (line != null) {
            width = Math.max(line.length(), width);
            lines.add(line);
            line = bufferedReader.readLine();
        }

        DyeColorMap colorMap = new DyeColorMap(width, lines.size());

        for (int i = 0; i < lines.size(); i++) {
            line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                byte code;
                try {
                    code = Byte.parseByte(line.substring(j, j + 1), 16);
                } catch (NumberFormatException e) {
                    colorMap.setColor(i, j, null);
                    continue;
                }
                colorMap.setColor(j, i, DyeColor.getByWoolData(code));
            }
        }

        return colorMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] rotateCounterClockWise(T[] array, int width, int height) {
        ArrayList<T> newArray = new ArrayList<T>(width * height);

        int newWidth = height;
        int newHeight = width;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newArray.set(y * newHeight + x, array[x * height + y]);
            }
        }

        width = newWidth;
        height = newHeight;
        return (T[]) newArray.toArray();
    }
}
