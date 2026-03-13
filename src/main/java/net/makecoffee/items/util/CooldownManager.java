package net.makecoffee.items.util;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownManager {

    private static final long ONE_SECOND = 1000L;
    private static final long DEFAULT_VALUE = 0L;

    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public boolean has(@NotNull Player player, @NotNull String key) {
        Map<String, Long> map = cooldowns.get(player.getUuid());
        if (map == null) {
            return false;
        }

        long endTime = map.getOrDefault(key, DEFAULT_VALUE);
        return System.currentTimeMillis() < endTime;
    }

    public void add(@NotNull Player player, @NotNull String key, float cooldown) {
        cooldowns.computeIfAbsent(player.getUuid(), (_) -> new HashMap<>())
                .put(key, System.currentTimeMillis() + (long) (cooldown * ONE_SECOND));
    }
}
