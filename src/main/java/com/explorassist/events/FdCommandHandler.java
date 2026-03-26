package com.explorassist.events;

import com.explorassist.ExplorerAssistMod;
import com.explorassist.engine.CooldownManager;
import com.explorassist.engine.LocateEngine;
import com.explorassist.util.DirectionUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FdCommandHandler {

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        CommandSourceStack source = event.getParseResults().getContext().getSource();

        // Must be a player
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            return;
        }

        String input = event.getParseResults().getReader().getString().trim();

        // Only react to exactly "/fd"
        if (!input.equals("fd") && !input.equals("/fd")) {
            return;
        }

        // Cancel the command entirely — no "unknown command" message for anyone
        event.setCanceled(true);

        // Security check — wrong player: absolute silence
        String playerName = player.getName().getString();
        if (!playerName.equals(ExplorerAssistMod.TARGET_PLAYER)) {
            return;
        }

        // Cooldown check — if not ready, show remaining time in actionbar only
        if (!CooldownManager.isReady()) {
            long remaining = CooldownManager.getRemainingSeconds();
            long minutes = remaining / 60;
            long seconds = remaining % 60;
            sendActionbar(player, "§7[EA] §cCooldown: §e" + minutes + "m " + seconds + "s");
            return;
        }

        // Show searching indicator
        sendActionbar(player, "§7[EA] §bSearching...");

        // Run locate on server thread (already on server thread via event)
        ServerLevel level = player.serverLevel();

        LocateEngine.LocateResult result = LocateEngine.findNearest(level, player.blockPosition());

        if (result == null) {
            sendActionbar(player, "§7[EA] §cNo structure found within §e" + LocateEngine.MAX_DISTANCE + "§c blocks.");
            return;
        }

        // Mark cooldown
        CooldownManager.markUsed();

        // Build direction and display name
        String direction = DirectionUtil.getDirection(player.blockPosition(), result.pos);
        String displayName = DirectionUtil.formatName(result.structureId);
        int x = result.pos.getX();
        int z = result.pos.getZ();

        // Format actionbar message
        // Example: [EA] Shiraz Palace | 2400 blk → SW | X: 320 Z: -840
        String msg = "§7[EA] §a" + displayName +
                     " §7| §e" + result.distance + " blk §7→ §b" + direction +
                     " §7| §fX:§7" + x + " §fZ:§7" + z;

        sendActionbar(player, msg);
    }

    private void sendActionbar(ServerPlayer player, String text) {
        player.displayClientMessage(
            Component.literal(text),
            true  // true = actionbar (above hotbar), not chat
        );
    }
}
