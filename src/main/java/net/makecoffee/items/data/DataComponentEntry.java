package net.makecoffee.items.data;

import net.minestom.server.component.DataComponent;

public record DataComponentEntry(DataComponent<?> type, Object value) {
}
