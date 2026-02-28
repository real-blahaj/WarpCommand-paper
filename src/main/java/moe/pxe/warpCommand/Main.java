package moe.pxe.warpCommand;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import moe.pxe.warpCommand.command.DeleteWarpCommand;
import moe.pxe.warpCommand.command.ListWarpsCommand;
import moe.pxe.warpCommand.command.SetWarpCommand;
import moe.pxe.warpCommand.command.WarpCommand;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;

    public static final Sound TELEPORT_SOUND = Sound.sound(Key.key("entity.enderman.teleport"), Sound.Source.MASTER, 1f, 1f);
    public static final Sound VIEW_WARPS_SOUND = Sound.sound(Key.key("block.chiseled_bookshelf.pickup.enchanted"), Sound.Source.MASTER, 1f, 1f);
    public static final Sound MODIFY_SOUND = Sound.sound(Key.key("entity.item_frame.place"), Sound.Source.MASTER, 0.75f, 1.25f);
    public static final Sound REMOVE_SOUND = Sound.sound(Key.key("entity.item_frame.remove_item"), Sound.Source.MASTER, 0.75f, 0.793701f);
    public static final Sound DELETE_SOUND = Sound.sound(Key.key("block.fire.extinguish"), Sound.Source.MASTER, 0.75f, 2f);

    private FileConfiguration warpsConfig;
    private final File warpsConfigFile = new File(getDataFolder(), "warps.yml");

    @Override
    public void onEnable() {
        INSTANCE = this;
        createWarpsConfig();
        reloadWarpsConfig();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(WarpCommand.getCommand());
            commands.registrar().register(ListWarpsCommand.getCommand());
            commands.registrar().register(SetWarpCommand.getCommand());
            commands.registrar().register(DeleteWarpCommand.getCommand());
        });
    }

    @Override
    public void onDisable() {
        saveWarpConfig();
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public FileConfiguration getWarpsConfig() {
        return this.warpsConfig;
    }

    public void reloadWarpsConfig() {
        if (warpsConfig == null) warpsConfig = new YamlConfiguration();
        try {
            warpsConfig.load(warpsConfigFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Warps.loadFromConfig(warpsConfig);
    }

    public void saveWarpConfig() {
        Warps.flushToConfig(warpsConfig);
        try {
            warpsConfig.save(warpsConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWarpsConfig() {
        if (!warpsConfigFile.exists()) {
            warpsConfigFile.getParentFile().mkdirs();
            saveResource("warps.yml", false);
        }
        reloadWarpsConfig();
    }
}
