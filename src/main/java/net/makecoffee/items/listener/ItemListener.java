package net.makecoffee.items.listener;

import net.makecoffee.items.data.ItemData;
import net.makecoffee.items.data.UseMessage;
import net.makecoffee.items.instance.ItemInstance;
import net.makecoffee.items.service.ItemService;
import net.makecoffee.items.util.CooldownManager;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.trait.ItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.UseCooldown;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ItemListener {

    private final CooldownManager cooldownManager;
    private final ItemService itemService;

    public ItemListener(@NotNull EventNode<@NotNull Event> node, @NotNull CooldownManager cooldownManager,
                        @NotNull ItemService itemService) {
        this.cooldownManager = cooldownManager;
        this.itemService = itemService;
        node.addListener(PlayerBeginItemUseEvent.class, this::onBegin);
        node.addListener(PlayerFinishItemUseEvent.class, this::onFinish);
    }

    private void onBegin(@NotNull PlayerBeginItemUseEvent event) {
        ItemStack is = event.getItemStack();
        Player player = event.getPlayer();
        if (hasCooldown(is, player)) {
            event.setCancelled(true);
            return;
        }

        handleUseMessage(is, event, player);
    }

    private void onFinish(@NotNull PlayerFinishItemUseEvent event) {
        ItemStack is = event.getItemStack();
        Player player = event.getPlayer();
        applyCooldown(is, player);
        handleUseMessage(is, event, player);
    }

    private @NotNull Optional<UseCooldown> cooldown(@NotNull ItemStack is) {
        return Optional.ofNullable(is.get(DataComponents.USE_COOLDOWN))
                .filter(cooldown -> cooldown.cooldownGroup() != null);
    }

    private boolean hasCooldown(@NotNull ItemStack is, @NotNull Player player) {
        return cooldown(is)
                .map(cooldown -> {
                    assert cooldown.cooldownGroup() != null;
                    return cooldownManager.has(player, cooldown.cooldownGroup());
                })
                .orElse(false);
    }

    private void applyCooldown(@NotNull ItemStack is, @NotNull Player player) {
        cooldown(is).ifPresent(cooldown -> {
            assert cooldown.cooldownGroup() != null;
            cooldownManager.add(player, cooldown.cooldownGroup(), cooldown.seconds());
        });
    }

    private @NotNull Optional<UseMessage> useMessage(@NotNull ItemStack is) {
        return Optional.ofNullable(itemService.createInstanceFromItemStack(is))
                .map(ItemInstance::data)
                .map(ItemData::useMessage)
                .filter(message -> message != UseMessage.EMPTY);

    }

    private void handleUseMessage(@NotNull ItemStack is, @NotNull ItemEvent itemEvent, @NotNull Player player) {
        useMessage(is).ifPresent(useMessage -> useMessage.handle(itemEvent, player));
    }
}
