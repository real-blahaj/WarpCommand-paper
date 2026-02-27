package moe.pxe.warpCommand.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import moe.pxe.warpCommand.Warp;
import moe.pxe.warpCommand.Warps;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class WarpArgument implements CustomArgumentType.Converted<Warp, String> {

    private static final SimpleCommandExceptionType ERROR_BAD_SOURCE = new SimpleCommandExceptionType(MessageComponentSerializer.message().serialize(Component.text("The source needs to be a CommandSourceStack")));

    private static final DynamicCommandExceptionType ERROR_NOT_FOUND = new DynamicCommandExceptionType(name ->
            MessageComponentSerializer.message().serialize(Component.text(name + " is not a warp."))
    );

    @Override
    public Warp convert(String nativeType) throws CommandSyntaxException {
        final Warp warp = Warps.getWarp(nativeType);
        if (warp == null) throw ERROR_NOT_FOUND.create(nativeType);
        return warp;
    }

    @Override
    public <S> Warp convert(String nativeType, S source) throws CommandSyntaxException {
        if (!(source instanceof CommandSourceStack stack)) {
            throw ERROR_BAD_SOURCE.create();
        }
        if (!stack.getSender().hasPermission("warps.warp."+nativeType) && !stack.getSender().hasPermission("warps.warp") && !stack.getSender().isOp()) {
            throw ERROR_NOT_FOUND.create(nativeType);
        }
        return convert(nativeType);
    }

    @Override
    public @NonNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(@NonNull CommandContext<S> context, @NonNull SuggestionsBuilder builder) {
        Arrays.stream(Warps.getAllWarps())
                .map(Warp::getName)
                .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
