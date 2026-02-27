package moe.pxe.warpCommand.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import moe.pxe.warpCommand.Warp;
import moe.pxe.warpCommand.Warps;
import moe.pxe.warpCommand.command.argument.WarpArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class DeleteWarpCommand {

    public static LiteralCommandNode<CommandSourceStack> getCommand() {
        return Commands.literal("delwarp")
                .requires(ctx -> ctx.getSender().hasPermission("warps.delete") || ctx.getSender().isOp())
                .then(Commands.argument("warp", new WarpArgument())
                        .executes(ctx -> {
                            Warp warp = ctx.getArgument("warp", Warp.class);
                            Warps.deleteWarp(warp.getName());
                            ctx.getSource().getSender().sendRichMessage("Deleted warp <name>", Placeholder.unparsed("name", warp.getName()));

                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }

}
