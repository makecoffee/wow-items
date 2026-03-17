package net.makecoffee.items.instance;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.makecoffee.items.Constants;
import net.makecoffee.items.data.DataComponentEntry;
import net.makecoffee.items.data.ItemData;
import net.makecoffee.items.data.ItemDisplayData;
import net.minestom.server.component.DataComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record ItemInstance(ItemData data) {

    public @NotNull ItemStack generate(int amount) {
        ItemStack is = ItemStack.of(data.material());
        ItemDisplayData display = data.display();
        for (DataComponentEntry entry : display.components()) {
            is = apply(is, entry.type(), entry.value());
        }
        return is.withAmount(amount)
                .withCustomName(display.formattedName())
                .withLore(display.formattedDescription())
                .withMaxStackSize(data.maxStackSize())
                .withoutExtraTooltip()
                .withTag(Constants.ITEM_TAG, CompoundBinaryTag.builder()
                        .putString(Constants.ITEM_ID_KEY, data.id())
                        .build());
    }

    @SuppressWarnings("unchecked")
    private <T> @NotNull ItemStack apply(@NotNull ItemStack is, @NotNull DataComponent<T> component,
                                         @NotNull Object value) {
        return is.with(component, (T) value);
    }
}
