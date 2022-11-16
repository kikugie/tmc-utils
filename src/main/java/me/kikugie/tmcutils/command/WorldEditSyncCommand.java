package me.kikugie.tmcutils.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import me.kikugie.tmcutils.features.WorldEditSync;
import me.kikugie.tmcutils.networking.WorldEditNetworkHandler;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class WorldEditSyncCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignoredAccess) {
        dispatcher.register(literal("wesync")
                .requires(source -> WorldEditNetworkHandler.isWorldEditConnected())
                .requires(source -> source.getPlayer().hasPermissionLevel(2))
                .executes(WorldEditSyncCommand::syncWorldEditToLitematica));
    }

    private static int syncWorldEditToLitematica(CommandContext<FabricClientCommandSource> context) {
        var box = WorldEditSync.getValidActiveSelection();
        if (box == null) {
            context.getSource().sendError(Text.of("Invalid selection!"));
            return 0;
        }
        WorldEditSync.updateRegion(box);
        context.getSource().sendFeedback(Text.of("WorldEdit synced!"));
        return 0;
    }
}
