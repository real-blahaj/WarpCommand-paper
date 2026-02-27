package moe.pxe.warpCommand.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import moe.pxe.warpCommand.Warp;
import moe.pxe.warpCommand.Warps;
import moe.pxe.warpCommand.command.argument.WarpArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class SetWarpCommand {

    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    public static LiteralCommandNode<CommandSourceStack> getCommand() {


        return Commands.literal("setwarp")
                .requires(ctx -> ctx.getSender().hasPermission("warps.set") || ctx.getSender().isOp())
                .then(Commands.argument("warp", new WarpArgument())
                        .then(Commands.literal("displayname")
                                .requires(ctx -> ctx.getSender().hasPermission("warps.set.displayname") || ctx.getSender().isOp())
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);
                                            Component name = MINIMESSAGE.deserialize(ctx.getArgument("name", String.class));
                                            warp.setDisplayName(name);
                                            ctx.getSource().getSender().sendRichMessage("Set display name of warp to <name>", Placeholder.component("name", name));

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);
                                    warp.setDisplayName(null);
                                    ctx.getSource().getSender().sendRichMessage("Removed display name from <warp>", Placeholder.component("warp", warp.getComponent()));

                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("description")
                                .requires(ctx -> ctx.getSender().hasPermission("warps.set.description") || ctx.getSender().isOp())
                                .then(Commands.argument("description", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);
                                            Component description = MINIMESSAGE.deserialize(ctx.getArgument("description", String.class));
                                            warp.setDescription(description);
                                            ctx.getSource().getSender().sendRichMessage("Set description of warp to <description>", Placeholder.component("description", description));

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);
                                    warp.setDescription(null);
                                    ctx.getSource().getSender().sendRichMessage("Removed description from <warp>", Placeholder.component("warp", warp.getComponent()));

                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("permission")
                                .requires(ctx -> ctx.getSender().hasPermission("warps.set.permission") || ctx.getSender().isOp())
                                .then(Commands.argument("permission", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);
                                            String permission = ctx.getArgument("permission", String.class);
                                            warp.setPermission(permission);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);
                                    warp.setPermission(null);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("location")
                                .then(Commands.argument("location", ArgumentTypes.finePosition(true))
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);

                                            final FinePositionResolver resolver = ctx.getArgument("location", FinePositionResolver.class);
                                            final FinePosition finePosition = resolver.resolve(ctx.getSource());

                                            warp.setLocation(finePosition.toLocation(ctx.getSource().getLocation().getWorld()));
                                            ctx.getSource().getSender().sendRichMessage("Set location of <warp> to <location>",
                                                    Placeholder.component("warp", warp.getComponent()),
                                                    Placeholder.unparsed("location", warp.getLocation().x()+" "+warp.getLocation().y()+" "+warp.getLocation().z())
                                            );

                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);

                                    warp.setLocation(ctx.getSource().getLocation());
                                    ctx.getSource().getSender().sendRichMessage("Set location of <warp> to your location", Placeholder.component("warp", warp.getComponent()));

                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(ctx -> {

                            Warp warp = ctx.getArgument("warp", Warp.class);
                            warp.setLocation(ctx.getSource().getLocation());
                            ctx.getSource().getSender().sendRichMessage("Set location of <warp> to your location", Placeholder.component("warp", warp.getComponent()));

                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(ctx -> {
                            Warp warp = Warps.newWarp(ctx.getArgument("name", String.class), ctx.getSource().getLocation());
                            ctx.getSource().getSender().sendRichMessage("Created new warp <warp>", Placeholder.component("warp", warp.getComponent()));

                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}
