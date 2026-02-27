package moe.pxe.warpCommand.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import moe.pxe.warpCommand.Warp;
import moe.pxe.warpCommand.command.argument.WarpArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCommand {

    public static LiteralCommandNode<CommandSourceStack> getCommand() {
        return Commands.literal("warp")
                .requires(ctx -> ctx.getSender().hasPermission("warps.warp") || ctx.getSender().isOp())
                .then(Commands.argument("warp", new WarpArgument())
                        .then(Commands.argument("players", ArgumentTypes.players())
                                .requires(ctx -> ctx.getSender().hasPermission("warps.warp.others") || ctx.getSender().isOp())
                                .executes(ctx -> {
                                    final Warp warp = ctx.getArgument("warp", Warp.class);

                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("players", PlayerSelectorArgumentResolver.class);
                                    final List<Player> players = targetResolver.resolve(ctx.getSource());

                                    for (final Player player : players) {
                                        warp.teleportPlayer(player);
                                    }

                                    ctx.getSource().getSender().sendRichMessage("Teleported <players> to <aqua><warp>",
                                            Placeholder.component("players", Component.text(String.join(", ", players.stream().map(Player::getName).toList()))),
                                            Placeholder.component("warp", warp.getComponent())
                                    );

                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(ctx -> {
                            if (!(ctx.getSource().getExecutor() instanceof final Player player)) {
                                ctx.getSource().getSender().sendRichMessage("<red><tr:permissions.requires.player>");
                                return 0;
                            }

                            final Warp warp = ctx.getArgument("warp", Warp.class);
                            warp.teleportPlayer(player);
                            return Command.SINGLE_SUCCESS;
                        }))
                .executes(ListWarpsCommand::displayBook)
                .build();
    }
}
