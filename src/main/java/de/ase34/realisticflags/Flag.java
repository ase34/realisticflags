package de.ase34.realisticflags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.ase34.flyingblocksapi.FlyingBlock;
import de.ase34.realisticflags.entity.FlagPartFlyingBlock;

public class Flag {

    public static final int UPDATE_INTERVAL = 4;
    public static final int VELOCITY_UPDATE_DELAY = 2;
    public static final double FREQUENCY = 40 / Math.PI;
    public static final double SLOPE = 0.1;
    public static final double WAVELENGTH = 0.3;

    private final AbstractDyeColorMap colors;
    private final Location spawnpoint;

    private final Vector heightDirection;
    private final Vector widthDirection;
    private final Vector depthDirection;

    private final int updateInterval;

    private double frequency;
    private double slope;
    private double wavelength;
    private int velocityUpdateDelay;

    private transient List<FlyingBlock> flyingBlocks;

    public Flag(AbstractDyeColorMap colors, Location spawnpoint, Vector heightDirection,
            Vector widthDirection, Vector depthDirection, int updateInterval, double frequency,
            double slope, double wavelength, int velocityUpdateDelay, List<FlyingBlock> flyingBlocks) {
        this.colors = colors;
        this.spawnpoint = spawnpoint;
        this.heightDirection = heightDirection;
        this.widthDirection = widthDirection;
        this.depthDirection = depthDirection;
        this.updateInterval = updateInterval;
        this.frequency = frequency;
        this.slope = slope;
        this.wavelength = wavelength;
        this.velocityUpdateDelay = velocityUpdateDelay;
        this.flyingBlocks = flyingBlocks;
    }

    public Flag(AbstractDyeColorMap colors, Location spawnpoint, Vector widthDirection) {
        this.colors = colors;
        this.spawnpoint = spawnpoint;
        this.widthDirection = widthDirection;
        this.heightDirection = new Vector(0, 1, 0);
        this.depthDirection = widthDirection.clone().crossProduct(heightDirection);
        this.updateInterval = UPDATE_INTERVAL;
        this.frequency = FREQUENCY;
        this.slope = SLOPE;
        this.wavelength = WAVELENGTH;
        this.velocityUpdateDelay = VELOCITY_UPDATE_DELAY;
        this.flyingBlocks = new ArrayList<FlyingBlock>();
    }

    private Location getFlyingBlockLocation(int x, int y) {
        Location blockLocation = spawnpoint.clone();
        blockLocation.add(widthDirection.clone().multiply(x));
        blockLocation.add(heightDirection.clone().multiply(y));

        return blockLocation;
    }

    public void spawn() {
        this.remove(); // safety first

        for (int x = 0; x < colors.getWidth(); x++) {
            for (int y = 0; y < colors.getHeight(); y++) {
                Location blockLocation = getFlyingBlockLocation(x, colors.getHeight() - 1 - y);

                DyeColor color = colors.getColor(x, y);
                if (color == null) {
                    continue;
                }

                FlyingBlock block = new FlagPartFlyingBlock(this, blockLocation, color,
                        depthDirection.clone(), x, y, velocityUpdateDelay);
                block.spawn(spawnpoint); // they will locate themselves
                flyingBlocks.add(block);
            }
        }
    }

    public void remove() {
        if (flyingBlocks == null) {
            flyingBlocks = new ArrayList<FlyingBlock>();
        }

        Iterator<FlyingBlock> it = flyingBlocks.iterator();
        while (it.hasNext()) {
            FlyingBlock next = it.next();

            if (next != null && !next.getBukkitEntity().isDead()) {
                next.remove();
            }
            it.remove();
        }
    }

    public AbstractDyeColorMap getColors() {
        return colors;
    }

    public Location getSpawnpoint() {
        return spawnpoint;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getSlope() {
        return slope;
    }

    public double getWavelength() {
        return wavelength;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public void setWavelength(double wavelength) {
        this.wavelength = wavelength;
    }
}
