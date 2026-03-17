package net.makecoffee.items.listener;

import net.makecoffee.items.util.CooldownManager;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.UseCooldown;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ItemListener {

    public ItemListener(@NotNull EventNode<@NotNull Event> node, @NotNull CooldownManager cooldownManager) {
        node.addListener(PlayerBeginItemUseEvent.class, event -> {
            Optional<UseCooldown> optional = cooldown(event.getItemStack());
            optional.ifPresent(cooldown -> {
                assert cooldown.cooldownGroup() != null;
                if (cooldownManager.has(event.getPlayer(), cooldown.cooldownGroup())) {
                    event.setCancelled(true);
                }
            });
        });

        node.addListener(PlayerFinishItemUseEvent.class, event -> {
            Optional<UseCooldown> optional = cooldown(event.getItemStack());
            optional.ifPresent(cooldown -> {
                assert cooldown.cooldownGroup() != null;
                cooldownManager.add(event.getPlayer(), cooldown.cooldownGroup(), cooldown.seconds());
            });
        });
    }

    private @NotNull Optional<UseCooldown> cooldown(@NotNull ItemStack is) {
        return Optional.ofNullable(is.get(DataComponents.USE_COOLDOWN))
                .filter(cooldown -> cooldown.cooldownGroup() != null);
    }
}
