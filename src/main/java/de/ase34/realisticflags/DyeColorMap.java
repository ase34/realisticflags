package de.ase34.realisticflags;

import java.util.Arrays;

import org.bukkit.DyeColor;

public class DyeColorMap extends AbstractDyeColorMap {

    private int width;
    private int height;

    private Byte[] colors;

    public DyeColorMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.colors = new Byte[width * height];
    }

    public DyeColorMap(int width, int height, Byte[] rawColors) {
        this.width = width;
        this.height = height;
        this.colors = Arrays.copyOf(rawColors, width * height);
    }

    public DyeColorMap(int width, int height, DyeColor color) {
        this.width = width;
        this.height = height;
        this.colors = new Byte[width * height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setColor(x, y, color);
            }
        }
    }

    public DyeColorMap(int width, int height, DyeColor[] colors) {
        this.width = width;
        this.height = height;
        setColors(colors);
    }

    @SuppressWarnings("deprecation")
    @Override
    public DyeColor[] getColors() {
        DyeColor[] colors = new DyeColor[this.colors.length];

        for (int i = 0; i < this.colors.length; i++) {
            if (this.colors[i] == null) {
                colors[i] = null;
            } else {
                colors[i] = DyeColor.getByWoolData(this.colors[i]);
            }
        }

        return colors;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setColors(DyeColor[] colors) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == null) {
                this.colors[i] = null;
            } else {
                this.colors[i] = colors[i].getWoolData();
            }
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
