package net.makecoffee.items.data;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.trait.ItemEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record UseMessage(Type type, Component message) {

    public static final UseMessage EMPTY = new UseMessage(null, Component.empty());

    public static @NotNull UseMessage empty() {
        return EMPTY;
    }

    public UseMessage {
        if (type == null) {
            type = Type.DO_NOT_CARE;
        }
    }

    public void handle(@NotNull ItemEvent itemEvent, @NotNull Player player) {
        if (type.matches(itemEvent)) {
            player.sendMessage(message);
        }
    }

    public enum Type {
        BEGIN(PlayerBeginItemUseEvent.class),
        FINISH(PlayerFinishItemUseEvent.class),
        DO_NOT_CARE(PlayerBeginItemUseEvent.class, PlayerFinishItemUseEvent.class);

        private final Set<Class<? extends ItemEvent>> eventTypes;

        @SafeVarargs
        Type(Class<? extends ItemEvent>... classes) {
            this.eventTypes = Set.of(classes);
        }

        public boolean matches(@NotNull ItemEvent event) {
            return eventTypes.stream().anyMatch(clazz -> clazz.isInstance(event));
        }
    }
}
