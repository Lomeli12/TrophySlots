package net.lomeli.trophyslots;

import net.lomeli.trophyslots.client.ClientProxy;
import net.lomeli.trophyslots.core.CommonProxy;
import net.lomeli.trophyslots.core.IProxy;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.lomeli.trophyslots.client.ClientConfig;
import net.lomeli.trophyslots.core.ServerConfig;
import net.lomeli.trophyslots.core.capabilities.IPlayerSlots;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotManager;
import net.lomeli.trophyslots.core.capabilities.PlayerSlotStorage;
import net.lomeli.trophyslots.core.command.ModCommands;
import net.lomeli.trophyslots.core.criterion.ModCriteria;
import net.lomeli.trophyslots.core.network.PacketHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TrophySlots.MOD_ID)
public class TrophySlots {
    public static final String MOD_ID = "trophyslots";
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ClientConfig CLIENT;
    public static final ServerConfig SERVER;
    static final String MOD_NAME = "Trophy Slots";
    public static Logger log = LogManager.getLogger(MOD_NAME);

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
    }

    public TrophySlots() {
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }

    public void commonInit(final FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
        ModCriteria.initTriggers();
        CapabilityManager.INSTANCE.register(IPlayerSlots.class, new PlayerSlotStorage(), PlayerSlotManager::new);
    }

    public void serverStarting(final FMLServerStartingEvent event) {
        ModCommands.registerCommands(event.getCommandDispatcher());
    }
}