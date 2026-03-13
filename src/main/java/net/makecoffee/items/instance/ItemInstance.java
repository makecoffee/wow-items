package net.makecoffee.items.instance;

import net.kyori.adventure.text.format.TextDecoration;
import net.makecoffee.items.data.DataComponentEntry;
import net.makecoffee.items.data.ItemData;
import net.minestom.server.component.DataComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemInstance(ItemData data) {

    public @NotNull ItemStack generate(int amount) {
        ItemStack is = ItemStack.of(data.material());
        for (DataComponentEntry entry : data.display().components()) {
            is = apply(is, entry.type(), entry.value());
        }
        return is.withAmount(amount)
                .withCustomName(data.display()
                        .name()
                        .decoration(TextDecoration.ITALIC, false))
                .withLore(data.display()
                        .description()
                        .stream()
                        .map(component ->
                                component.decoration(TextDecoration.ITALIC, false))
                        .toList())
                .withMaxStackSize(data.maxStackSize())
                .withoutExtraTooltip();
    }

    @SuppressWarnings("unchecked")
    private <T> @NotNull ItemStack apply(@NotNull ItemStack is, @NotNull DataComponent<T> component,
                                         @NotNull Object value) {
        return is.with(component, (T) value);
    }
}
