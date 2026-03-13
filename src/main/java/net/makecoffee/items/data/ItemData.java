package net.makecoffee.items.data;

import net.makecoffee.items.Constants;
import net.minestom.server.item.Material;

public record ItemData(String id, Material material, ItemDisplayData display, int maxStackSize) {

    public ItemData {
        if (maxStackSize == 0) {
            maxStackSize = Constants.ITEM_MAX_STACK_SIZE;
        }
    }
}
