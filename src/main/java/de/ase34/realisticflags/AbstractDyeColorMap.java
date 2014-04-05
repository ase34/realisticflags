package de.ase34.realisticflags;

import org.bukkit.DyeColor;

public abstract class AbstractDyeColorMap {

    public abstract DyeColor[] getColors();

    public abstract void setColors(DyeColor[] colors);

    public abstract int getWidth();

    public abstract int getHeight();

    public DyeColor getColor(int x, int y) {
        int width = getWidth();
        DyeColor[] colors = getColors();

        if (x >= width || y >= getHeight()) {
            throw new IllegalArgumentException();
        }

        int index = y * width + x;
        if (index >= colors.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return colors[index];
    }

    public void setColor(int x, int y, DyeColor color) {
        int width = getWidth();
        DyeColor[] colors = getColors();

        int index = y * width + x;
        if (index >= colors.length) {
            throw new IndexOutOfBoundsException();
        }

        colors[index] = color;
        setColors(colors);
    }

}
