package net.makecoffee.items.data;

import net.kyori.adventure.text.Component;

import java.util.List;

public record ItemDisplayData(Component name, List<Component> description, List<DataComponentEntry> components) {

    public ItemDisplayData {
        if (description == null) {
            description = List.of();
        }
    }
}
