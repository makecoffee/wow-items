package net.makecoffee.items;

import net.makecoffee.items.command.ItemCommand;
import net.makecoffee.items.listener.ItemListener;
import net.makecoffee.items.repository.ItemRepository;
import net.makecoffee.items.service.ItemService;
import net.makecoffee.items.util.CooldownManager;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public final class WowItems {

    private static final String DEFAULT_HOST = "0.0.0.0";
    private static final int DEFAULT_PORT = 25565;
    private static final Block DEFAULT_BLOCK = Block.STONE;
    private static final Pos DEFAULT_SPAWN_POS = new Pos(0, 42, 0);

    private static GlobalEventHandler globalEventHandler;
    private final CooldownManager cooldownManager = new CooldownManager();

    private InstanceContainer instanceContainer;
    private ItemRepository itemRepository;
    private ItemService itemService;

    public void start() {
        MinecraftServer server = MinecraftServer.init(new Auth.Online());
        globalEventHandler = MinecraftServer.getGlobalEventHandler();

        itemRepository = new ItemRepository();
        itemService = new ItemService(itemRepository);
        itemService.loadFromJson();

        configureInstance();
        configureListeners();

        MinecraftServer.getCommandManager().register(new ItemCommand(itemRepository, itemService));
        server.start(DEFAULT_HOST, DEFAULT_PORT);
    }

    private void configureInstance() {
        instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier()
                .fillHeight(0, 40, DEFAULT_BLOCK));
        instanceContainer.setChunkSupplier(LightingChunk::new);
    }

    private void configureListeners() {
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instanceContainer);
            Player player = event.getPlayer();
            player.setRespawnPoint(DEFAULT_SPAWN_POS);
            player.setGameMode(GameMode.CREATIVE);
        });

        new ItemListener(globalEventHandler, cooldownManager, itemService);
    }

    public CooldownManager cooldownManager() {
        return cooldownManager;
    }

    public ItemRepository itemRepository() {
        return itemRepository;
    }

    public ItemService itemService() {
        return itemService;
    }
}
