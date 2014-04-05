package de.ase34.realisticflags.entity;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import de.ase34.flyingblocksapi.FlyingBlock;
import de.ase34.realisticflags.Flag;

public class FlagPartFlyingBlock extends FlyingBlock {

    private final Flag flag;
    private final Location initialLocation;
    private final Vector direction;
    private final double x;
    private final double y;
    private final int velocityUpdateDelay;

    private long counter;

    @SuppressWarnings("deprecation")
    public FlagPartFlyingBlock(Flag flag, Location initialLocation, DyeColor color,
            Vector direction, double x, double y, int velocityUpdateDelay) {
        super(Material.WOOL, color.getData(), flag.getUpdateInterval());
        this.flag = flag;
        this.initialLocation = initialLocation;
        this.direction = direction;
        this.velocityUpdateDelay = velocityUpdateDelay;

        this.x = x;
        this.y = y;
    }

    @Override
    public void onTick() {
        counter++;
        if (counter % velocityUpdateDelay != 0) {
            return;
        }

        // ignores y
        double result = Math.sin(x * flag.getWavelength() - (counter + velocityUpdateDelay)
                * flag.getFrequency())
                * flag.getSlope() * x;

        Vector oldVector = getBukkitEntity().getLocation().toVector();
        Vector nextVector = initialLocation.clone().toVector()
                .add(new Vector(0, getHeightOffset(), 0));
        nextVector.add(direction.clone().multiply(result));

        setVelocity((nextVector.clone().subtract(oldVector)).multiply(1D / velocityUpdateDelay));
    }
}
