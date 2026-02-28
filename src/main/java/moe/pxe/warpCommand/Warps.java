package moe.pxe.warpCommand;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Warps {
    private static final HashMap<String, Warp> WARPS = new HashMap<>();

    public static Warp getWarp(String name) {
        return WARPS.get(name);
    }

    public static Warp newWarp(String name, Location location) {
        Warp warp = new Warp(name, location);
        WARPS.put(name, warp);
        return warp;
    }

    public static void deleteWarp(String name) {
        WARPS.remove(name);
    }

    public static Warp[] getAllWarps() {
        return WARPS.values().toArray(new Warp[0]);
    }

    public static void loadFromConfig(FileConfiguration config) {
        config.getValues(true).forEach((name, obj) -> {
            if (!(obj instanceof MemorySection section)) return;
            Component displayName = section.getRichMessage("display-name");
            Component description = section.getRichMessage("description");
            Location location = section.getLocation("location");
            String permission = section.getString("permission");

            WARPS.put(name, new Warp(name, location, displayName, description, permission));
        });
    }

    public static void flushToConfig(FileConfiguration config) {
        try {
            config.loadFromString("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Warp> warp : WARPS.entrySet()) {
            config.setRichMessage(warp.getKey()+".display-name", warp.getValue().getDisplayName());
            config.setRichMessage(warp.getKey()+".description", warp.getValue().getDescription());
            config.set(warp.getKey()+".location", warp.getValue().getLocation());
            config.set(warp.getKey()+".permission", warp.getValue().getPermission());
        }
    }
}
