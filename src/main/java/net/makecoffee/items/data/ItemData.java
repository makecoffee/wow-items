package net.makecoffee.items.data;

import com.google.gson.annotations.SerializedName;
import net.makecoffee.items.Constants;
import net.minestom.server.item.Material;

public record ItemData(String id, Material material, ItemDisplayData display, int maxStackSize,
                       @SerializedName("use_message") UseMessage useMessage) {

    public ItemData {
        if (maxStackSize == 0) {
            maxStackSize = Constants.ITEM_MAX_STACK_SIZE;
        }

        if (useMessage == null) {
            useMessage = UseMessage.empty();
        }
    }
}
