package net.makecoffee.items.listener;

import net.makecoffee.items.util.CooldownManager;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.item.component.UseCooldown;
import org.jetbrains.annotations.NotNull;

public final class ItemListener {

    public ItemListener(@NotNull EventNode<@NotNull Event> node, @NotNull CooldownManager cooldownManager) {
        node.addListener(PlayerBeginItemUseEvent.class, event -> {
            UseCooldown cooldown = event.getItemStack().get(DataComponents.USE_COOLDOWN);
            if (cooldown == null) {
                return;
            }

            String cooldownGroup = cooldown.cooldownGroup();
            if (cooldownGroup == null) {
                return;
            }

            if (cooldownManager.has(event.getPlayer(), cooldownGroup)) {
                event.setCancelled(true);
            }
        });

        node.addListener(PlayerFinishItemUseEvent.class, event -> {
            UseCooldown cooldown = event.getItemStack().get(DataComponents.USE_COOLDOWN);
            if (cooldown == null) {
                return;
            }

            String cooldownGroup = cooldown.cooldownGroup();
            if (cooldownGroup == null) {
                return;
            }

            cooldownManager.add(event.getPlayer(), cooldownGroup, cooldown.seconds());
        });
    }
}
