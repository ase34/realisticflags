package de.ase34.realisticflags.test;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.Test;

import de.ase34.realisticflags.DyeColorMap;
import de.ase34.realisticflags.executors.NewColorMapCommandExecutor;
import de.ase34.realisticflags.executors.NewFlagCommandExecutor;

public class CommandTest {

    @Test
    public void testVectorParser() {
        Assert.assertEquals(NewFlagCommandExecutor.parseVector("10.5,13.2,15.0"), new Vector(10.5,
                13.2, 15.0));
        Assert.assertEquals(NewFlagCommandExecutor.parseVector("32,-53,80.4"), new Vector(32.5,
                -52.5, 80.4));
    }

    @Test
    public void testLocationNormalization() {
        Location location = new Location(null, 13.4, -21.9, 31.0);

        NewFlagCommandExecutor.normalizeLocation(location);

        Assert.assertEquals(13.5, location.getX(), 0.0001);
        Assert.assertEquals(-21.5, location.getY(), 0.0001);
        Assert.assertEquals(31.5, location.getZ(), 0.0001);
    }

    @Test
    public void testColorMapParsing() throws Exception {
        InputStream istream = getClass().getResourceAsStream("/color_map_test.txt");

        DyeColorMap colorMap = NewColorMapCommandExecutor.readDyeColorMap(new InputStreamReader(
                istream));

        Assert.assertEquals(9, colorMap.getWidth());
        Assert.assertEquals(5, colorMap.getHeight());

        Assert.assertEquals(DyeColor.WHITE, colorMap.getColor(0, 0));
        Assert.assertEquals(null, colorMap.getColor(8, 1));
        Assert.assertEquals(DyeColor.PURPLE, colorMap.getColor(3, 1));
        Assert.assertEquals(DyeColor.BLUE, colorMap.getColor(5, 1));
        Assert.assertEquals(DyeColor.RED, colorMap.getColor(4, 2));
        Assert.assertEquals(DyeColor.BROWN, colorMap.getColor(3, 3));
        Assert.assertEquals(DyeColor.GREEN, colorMap.getColor(5, 3));
    }

}