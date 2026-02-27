package moe.pxe.warpCommand.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import moe.pxe.warpCommand.Main;
import moe.pxe.warpCommand.Warp;
import moe.pxe.warpCommand.Warps;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class ListWarpsCommand {

    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    public static int displayBook(CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getExecutor() instanceof final Player player)) {
            ctx.getSource().getSender().sendRichMessage("<red><tr:permissions.requires.player>");
            return 0;
        }

        Collection<Component> bookPages = new ArrayList<>();
        int idx = -1;
        Component currentPage = Component.empty();
        Warp[] warps = Warps.getAllWarps();
        for (Warp warp : warps) {
            idx++;
            if (idx % 12 == 0) {
                if (idx > 0) bookPages.add(currentPage);
                currentPage = MINIMESSAGE.deserialize("<dark_aqua>Warps</dark_aqua> <gray>(<warp_count>)</gray>\n", Placeholder.unparsed("warp_count", String.valueOf(warps.length)));
            }
            currentPage = currentPage.append(MINIMESSAGE.deserialize("\n<dark_gray>•</dark_gray> <warp>", Placeholder.component("warp", warp.getComponent())));
        }
        bookPages.add(currentPage);

        player.openBook(Book.book(Component.empty(), Component.empty(), bookPages));
        player.playSound(Main.VIEW_WARPS_SOUND);
        return Command.SINGLE_SUCCESS;
    }

    public static LiteralCommandNode<CommandSourceStack> getCommand() {
        return Commands.literal("warps")
                .then(Commands.literal("reload")
                        .requires(ctx -> ctx.getSender().hasPermission("warps.reload") || ctx.getSender().isOp())
                        .executes(ctx -> {
                            Main.getInstance().reloadWarpsConfig();
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(ListWarpsCommand::displayBook)
                .build();
    }
}
