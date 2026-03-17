package net.makecoffee.items.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ItemDisplayData(Component name, List<Component> description, List<DataComponentEntry> components) {

    public ItemDisplayData {
        if (description == null) {
            description = List.of();
        }
    }

    public @NotNull Component formattedName() {
        return applyDecoration(name);
    }

    public @NonNull @Unmodifiable List<Component> formattedDescription() {
        return description.stream()
                .map(this::applyDecoration)
                .toList();
    }

    private @NotNull Component applyDecoration(@NotNull Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }
}
