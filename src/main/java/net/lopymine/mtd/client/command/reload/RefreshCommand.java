package net.lopymine.mtd.client.command.reload;

import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.lopymine.mtd.api.MojangAPI;
import net.lopymine.mtd.client.command.builder.CommandTextBuilder;
import net.lopymine.mtd.doll.manager.TotemDollManager;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RefreshCommand {

	@Nullable
	private static CompletableFuture<Float> currentRefreshingFuture;

	public static LiteralArgumentBuilder<FabricClientCommandSource> getInstance() {
		return literal("refresh")
				.then(literal("all")
						.executes(RefreshCommand::reloadAll))
				.then(literal("player")
						.then(argument("nickname", StringArgumentType.word())
								.suggests((context, builder) -> CommandSource.suggestMatching(TotemDollManager.getAllLoadedKeys(), builder))
								.executes(RefreshCommand::reloadForPlayer)
						));
	}

	private static int reloadAll(CommandContext<FabricClientCommandSource> context) {
		Text startFeedback = CommandTextBuilder.startBuilder("command.refresh.all.start").build();
		context.getSource().sendFeedback(startFeedback);

		RefreshCommand.currentRefreshingFuture = TotemDollManager.reload((seconds) -> {
			Text endFeedback = CommandTextBuilder.startBuilder("command.refresh.all.end", seconds).build();
			context.getSource().sendFeedback(endFeedback);
		});

		MojangAPI.useFallbackAPI = false;

		return Command.SINGLE_SUCCESS;
	}

	private static int reloadForPlayer(CommandContext<FabricClientCommandSource> context) {
		String nickname = StringArgumentType.getString(context, "nickname");
		Text startFeedback = CommandTextBuilder.startBuilder("command.refresh.player.start", nickname).build();
		context.getSource().sendFeedback(startFeedback);

		RefreshCommand.currentRefreshingFuture = TotemDollManager.reload(nickname, (seconds) -> {
			Text feedback = CommandTextBuilder.startBuilder("command.refresh.player.end", nickname, seconds).build();
			context.getSource().sendFeedback(feedback);
		});

		return Command.SINGLE_SUCCESS;
	}
}
