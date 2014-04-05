package de.ase34.realisticflags.gson;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

import de.ase34.realisticflags.DyeColorMapPluginProxy;
import de.ase34.realisticflags.RealisticFlagsPlugin;

public class DyeColorMapPluginProxyCreator implements InstanceCreator<DyeColorMapPluginProxy> {

    private RealisticFlagsPlugin plugin;

    public DyeColorMapPluginProxyCreator(RealisticFlagsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public DyeColorMapPluginProxy createInstance(Type type) {
        return new DyeColorMapPluginProxy(plugin, null);
    }

}
