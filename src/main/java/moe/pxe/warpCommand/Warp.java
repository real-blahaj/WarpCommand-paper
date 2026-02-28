package moe.pxe.warpCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warp {
    private final String name;
    private Component displayName;
    private Component description;
    private Location location;
    private String permission;

    protected Warp(String name, Location location, Component displayName, Component description, String permission) {
        this.name = name;
        this.location = location;
        this.displayName = displayName;
        this.description = description;
        this.permission = permission;
    }

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Component getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public String getPermission() { return permission; }

    public void setDisplayName(Component displayName) {
        this.displayName = displayName;
    }

    public void setDescription(Component description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPermission(String permission) { this.permission = permission; }

    public Component getComponent() {
        Component hover = displayName != null ? displayName.append(Component.text(" ("+name+")", TextColor.color(0x808080))) : Component.text(name);
        if (description != null) hover = hover.appendNewline().append(description.color(NamedTextColor.GRAY));
        hover = hover.append(Component.text("\nClick to teleport →").color(NamedTextColor.DARK_GRAY));

        return (displayName != null ? displayName : Component.text(name)).hoverEvent(hover).clickEvent(ClickEvent.runCommand("/warp "+name));
    }

    public boolean hasPermission(CommandSender player) {
        return permission == null || player.isOp() || player.hasPermission(permission);
    }

    public void teleportPlayer(Player player) { teleportPlayer(player, false); }

    public void teleportPlayer(Player player, boolean bypassPermission) {
        if (!bypassPermission && permission != null && !player.hasPermission(permission)) {
            player.sendRichMessage("<red>You do not have permission to teleport to this warp.");
            return;
        }
        player.teleport(this.location);
        player.playSound(Main.TELEPORT_SOUND);
        player.sendRichMessage("Teleported you to <aqua><name>", Placeholder.component("name", getComponent()));
    }
}
