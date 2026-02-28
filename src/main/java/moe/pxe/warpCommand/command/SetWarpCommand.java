package moe.pxe.warpCommand.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import moe.pxe.warpCommand.Main;
import moe.pxe.warpCommand.Warp;
import moe.pxe.warpCommand.Warps;
import moe.pxe.warpCommand.command.argument.WarpArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.Locale;

public class SetWarpCommand {

    private static final MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

    public static LiteralCommandNode<CommandSourceStack> getCommand() {
        Runnable saveConfig = Main.getInstance()::saveWarpConfig;
        return Commands.literal("setwarp")
                .requires(ctx -> ctx.getSender().hasPermission("warps.set") || ctx.getSender().isOp())
                .then(Commands.argument("warp", new WarpArgument())
                        .then(Commands.literal("displayname")
                                .requires(ctx -> ctx.getSender().hasPermission("warps.set.displayname") || ctx.getSender().isOp())
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> {
                                            Component displayName = ctx.getArgument("warp", Warp.class).getDisplayName();
                                            if (displayName != null) builder.suggest(MINIMESSAGE.serialize(displayName));
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);
                                            Component name = MINIMESSAGE.deserialize(ctx.getArgument("name", String.class));

                                            warp.setDisplayName(name);
                                            saveConfig.run();
                                            ctx.getSource().getSender().sendRichMessage("Set display name of warp to <name>", Placeholder.component("name", name));
                                            ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);

                                    warp.setDisplayName(null);
                                    saveConfig.run();
                                    ctx.getSource().getSender().sendRichMessage("Removed display name from <warp>", Placeholder.component("warp", warp.getComponent()));
                                    ctx.getSource().getSender().playSound(Main.REMOVE_SOUND);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("description")
                                .requires(ctx -> ctx.getSender().hasPermission("warps.set.description") || ctx.getSender().isOp())
                                .then(Commands.argument("description", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> {
                                            Component description = ctx.getArgument("warp", Warp.class).getDescription();
                                            if (description != null) builder.suggest(MINIMESSAGE.serialize(description));
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);
                                            Component description = MINIMESSAGE.deserialize(ctx.getArgument("description", String.class));

                                            warp.setDescription(description);
                                            saveConfig.run();
                                            ctx.getSource().getSender().sendRichMessage("Set description of warp to <description>", Placeholder.component("description", description));
                                            ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);

                                    warp.setDescription(null);
                                    saveConfig.run();
                                    ctx.getSource().getSender().sendRichMessage("Removed description from <warp>", Placeholder.component("warp", warp.getComponent()));
                                    ctx.getSource().getSender().playSound(Main.REMOVE_SOUND);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("permission")
                                .requires(ctx -> ctx.getSender().hasPermission("warps.set.permission") || ctx.getSender().isOp())
                                .then(Commands.argument("permission", StringArgumentType.greedyString())
                                        .suggests((ctx, builder) -> {
                                            String permission = ctx.getArgument("warp", Warp.class).getPermission();
                                            if (permission != null) builder.suggest(permission);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);
                                            String permission = ctx.getArgument("permission", String.class);

                                            warp.setPermission(permission);
                                            saveConfig.run();
                                            ctx.getSource().getSender().sendRichMessage("Set required permission for warp to <permission>",
                                                    Placeholder.unparsed("permission", permission));
                                            ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);

                                    warp.setPermission(null);
                                    saveConfig.run();
                                    ctx.getSource().getSender().sendRichMessage("Removed required permission from <warp>", Placeholder.component("warp", warp.getComponent()));
                                    ctx.getSource().getSender().playSound(Main.REMOVE_SOUND);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("location")
                                .then(Commands.argument("location", ArgumentTypes.finePosition(true))
                                        .executes(ctx -> {
                                            Warp warp = ctx.getArgument("warp", Warp.class);

                                            final FinePositionResolver resolver = ctx.getArgument("location", FinePositionResolver.class);
                                            final FinePosition finePosition = resolver.resolve(ctx.getSource());

                                            warp.setLocation(finePosition.toLocation(ctx.getSource().getLocation().getWorld()));
                                            saveConfig.run();
                                            ctx.getSource().getSender().sendRichMessage("Set location of <warp> to <location>",
                                                    Placeholder.component("warp", warp.getComponent()),
                                                    Placeholder.unparsed("location", warp.getLocation().x()+" "+warp.getLocation().y()+" "+warp.getLocation().z())
                                            );
                                            ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                                            return Command.SINGLE_SUCCESS;
                                        }))
                                .executes(ctx -> {
                                    Warp warp = ctx.getArgument("warp", Warp.class);

                                    warp.setLocation(ctx.getSource().getLocation());
                                    saveConfig.run();
                                    ctx.getSource().getSender().sendRichMessage("Set location of <warp> to your location", Placeholder.component("warp", warp.getComponent()));
                                    ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(ctx -> {
                            Warp warp = ctx.getArgument("warp", Warp.class);

                            warp.setLocation(ctx.getSource().getLocation());
                            saveConfig.run();
                            ctx.getSource().getSender().sendRichMessage("Set location of <warp> to your location", Placeholder.component("warp", warp.getComponent()));
                            ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.argument("name", StringArgumentType.word())
                        .requires(ctx -> ctx.getSender().hasPermission("warp.create") || ctx.getSender().isOp())
                        .executes(ctx -> {
                            String name = ctx.getArgument("name", String.class);
                            if (Warps.getWarp(name) != null) {
                                ctx.getSource().getSender().sendRichMessage("<red>No permission to edit warp <name>", Placeholder.unparsed("name", name));
                                return 0;
                            }

                            Warp warp = Warps.newWarp(name.toLowerCase(Locale.ROOT), ctx.getSource().getLocation());
                            saveConfig.run();
                            ctx.getSource().getSender().sendRichMessage("Created new warp <warp>", Placeholder.component("warp", warp.getComponent()));
                            ctx.getSource().getSender().playSound(Main.MODIFY_SOUND);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();
    }
}
