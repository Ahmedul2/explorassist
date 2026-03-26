package com.explorassist.events;

import com.explorassist.ExplorerAssistMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerLoginHandler {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // Silently check — no message, no action for other players
        String name = event.getEntity().getName().getString();
        if (!name.equals(ExplorerAssistMod.TARGET_PLAYER)) {
            return;
        }
        // Target player joined — system is active, nothing visible happens
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        String name = event.getEntity().getName().getString();
        if (!name.equals(ExplorerAssistMod.TARGET_PLAYER)) {
            return;
        }
        // Could reset cooldown on logout if desired
    }
}
