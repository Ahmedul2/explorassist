package com.explorassist;

import com.explorassist.engine.CooldownManager;
import com.explorassist.events.PlayerLoginHandler;
import com.explorassist.events.FdCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("explorassist")
public class ExplorerAssistMod {

    public static final String MODID = "explorassist";
    public static final String TARGET_PLAYER = "Ahmedul2";

    public ExplorerAssistMod() {
        MinecraftForge.EVENT_BUS.register(new PlayerLoginHandler());
        MinecraftForge.EVENT_BUS.register(new FdCommandHandler());
    }
}
