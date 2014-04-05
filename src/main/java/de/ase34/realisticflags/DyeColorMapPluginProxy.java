package de.ase34.realisticflags;

import org.bukkit.DyeColor;

public class DyeColorMapPluginProxy extends AbstractDyeColorMap {

    private transient RealisticFlagsPlugin plugin;
    private String key;

    public DyeColorMapPluginProxy(RealisticFlagsPlugin plugin, String key) {
        this.plugin = plugin;
        this.key = key;
    }

    public AbstractDyeColorMap getDelegate() {
        return plugin.getColorMaps().get(key);
    }

    @Override
    public DyeColor[] getColors() {
        return getDelegate().getColors();
    }

    @Override
    public void setColors(DyeColor[] colors) {
        getDelegate().setColors(colors);
    }

    @Override
    public int getWidth() {
        return getDelegate().getWidth();
    }

    @Override
    public int getHeight() {
        return getDelegate().getHeight();
    }

    public String getKey() {
        return key;
    }

}
